angular
    .module('app')
    .directive('sideNav', sideNav);

    sideNav.$inject = ['UserService','$rootScope','$location','$mdSidenav'];
    
    function sideNav(UserService, $rootScope,$location,$mdSidenav) {
   /*     var url = 'app-code/altreComponenti/sidenav.app.view.html';
        function buildToggler(componentId) {
        return function() {
            $mdSidenav(componentId).toggle();
        };
        }
    function debounce(func, wait, context) {
      var timer;

      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }
    function buildDelayedToggler(navID) {
      return debounce(function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }, 200);
    }
    function buildToggler(navID) {
      return function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      };
    }
  })
  .controller('LeftCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      // Component lookup should always be available since we are not using `ng-if`
      $mdSidenav('left').close()
        .then(function () {
          $log.debug("close LEFT is done");
        });

    };
  })
  .controller('RightCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      // Component lookup should always be available since we are not using `ng-if`
      $mdSidenav('right').close()
        .then(function () {
          $log.debug("close RIGHT is done");
        });
    };
  });
        function link($scope, element, attrs) {
            $scope.toggleLeft = buildDelayedToggler('left');
            $scope.toggleRight = buildToggler('right');
            $scope.isOpenRight = function(){
                return $mdSidenav('right').isOpen();
            };
        }
	    var directive = {
           	link: link,
	        templateUrl: url,
    	    restrict: 'E',
        	replace: true,
            scope: true,
            bindToController: true // because the scope is isolated
    	};	
        return directive; */  
    }

