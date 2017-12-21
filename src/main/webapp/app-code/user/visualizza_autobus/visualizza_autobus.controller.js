(function () {
    'use strict';

    angular
        .module('app')
        .controller('AutobusController', AutobusController)
        .config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});

    AutobusController.$inject = ['$store','NotificationsService', 'RouteService','$location','UserService', '$rootScope', 'FlashService','$routeParams','$mdDialog','AuthenticationService'];
    function AutobusController($store,NotificationsService,RouteService, $location,UserService, $rootScope, FlashService,$routeParams,$mdDialog,AuthenticationService) {
        var vm = this;
        vm.loggedIn = $store.get('user');
        vm.tratta1=$routeParams.tratta1;
        vm.tratta2=$routeParams.tratta2;
        vm.initializa=initialize;
        vm.visualizzautente=visualizzautente;
        vm.logout=logout;
        initialize();
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }

        function initialize(){
            RouteService.GetTravel(vm.tratta1,vm.tratta2,vm.tratta3,vm.tratta4)
                .then(function(response) {
                vm.disabled=false;
                vm.messaggio="";
                vm.travel=response;
                UserService.CheckFriend(vm.utente.email,vm.loggedIn.user).then(function(response){
                    vm.amicizia=response;
                });
            });
        }

    }

})();
