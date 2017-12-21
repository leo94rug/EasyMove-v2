(function () {
    'use strict';

    angular
        .module('app')
        .controller('DelAddBusController', DelAddBusController);

    DelAddBusController.$inject = ['UserService', '$location', '$rootScope', 'GestioneService','AuthenticationService'];
    function DelAddBusController(UserService, $location, $rootScope, GestioneService,AuthenticationService) {
        var vm = this;
        vm.sub = sub;
        vm.add=add;
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
            GestioneService.GetAll().then(function (response) {
                if(response.success===false){}
                else{
                    vm.autobus=response;
                }
            });
        }
        function loadTratte(){
            GestioneService.GetById(vm.idBus).then(function(response){
                if(response.success===false){}
                else{
                    vm.tratte=response;
                }
            });
        }
        function add(precedente,posizione){
          var data="{precedente: "+precedente+ ",autobus: "+vm.idBus+",posizione:"+posizione+"}";
            GestioneService.Add(data).then(function(response){
                if(response.success===false){}
                else{
                    
                    vm.loadTratte();
                }
            });
        }
        function sub(){
            GestioneService.Create(vm.tratte).then(function(response){});
            //loadTratte();
        }
    }

})();
