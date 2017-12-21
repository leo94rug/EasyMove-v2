angular
    .module('app')
    .directive('feedBack', feedBack);

    feedBack.$inject = ['UserService','$rootScope','$location'];
    
    function feedBack(UserService, $rootScope,$location) {
        var url = 'app-code/altreComponenti/feedback.app.view.html';        
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


