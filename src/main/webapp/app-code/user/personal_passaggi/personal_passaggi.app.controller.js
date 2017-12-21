﻿(function () {
    'use strict';

    angular
        .module('app')
        .controller('PassaggiController', PassaggiController);

    PassaggiController.$inject = ['$timeout','UserService','RouteService', '$location', '$store', 'FlashService','AuthenticationService'];
    function PassaggiController($timeout,UserService,RouteService, $location, $store, FlashService,AuthenticationService) {
        var vm = this;
        vm.initialize=initialize;
        vm.delete=deleted;
        initialize();
        vm.logout=logout;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerBacheca' ) );
            myEl.addClass('active');           
        });      
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.pop({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function initialize(){
            $('body,html').animate({scrollTop:0},800);
            debugger;
            vm.utente = $store.get('utente');
            UserService.GetViaggi(vm.utente.id).then(function (response) {
                if(response.success===false){                    
                    $location.path('/error');
                }
                else{
                    vm.travel=response.res.data;
                    vm.travel.forEach( function (arrayItem){
                        arrayItem.orario_partenza = new Date(arrayItem.orario_partenza);
                    });
                }
            });
        }
        function deleted(id){
            
            RouteService.Delete(id).then(function (response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    FlashService.pop({title: "Hai eliminato il passaggio", body: "Ricordati di avvisare gli eventuali passeggeri", type: "info"});
                    vm.initialize();
                }
            });
        }
    }
})();
