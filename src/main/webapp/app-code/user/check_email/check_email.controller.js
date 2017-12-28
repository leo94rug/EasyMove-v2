(function () {
    'use strict';
angular
  .module('app')
  .controller('CheckEmailController', CheckEmailController);

    CheckEmailController.$inject = ['$timeout','FlashService','AccountService','AuthenticationService','$routeParams','$store','$location'];

    function CheckEmailController($timeout,FlashService,AccountService,AuthenticationService,$routeParams,$store,$location){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.message="Stiamo confermando la tua email ... ";
        vm.utente = $store.get('utente');
        vm.inserisci=inserisci;
        vm.logout=logout;
        vm.inserisci();
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerRegister' ) );
            myEl.addClass('active');           
        });      
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/login');
        }
        function inserisci() {
            var obj = new Object();
            obj.email = $routeParams.email;
            obj.hash  = $routeParams.hash;
            var jsonString= JSON.stringify(obj);
            AccountService.ConfermaEmail(jsonString).then(function (response) {
                if(response.res.success===false){
                    switch (response.res.status) {
                        case 500:
                        {
                            $location.path('/error');
                            break;
                        }
                        case 498:
                        {
                            $('body,html').animate({scrollTop: 0}, 800);
                            FlashService.pop({title: "Attenzione!", body: "Email già confermata", type: "warning"});
                            break;
                        }
                        case 404:
                        {
                            $('body,html').animate({scrollTop: 0}, 800);
                            FlashService.pop({title: "Attenzione!", body: "Email errata", type: "warning"});
                            break;
                        }
                        default:
                        {
                            $location.path('/error');
                            break;
                        }
                    }                }
                else{
                    FlashService.set({title: "Hai confermato la tua email", body: "", type: "info"});
                    $location.path("/login");
                }
            });
        }
}


  })();