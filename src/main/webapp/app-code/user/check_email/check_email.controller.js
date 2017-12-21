(function () {
    'use strict';
angular
  .module('app')
  .controller('CheckEmailController', CheckEmailController);

    CheckEmailController.$inject = ['$timeout','FlashService','UserService','AuthenticationService','$routeParams','$store','$location'];

    function CheckEmailController($timeout,FlashService,UserService,AuthenticationService,$routeParams,$store,$location){
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
            UserService.ConfermaEmail(jsonString).then(function (response) {
                if(response.res.success===false){
                    $location.path('/error');
                }
                else{
                    FlashService.set({title: "Hai confermato la tua email", body: "", type: "info"});
                    $location.path("/login");
                }
            });
        }
}


  })();