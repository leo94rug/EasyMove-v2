(function () {
    'use strict';

    angular
        .module('app')
        .controller('MenuGestioneController', MenuGestioneController);

    MenuGestioneController.$inject = ['$location','AuthenticationService'];
    function MenuGestioneController($location,AuthenticationService) {
        var vm = this;
        vm.logout=logout;
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
    }
})();
