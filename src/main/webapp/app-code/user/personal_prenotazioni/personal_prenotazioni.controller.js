(function () {
    'use strict';

    angular
            .module('app')
            .controller('PersonalPrenotazioniController', PersonalPrenotazioniController);

    PersonalPrenotazioniController.$inject = ['$timeout', 'PrenotationService', '$location', '$store', 'FlashService', 'AuthenticationService'];
    function PersonalPrenotazioniController($timeout, PrenotationService, $location, $store, FlashService, AuthenticationService) {
        var vm = this;
        $('body,html').animate({scrollTop: 0}, 800);
        vm.initialize = initialize;
        initialize();
        vm.logout = logout;
        $timeout(function () {
            var myEl = angular.element(document.querySelector('#headerBacheca'));
            myEl.addClass('active');
        });

        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.pop({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }

        function initialize() {
            vm.utente = $store.get('utente');
            PrenotationService.GetPrenotazioni(vm.utente.id).then(function (response) {
                if (response.success === false) {
                    switch (response.res.status) {
                        case 500:
                        {
                            $location.path('/error');
                            break;
                        }
                        case 401:
                        {
                            $('body,html').animate({scrollTop: 0}, 800);
                            FlashService.set({title: "Attenzione!", body: "Effettua il login per continuare", type: "warning"});
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
                    vm.prenotazioni = response.res.data;
                    vm.prenotazioni.forEach(function (arrayItem) {
                        arrayItem.tratta_auto.orario_partenza = new Date(arrayItem.tratta_auto.orario_partenza);
                    });
                }
            });
        }
    }

})();
