(function () {
    'use strict';
angular
  .module('app')
  .controller('ModificaPswController', ModificaPswController);

    ModificaPswController.$inject = ['$timeout','FlashService','UserService','AuthenticationService','$store','$location'];

    function ModificaPswController($timeout,FlashService,UserService,AuthenticationService,$store,$location){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.utente = $store.get('utente');
        vm.modificaPsw=modificaPsw;
        vm.logout=logout;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerLogin' ) );
            myEl.addClass('active');           
        });      
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function modificaPsw() {
            if (vm.password.nuova_password!=vm.password.ripeti_password) {
                $('body,html').animate({scrollTop:0},800);
                FlashService.pop({title: "Attezione!", body: "Le password non coincidono", type: "warning"});
            }
            else{
                var utente = {
                    email:vm.utente.email,
                    password:vm.password.vecchia_password
                }
                UserService.Login(utente).then(function (response) {
                    if(response.success===false){
                        $location.path('/error');
                    }
                    else{
                        if (response===0) {
                            $('body,html').animate({scrollTop:0},800);
                            FlashService.pop({title: "Attezione!", body: "Password errata", type: "warning"});
                        }
                        else{
                            var utente_mod = {
                                email:vm.utente.email,
                                password:vm.password.nuova_password
                            }   
                            UserService.ModificaPsw(utente_mod).then(function (response) {
                                if(response.success===false){
                                    $location.path('/error');
                                }
                                else{
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