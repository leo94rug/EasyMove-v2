(function () {
    'use strict';

    angular
        .module('app')
        .controller('FunzioniFermateController', FunzioniFermateController);

    FunzioniFermateController.$inject = ['UserService', '$location', '$rootScope', 'GestioneService','AuthenticationService'];
    function FunzioniFermateController(UserService, $location, $rootScope, GestioneService,AuthenticationService) {
        var vm = this;
        vm.sub = sub;
        vm.loadTratte=loadTratte;
        vm.register1 = register1;
        vm.autobus = [];
        vm.tratte=[];
register1();
        vm.logout=logout;
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        function register1() {
            GestioneService.GetAll()
                .then(function (response) {
                        vm.autobus=response;
                }); 
        }
        function loadTratte(){
            var id=vm.idBus;
            GestioneService.GetById(id).then(function(response){
                vm.tratte=response;

            })
        }
        function sub(){
            GestioneService.Create(vm.tratte).then(function(response){});
            //loadTratte();
        }
    }

})();
