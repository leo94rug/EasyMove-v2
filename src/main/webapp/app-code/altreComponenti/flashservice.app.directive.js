angular
    .module('app')
    .directive('flashService', flashService);

    flashService.$inject = ['UserService','$rootScope','$location'];
    
    function flashService(UserService, $rootScope,$location) {
        var url = 'app-code/altreComponenti/flashservice.app.view.html';        
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


