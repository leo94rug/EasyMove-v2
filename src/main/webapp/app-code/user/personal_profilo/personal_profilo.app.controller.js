(function () {
    'use strict';

    angular
        .module('app')
        .controller('ProfiloUtenteController', ProfiloUtenteController);

    ProfiloUtenteController.$inject = ['DateService', '$timeout', 'CarService', 'UserService', '$location', '$store', 'FlashService', 'AuthenticationService'];
    function ProfiloUtenteController(DateService, $timeout, CarService, UserService, $location, $store, FlashService, AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;
        vm.image_loading = "";
        vm.update = update;
        vm.showImage = showImage;
        vm.showPic = false;
        vm.upload = upload;
        vm.addcar = addcar;
        vm.logout = logout;
        vm.deletecar = deletecar;
        vm.minDate = new Date(1900, 0, 1);
        vm.maxDate = new Date(2006, 0, 1);
        initialize();
        $timeout(function () {
            var myEl = angular.element(document.querySelector('#headerBacheca'));
            myEl.addClass('active');
        });
        Date.createFromMysql = function (mysql_string) {
            var t, result = null;
            if (typeof mysql_string === 'string') {
                t = mysql_string.split(/[- :]/);
                result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);
            }
            return result;
        }
        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({ title: "Logout effettuato", body: "", type: "info" });
            $location.path('/');
        }
        function initialize() {
            $('body,html').animate({ scrollTop: 0 }, 800);

            var localUtente = $store.get('utente');

            UserService.GetProfilo(localUtente.id).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                }
                else {
                    response.res.data.anno_nascita = DateService.dateFromString(response.res.data.anno_nascita);

                    vm.utente = response.res.data;
                    CarService.GetAuto(vm.utente.id).then(function (response) {
                        if (response.success === false) {
                            $location.path('/error');
                        }
                        else {
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
            }
            UserService.Update(utente, vm.utente.id).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                }
                else {

                    FlashService.pop({ title: "Profilo aggiornato!", body: "", type: "info" });
                    initialize();
                    //AuthenticationService.UpdateCredentials(vm.utente);
                }
            });
        }
        function upload(dataUrl, name) {
            vm.image_loading = "Caricamento in corso ..";
            var obj = new Object();
            obj.immagine = dataUrl;
            obj.id = vm.utente.id;
            debugger;
            var jsonString = JSON.stringify(obj);
            UserService.UpdateImage(jsonString).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                }
                else {
                    vm.image_loading = "";
                    FlashService.pop({ title: "Immagine inserita", body: "", type: "info" });
                }
            });
        }
        function addcar(auto) {
            debugger;
            if (vm.gestioneAutoForm.$valid) {
                vm.gestioneAutoForm.$submitted = false;
                var autoToSend = {
                    "marca": auto.marca,
                    "modello": auto.modello,
                    "colore": auto.colore,
                    "utente_fk": vm.utente.id
                }
                CarService.CreateCar(autoToSend).then(function (response) {
                    if (response.success === false) {
                        $location.path('/error');
                    }
                    else {
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
                    $location.path('/error');
                }
                else {
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
