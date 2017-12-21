angular
    .module('app')
    .directive('sideBarPers', sideBarPers);

    sideBarPers.$inject = ['UserService','$rootScope','$location'];
    
    function sideBarPers(UserService, $rootScope,$location) {
            var url = 'app-code/altreComponenti/sidebarpers.app.view.html';
            
		    var directive = {
        	link: link,
	        templateUrl: url,
    	    restrict: 'E',
        	replace: true,
            scope: true,
            bindToController: true // because the scope is isolated
    	    };	
            return directive;   
	    }


        function link(scope, element, attrs,$rootScope) {
            scope.$watch("$rootScope.globals.currentUser",function(newValue,oldValue) {

            });
        }


