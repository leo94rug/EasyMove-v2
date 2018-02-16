(function () {
    'use strict';

    angular
        .module('app')
        .controller('ProfiloUtenteController', ProfiloUtenteController);

    ProfiloUtenteController.$inject = ['DateService', '$timeout', 'CarService', 'UserService', '$location', '$store', 'FlashService', 'AuthenticationService'];
    function ProfiloUtenteController(DateService, $timeout, CarService, UserService, $location, $store, FlashService, AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;
        vm.update = update;
        vm.showImage = showImage;
        vm.upload = upload;
        vm.addcar = addcar;
        vm.logout = logout;
        vm.deletecar = deletecar;
        initialize();

        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({ title: "Logout effettuato", body: "", type: "info" });
            $location.path('/');
        }
        function initialize() {
            vm.image_loading = "";
            vm.showPic = false;
            vm.minDate = new Date(1900, 0, 1);
            vm.maxDate = new Date(2006, 0, 1);
            $('body,html').animate({ scrollTop: 0 }, 800);
            $timeout(function () {
                var myEl = angular.element(document.querySelector('#headerBacheca'));
                myEl.addClass('active');
            });
            var localUtente = $store.get('utente');
            UserService.GetProfilo(localUtente.id).then(function (response) {
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
                    response.res.data.anno_nascita = DateService.dateFromString(response.res.data.anno_nascita);

                    vm.utente = response.res.data;
                    CarService.GetAuto(vm.utente.id).then(function (response) {
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
                            vm.autolist = response.res.data;
                        }
                    });
                }
            });

        }
        function showImage() {
            vm.showPic = true;
        }
        function update() {
            var utente = {
                "professione": vm.utente.professione,
                "nome": vm.utente.nome,
                "cognome": vm.utente.cognome,
                "biografia": vm.utente.biografia,
                "sesso": vm.utente.sesso,
                "telefono1": vm.utente.telefono1,
                "anno_nascita": DateService.stringFromDate(vm.utente.anno_nascita)
            };

            UserService.Update(utente, vm.utente.id).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                } else {

                    FlashService.pop({ title: "Profilo aggiornato!", body: "", type: "info" });
                    initialize();
                    //AuthenticationService.UpdateCredentials(vm.utente);
                }
            });
        }
        function dataURItoBlob(dataURI) {
            // convert base64 to raw binary data held in a string
            // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
            var byteString = atob(dataURI.split(',')[1]);

            // separate out the mime component
            var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

            // write the bytes of the string to an ArrayBuffer
            var ab = new ArrayBuffer(byteString.length);

            // create a view into the buffer
            var ia = new Uint8Array(ab);

            // set the bytes of the buffer to the correct values
            for (var i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }

            // write the ArrayBuffer to a blob, and you're done
            var blob = new Blob([ab], { type: mimeString });
            return blob;

        }
        function upload(dataUrl, name) {
            vm.image_loading = "Caricamento in corso ..";
            var blob = dataURItoBlob(dataUrl);
            UserService.UpdateImage2(vm.utente.id, blob, name).then(function (response) {
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
                    vm.image_loading = "";
                    FlashService.pop({ title: "Immagine inserita", body: "", type: "info" });
                }
            });
        }
        function addcar(auto) {

            if (vm.gestioneAutoForm.$valid) {
                vm.gestioneAutoForm.$submitted = false;
                var autoToSend = {
                    "marca": auto.marca,
                    "modello": auto.modello,
                    "colore": auto.colore,
                    "utente_fk": vm.utente.id
                };
                CarService.CreateCar(autoToSend).then(function (response) {
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
                        FlashService.pop({ title: "Auto inserita", body: "", type: "info" });
                        vm.autolist.push(autoToSend);
                        vm.auto.marca = "";
                        vm.auto.modello = "";
                        vm.auto.colore = "";
                        vm.gestioneAutoForm.$setUntouched();
                    }
                });
            }
        }
        function deletecar(id) {
            CarService.DeleteCar(id).then(function (response) {
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
                    FlashService.pop({ title: "Auto eliminata", body: "", type: "info" });
                    var index = vm.autolist.findIndex(i => i.id === id);
                    if (index > -1) {
                        vm.autolist.splice(index, 1);
                    }
                }
            });
        }
    }
})();
