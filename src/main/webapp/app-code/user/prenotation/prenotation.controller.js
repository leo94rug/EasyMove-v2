(function () {
    'use strict';

    angular
            .module('app')
            .controller('PrenotationController', PrenotationController)
            .config(function ($mdThemingProvider) {
                $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
                $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
                $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
                $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
            });

    PrenotationController.$inject = ['$timeout', 'FeedbackService', 'NotificationsService', '$scope', '$store', 'RouteService', '$location', 'UserService', '$rootScope', 'FlashService', '$routeParams', '$mdDialog', 'AuthenticationService'];
    function PrenotationController($timeout, FeedbackService, NotificationsService, $scope, $store, RouteService, $location, UserService, $rootScope, FlashService, $routeParams, $mdDialog, AuthenticationService) {
        var vm = this;
        vm.tratta1 = $routeParams.tratta1;
        vm.tratta2 = $routeParams.tratta2;
        vm.initializa = initialize;
        vm.visualizzautente = visualizzautente;
        vm.richiestaAmicizia = richiestaAmicizia;
        vm.showAlert = showAlert;
        vm.prenota = prenota;
        vm.logout = logout;
        vm.options = [
            {category: 'posti', name: '1', value: 1},
            {category: 'posti', name: '2', value: 2},
            {category: 'posti', name: '3', value: 3},
            {category: 'posti', name: '4', value: 4},
        ];
        initialize();
        function initialize() {
            vm.utente = $store.get('utente');
            $('body,html').animate({scrollTop: 0}, 800);
            vm.disabled_amicizia = true;
            vm.disabled_prenotazione = true;
            vm.messaggio = "";
            if (vm.utente) {
                RouteService.GetDettaglioPercorso(vm.tratta1, vm.tratta2).then(function (response) {
                    if (response.success === false) {
                        $location.path('/error');
                    } else {
                        vm.dettaglioPercorso = response.res.data;
                        debugger;//TODO:controllare il disable delle prenotazioni
                        vm.dettaglioPercorso.orario_partenza = Date.createFromMysql(vm.dettaglioPercorso.orario_partenza_string);
                        RouteService.GetPercorso(vm.dettaglioPercorso.viaggio_fk).then(function (response) {
                            if (response.success === false) {
                                $location.path('/error');
                            } else {
                                vm.percorso = response.res.data;
                                vm.stringaPercorso = "";
                                for (var i = 0; i < vm.percorso.length; i++) {
                                    vm.stringaPercorso += vm.percorso[i].denominazione_partenza;
                                    vm.stringaPercorso += " -> ";
                                    if (i == vm.percorso.length - 1) {
                                        vm.stringaPercorso += vm.percorso[i].denominazione_arrivo;
                                    }
                                }
                            }
                        });
                        RouteService.GetViaggio(vm.dettaglioPercorso.viaggio_fk).then(function (response) {
                            if (response.success === false) {
                                $location.path('/error');
                            } else {
                                vm.viaggio = response.res.data;
                                UserService.GetProfilo(vm.viaggio.utente_fk).then(function (response) {
                                    if (response.success === false) {
                                        $location.path('/error');
                                    } else {
                                        vm.utenteProfilo = response.res.data;
                                        UserService.TravelNumber(vm.utenteProfilo.id).then(function (response) {
                                            if (response.success === false) {
                                                $location.path('/error');
                                            } else {
                                                vm.numbertravel = response.res.data;
                                            }
                                        });
                                        FeedbackService.GetFeedback(vm.utenteProfilo.id).then(function (response) {
                                            if (response.success === false) {
                                                $location.path('/error');
                                            } else {
                                                vm.feedback = response.res.data;
                                                vm.feedback.listaFeedback.forEach(function (arrayItem) {
                                                    arrayItem.dateString = Date.createFromMysql(arrayItem.dateString);
                                                });
                                                debugger;
                                            }
                                        });
                                        UserService.CheckFriend(vm.utenteProfilo.id, vm.utente.id).then(function (response) {
                                            if (response.success === false) {
                                                $location.path('/error');
                                            } else {
                                                vm.amicizia = response.res.data;
                                                switch (vm.amicizia) {
                                                    case 0 :
                                                    {
                                                        vm.disabled_amicizia = false;
                                                        vm.disabled_prenotazione = true;
                                                    }                                                    
                                                    case 1 :
                                                    {
                                                        vm.disabled_amicizia = true;
                                                        vm.disabled_prenotazione = true;
                                                    }                                                    
                                                    case 2 :
                                                    {
                                                        vm.disabled_amicizia = true;
                                                    }                                                    
                                                    case 3 :
                                                    {
                                                        vm.disabled_amicizia = true;
                                                        vm.disabled_prenotazione = true;
                                                    }
                                                    default:{
                                                        vm.disabled_amicizia = true;
                                                        vm.disabled_prenotazione = true;       
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                $location.path('/');
            }
        }
        $timeout(function () {
            var myEl = angular.element(document.querySelector('#headerSearch'));
            myEl.addClass('active');
        });

        function prenota() {
            //if ($scope.offer.$valid) {
            var stringPosti = " posti";
            if (vm.posti == 1) {
                stringPosti = " posto"
            }
            if (vm.posti <= vm.dettaglioPercorso.posti) {
                var send = new Object();
                send.messaggio = vm.utente.nome + " " + vm.utente.cognome + ' vorrebbe prenotare ' + vm.posti + stringPosti;
                send.posti_da_prenotare = vm.posti;
                send.mittente = vm.utente.id;
                send.destinatario = vm.viaggio.utente_fk;
                send.tipologia = 3;
                send.id_viaggio = vm.viaggio.id;
                send.fine_validita = new Date(2020, 1, 1).getTime();
                send.inizio_validita = new Date().getTime();
                send.posti = vm.dettaglioPercorso.posti;
                send.nome_viaggio = "Da " + vm.dettaglioPercorso.denominazione_partenza + " a " + vm.dettaglioPercorso.denominazione_arrivo;
                send.id_partenza = vm.tratta1;
                send.id_arrivo = vm.tratta2;
                NotificationsService.inviaNotifica(send).then(function (response) {
                    if (response.success === false) {
                        $location.path('/error');
                    } else {
                        vm.disabled_prenotazione = true;
                        vm.messaggio = "Abbiamo inviato il messaggio";
                    }
                });
            } else {
                vm.messaggio = "Sono disponibili " + vm.dettaglioPercorso.posti + stringPosti;
            }
            //}
        }
        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function showAlert(ev) {
            $mdDialog.show(
                    $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title('Numero di telefono')
                    .textContent(vm.travel.utente.telefono1)
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Close')
                    .targetEvent(ev)
                    );
        }
        ;
        function richiestaAmicizia(ev) {
            var confirm = $mdDialog.prompt()
                    .title('Invio richiesta di visualizzazione dei dati personali')
                    .textContent('Per prenotare un posto l\'utente deve accetare di condividere i suoi dati, come ad esempio il numero di telefono. Riceverai una notifica appena sarà possibile prenotare un posto')
                    .placeholder('Messaggio')
                    .ariaLabel('Messaggio')
                    .initialValue('Inserisci un messaggio ...')
                    .targetEvent(ev)
                    .ok('Send!')
                    .cancel('Cancel');

            $mdDialog.show(confirm).then(function (result) {
                var send = new Object();
                send.messaggio = result;
                send.mittente = vm.utente.id;
                send.destinatario = vm.viaggio.utente_fk;
                send.tipologia = 1;
                send.id_viaggio = vm.viaggio.id;
                send.fine_validita = new Date(2020, 1, 1).getTime();
                send.inizio_validita = new Date().getTime();
                send.posti = vm.dettaglioPercorso.posti_liberi;
                send.nome_viaggio = "Da " + vm.dettaglioPercorso.denominazione_partenza + " a " + vm.dettaglioPercorso.denominazione_arrivo;
                //send.nome_viaggio="";
                send.id_partenza = vm.tratta1;
                send.id_arrivo = vm.tratta2;
                NotificationsService.inviaNotifica(send).then(function (response) {
                    if (response.success === false) {
                        $location.path('/error');
                    } else {
                        vm.disabled_amicizia = true;
                        vm.messaggio = "Abbiamo inviato il messaggio";
                    }
                });
            },
                    function () {
                        vm.messaggio = 'Non è stato inviato';
                    });
        }
        function visualizzautente(email) {
            $location.path('/profilo-utenti/' + email);
        }
        Date.createFromMysql = function (mysql_string) {
            var t, result = null;
            if (typeof mysql_string === 'string') {
                t = mysql_string.split(/[- :]/);
                result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);
            }
            return result;
        }
    }
})();
