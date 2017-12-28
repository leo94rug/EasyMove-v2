(function () {
    'use strict';
    angular
            .module('app')
            .controller('LoginController', LoginController);

    LoginController.$inject = ['$timeout', 'AuthenticationService', 'AccountService', 'FlashService', '$location', '$store'];

    function LoginController($timeout, AuthenticationService, AccountService, FlashService, $location, $store) {
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
                            vm.notconfirmed = true;
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
                        response.res.data.anno_nascita = Date.createFromMysql(response.res.data.anno_nascita_string);
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
        Date.createFromMysql = function (mysql_string) {
            var t, result = null;
            if (typeof mysql_string === 'string') {
                t = mysql_string.split(/[- :]/);
                result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);
            }
            return result;
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