(function () {
    'use strict';
angular
  .module('app')
  .controller('ErrorController', ErrorController);

    ErrorController.$inject = ['$timeout','FlashService','AuthenticationService','$location','$store'];

    function ErrorController($timeout,FlashService,AuthenticationService,$location,$store){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.utente = $store.get('utente');
        vm.logout=logout;
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
    }
})();