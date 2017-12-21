(function () {
    'use strict';

    angular
        .module('app')
        .controller('AutobusIbridoController', AutobusIbridoController)
        .config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});

    AutobusIbridoController.$inject = ['NotificationsService', 'RouteService','$location','UserService', '$rootScope', 'FlashService','$routeParams','$mdDialog','AuthenticationService'];
    function AutobusIbridoController(NotificationsService,RouteService, $location,UserService, $rootScope, FlashService,$routeParams,$mdDialog,AuthenticationService) {
        var vm = this;
        vm.visualizza=visualizza;
        vm.logout=logout;
        vm.loggedIn = $store.get('user');
        if($rootScope.travel===undefined){
            $location.path('/search');
        }
        else{
            vm.route=$rootScope.travel.currentTravel.cambioBus;
        }
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        function visualizza(tratta1,tratta2){
            $location.path('/prenotation/' + tratta1 + '/' + tratta2);

        }
    }

})();
