(function () {
    'use strict';
    angular
            .module('app')
            .controller('LoginController', LoginController);

    LoginController.$inject = ['DateService','$timeout', 'AuthenticationService', 'AccountService', 'FlashService', '$location', '$store'];

    function LoginController(DateService,$timeout, AuthenticationService, AccountService, FlashService, $location, $store) {
        var vm = this;
        $('body,html').animate({scrollTop: 0}, 800);
        vm.login = login;
        vm.utente = $store.get('utente');
        vm.notconfirmed = false;
        vm.resendEmail = resendEmail;
        $timeout(function () {
            var myEl = angular.element(document.querySelector('#headerLogin'));
            myEl.addClass('active');
        });
        vm.email;
        function login() {
            if (vm.loginUser != undefined) {
                vm.email = vm.loginUser.email;
            }
            AccountService.Login(vm.loginUser).then(function (response) {
                if (response.success === false) {
                    vm.email = undefined;

                    switch (response.res.status) {
                        case 500:
                        {
                            $location.path('/error');
                            break;
                        }
                        case 499:
                        {
                            $('body,html').animate({scrollTop: 0}, 800);
                            //vm.notconfirmed = true;
                            FlashService.pop({title: "Attenzione!", body: "Email non confermata", type: "warning"});
                            break;
                        }
                        case 404:
                        {
                            $('body,html').animate({scrollTop: 0}, 800);
                            FlashService.pop({title: "Attenzione!", body: "Password o email errati", type: "warning"});
                            break;
                        }
                        default:
                        {
                            $location.path('/error');
                            break;
                        }
                    }
                } else {
                    if (response.res.data.anno_nascita != null && response.res.data.anno_nascita != '') {
                        response.res.data.anno_nascita = DateService.dateFromString(response.res.data.anno_nascita);
                    }
                    AuthenticationService.SetCredentials(response.res.data, vm.coll);
                    $('body,html').animate({scrollTop: 0}, 800);
                    vm.email = undefined;
                    var message='';
                    switch (response.res.data.tipo) {
                        case 1:{message = 'Sei loggato!';break;}
                        case 2:{message = 'Sei loggato come admin!';break;}
                    }
                    FlashService.set({title: message, body: "", type: "success"});
                    $location.path('/');
                }
            });
        }
        function resendEmail() {
            AccountService.ResendEmail(vm.email).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                } else {
                    FlashService.set({title: 'Ti abbiamo reinviato un email di conferma', body: "", type: "success"});
                    $location.path('/');
                }
            });
        }
    }
})();