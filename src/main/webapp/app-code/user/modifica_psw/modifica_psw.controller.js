(function () {
    'use strict';
    angular
            .module('app')
            .controller('ModificaPswController', ModificaPswController);

    ModificaPswController.$inject = ['$timeout', 'FlashService', 'AccountService', 'AuthenticationService', '$store', '$location'];

    function ModificaPswController($timeout, FlashService, AccountService, AuthenticationService, $store, $location) {
        var vm = this;
        $('body,html').animate({scrollTop: 0}, 800);
        vm.utente = $store.get('utente');
        vm.modificaPsw = modificaPsw;
        vm.logout = logout;
        $timeout(function () {
            var myEl = angular.element(document.querySelector('#headerLogin'));
            myEl.addClass('active');
        });
        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function modificaPsw() {
            if (vm.password.nuova_password !== vm.password.ripeti_password) {
                $('body,html').animate({scrollTop: 0}, 800);
                FlashService.pop({title: "Attezione!", body: "Le password non coincidono", type: "warning"});
            } else {
                var utente = {
                    email: vm.utente.email,
                    password: vm.password.vecchia_password
                };
                AccountService.Login(utente).then(function (response) {
                    if (response.success === false) {
                        $location.path('/error');
                    } else {
                        if (response === 0) {
                            $('body,html').animate({scrollTop: 0}, 800);
                            FlashService.pop({title: "Attezione!", body: "Password errata", type: "warning"});
                        } else {
                            var utente_mod = {
                                id: vm.utente.id,
                                password: vm.password.nuova_password
                            };
                            AccountService.ModificaPsw(utente_mod).then(function (response) {
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
                                    FlashService.set({title: "Password modificata", body: "", type: "success"});
                                    $location.path('/');
                                }
                            });
                        }
                    }
                });
            }
        }
    }
})();