angular
.module('app')
.directive('notificationDesktop', notificationDesktop);

notificationDesktop.$inject = ['UserService','$rootScope','$location'];

function notificationDesktop(UserService, $rootScope,$location) {
        var url = 'app-code/altreComponenti/notification-desk.app.view.html';
        
    var directive = {
      link: link,
      templateUrl: url,
      restrict: 'E',
      replace: true,
        scope: true,
        bindToController: true // because the scope is isolated

        // note: This would be 'ExampleController' (the exported controller name, as string)
        // if referring to a defined controller in its separate file.
        //controllerAs: 'vm',
        //bindToController: true // because the scope is isolated
      };	
        return directive;   
  }


    function link(scope, element, attrs,$rootScope) {
        scope.$watch("$rootScope.globals.currentUser",function(newValue,oldValue) {

        });
    }