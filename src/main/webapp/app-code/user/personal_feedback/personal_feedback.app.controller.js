(function () {
    'use strict';

    angular
            .module('app')
            .controller('FeedbackRicevutiController', FeedbackRicevutiController)
            .config(function ($mdThemingProvider) {
                $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
                $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
                $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
                $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
            });

    FeedbackRicevutiController.$inject = ['DateService', '$timeout', 'FeedbackService', '$location', '$store', 'FlashService', 'AuthenticationService'];
    function FeedbackRicevutiController(DateService, $timeout, FeedbackService, $location, $store, FlashService, AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;
        vm.logout = logout;
        initialize();

        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.pop({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }

        function initialize() {
            vm.utente = $store.get('utente');
            $('body,html').animate({scrollTop: 0}, 800);
            $timeout(function () {
                var myEl = angular.element(document.querySelector('#headerBacheca'));
                myEl.addClass('active');
            });
            FeedbackService.GetFeedback(vm.utente.id).then(function (response) {
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
                            AuthenticationService.ClearCredentials();
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
                    vm.feedback = response.res.data;
                    vm.feedback.listaFeedback.forEach(function (arrayItem) {
                        arrayItem.date = DateService.dateFromString(arrayItem.date);
                    });

                }
            });
        }
    }

})();
