(function () {
    'use strict';

    angular
        .module('app')
        .controller('AutobusCambioController', AutobusCambioController)
        .config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});

    AutobusCambioController.$inject = ['NotificationsService', 'RouteService','$location','UserService', '$rootScope', 'FlashService','$routeParams','$mdDialog','AuthenticationService'];
    function AutobusCambioController(NotificationsService,RouteService, $location,UserService, $rootScope, FlashService,$routeParams,$mdDialog,AuthenticationService) {
        var vm = this;
        vm.loggedIn = $store.get('user');
                if($rootScope.travel===undefined){
                        $location.path('/search');
        }else{
        vm.route=$rootScope.travel.currentTravel.cambioBus;
}
        vm.logout=logout;
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }

    }

})();
