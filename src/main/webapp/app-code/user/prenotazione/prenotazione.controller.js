(function () {
    'use strict';
    angular
        .module('app')
        .controller('PrenotazioneController', PrenotazioneController);

    PrenotazioneController.$inject = ['$http', '$routeParams', '$timeout', 'RouteService', 'AccountService', 'FlashService', '$location', '$store','NotificationsService'];

    function PrenotazioneController($http, $routeParams, $timeout, RouteService, AccountService, FlashService, $location, $store,NotificationsService) {
        var vm = this;
        vm.prenota = prenota;
        vm.initialize = initialize;
        vm.initialize();
        function prenota(){
            debugger;
                if (vm.offer.$valid) {
                    if (vm.posti <= vm.postiDisponibili) {
                        var send = new Object();
                        var stringPosti = " posti";
                        if (vm.posti === 1) {
                            stringPosti = " posto";
                        }
                        send.messaggio = vm.utente.nome + " " + vm.utente.cognome + ' vorrebbe prenotare ' + vm.posti + stringPosti;
                        send.posti_da_prenotare = vm.posti;
                        send.mittente = vm.utente.id;
                        send.destinatario = vm.dettaglioPercorso.utente.id;
                        send.tipologia = 3;
                        send.id_viaggio = vm.dettaglioPercorso.viaggio_auto.id;
                        send.posti = vm.posti;
                        send.nome_viaggio = vm.dettaglioPercorso.denominazione_partenza + " a " + vm.dettaglioPercorso.denominazione_arrivo;
                        send.id_partenza = vm.tratta1;
                        send.id_arrivo = vm.tratta2;
                        NotificationsService.inviaNotifica(send).then(function (response) {
                            debugger;
                            if (response.success === false) {
                                switch (response.res.status) {
                                    case 500:
                                        {
                                            $location.path('/error');
                                            break;
                                        }
                                    case 401:
                                        {
                                            $('body,html').animate({ scrollTop: 0 }, 800);
                                            AuthenticationService.ClearCredentials();
                                            FlashService.set({ title: "Attenzione!", body: "Effettua il login per continuare", type: "warning" });
                                            $location.path('/login');
                                            break;
                                        }
                                    default:
                                        {
                                            $location.path('/error');
                                            break;
                                        }
                                }
                            } else {
                                debugger;
                                NotificationsService.eliminaNotifica(vm.idNotifica).then(function (response){
                                    //TODO: gestire risposta
                                    debugger;
                                    FlashService.set({ title: "Hai inviato una richiesta di prenotazione", body: "", type: "info" });
                                    $location.path('/bacheca');
                                });
                                                                
                            }
                        });
                    } else {
                        vm.attenzione = "ATTENZIONE";
                    }
                }
        }
        function initialize() {
            $('body,html').animate({ scrollTop: 0 }, 800);
            vm.utente = $store.get('utente');
            vm.tratta1 = $routeParams.tratta1;
            vm.tratta2 = $routeParams.tratta2;
            vm.idNotifica = $routeParams.notification;
            $timeout(function () {
                var myEl = angular.element(document.querySelector('#headerLogin'));
                myEl.addClass('active');
            });
            RouteService.GetDettaglioPercorso(vm.tratta1, vm.tratta2).then(function (response) {
               debugger; if (response.success === false) {
                    switch (response.res.status) {
                        case 500:
                            {
                                $location.path('/error');
                                break;
                            }
                        case 404:
                            {
                                FlashService.pop({ title: "Il viaggio potrebbe essere stato rimosso", body: "", type: "info" });
                                break;
                            }
                        case 401:
                            {
                                $('body,html').animate({ scrollTop: 0 }, 800);
                                AuthenticationService.ClearCredentials();
                                FlashService.set({ title: "Attenzione!", body: "Effettua il login per continuare", type: "warning" });
                                $location.path('/login');
                                break;
                            }
                        case 410:
                            {
                                FlashService.pop({ title: "Il viaggio potrebbe essere già stato effettuato", body: "", type: "info" });
                                break;
                            }
                    }
                } else {
                    vm.dettaglioPercorso = response.res.data;
                    debugger;
                    vm.postiDisponibili = vm.dettaglioPercorso.posti;
                    vm.attenzione = "";
                    vm.posti = 0;
                    vm.options = [
                        { category: 'posti', name: '1', value: 1 },
                        { category: 'posti', name: '2', value: 2 },
                        { category: 'posti', name: '3', value: 3 },
                        { category: 'posti', name: '4', value: 4 },
                        { category: 'posti', name: '5', value: 5 },
                        { category: 'posti', name: '6', value: 6 }
                    ];

                }
            });
        };

    }
})();