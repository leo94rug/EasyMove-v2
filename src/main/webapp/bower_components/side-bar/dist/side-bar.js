(function() {
  'use strict';

  angular.module('sideBarAngular', [
    'sideBar.templates', 'ngMaterial'
  ]);
}());

(function() {
  'use strict';

  function SideBarController($attrs, $timeout,$scope, $mdSidenav, $log,$store,AuthenticationService,$location,$mdComponentRegistry) {
    $scope.utente = $store.get('utente');
    $scope.toggleLeft = buildToggler('left');
    $scope.h=window.screen.height;
    $scope.logout=logout;
    $scope.$watch(function(){
      return $mdComponentRegistry.get('left') ? $mdSidenav('left').isOpen() : false;
    }, 
      function(newVal){
        if (newVal==true) {
          var myEl = angular.element( document.querySelector( 'body' ) );
          myEl.addClass('scrollBloccato');
        }
        else{
          $("body").removeClass("scrollBloccato");
        }
      }
    );
    function isOpen(componentId){
      return $mdSidenav(componentId).isOpen();
    }
    function logout(){
      AuthenticationService.ClearCredentials();
      $scope.utente=$store.get('user');
      $mdSidenav('left').toggle();
      $location.path('/');
    }
    function buildToggler(componentId) {

      return function() {
        $mdSidenav(componentId).toggle();
      };
    }
  }
  angular
    .module('sideBarAngular')
    .controller('SideBarController', [
      '$attrs', '$timeout','$scope', '$mdSidenav', '$log','$store','AuthenticationService','$location','$mdComponentRegistry',
      SideBarController
    ]);

}());

(function() {

  'use strict';

  function SideBarDirective() {

    function link(scope, element, attrs, ctrl) {

          }

    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'side-bar-directive.html',
      scope: {},
      controller: 'SideBarController',
      controllerAs: 'ctrl',
      bindToController: {
        maxRating: '@?',
        rating: '=?',
        readOnly: '=?',
        onRating: '&'
      },
      link: link
    };
  }

  angular
    .module('sideBarAngular')
    .directive('sideBar', [
    SideBarDirective
  ]);

}());

(function(){angular.module("sideBar.templates", []).run(["$templateCache", function($templateCache) {$templateCache.put("side-bar-directive.html",
  '<div class="delete">'+
    '<md-button class="md-icon-button md-primary visib " ng-click="toggleLeft()">'+
        '<md-icon md-svg-src="images/menu.svg" style="height: 50px;width: 50px;color:black;"></md-icon>'+
    '</md-button>'+
    '<section layout="row" flex class="superZ">'+
        '<md-sidenav class="md-sidenav-left scrollBloccato" md-component-id="left" md-whiteframe="4">'+
            '<md-content layout-padding style="margin-top: 80px;">'+
            '<p ng-if="utente" class="active"><a href="home">Home</a></p>'+
            '<p ng-if="utente"><a href="search"><b>Cerca</b></a></p>'+
            '<p ng-if="utente"><a href="offer1"><b>Offri</b></a></p>'+
            '<p ng-if="!utente" class="active"><a href="home">Home</a></p>'+
            '<p ng-if="!utente"><a href="login" title="Destinations">Login</a></p>'+
            '<p ng-if="utente"><a href="bacheca">Bacheca</a></p>'+            
            '<p ng-if="utente"><a href="profilo">Profilo</a></p>'+
            '<p ng-if="utente"><a href="feedback-ricevuti">Feedback ricevuti</a></p>'+
            '<p ng-if="utente"><a href="passaggi">Passaggi offerti</a></p>'+
            '<p ng-if="utente"><a href="prenotazioni">Prenotazioni</a></p>'+
            
            '<p ng-if="!utente"><a href="register">Registrazione</a></p>'+
            '<p ng-if="utente"><a ng-click="logout()">Logout</a></p>'+
            '</md-content>'+
        '</md-sidenav>'+
    '</section>'+
'</div>');}]);})();