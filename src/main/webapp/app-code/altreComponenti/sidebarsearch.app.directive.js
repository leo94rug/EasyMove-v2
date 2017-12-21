angular
    .module('app')
    .directive('sideBarSearch', sideBarSearch);

    sideBarSearch.$inject = ['UserService','$rootScope','$location'];
    
    function sideBarSearch(UserService, $rootScope,$location) {
            var url = 'app-code/altreComponenti/sidebarsearch.app.view.html';
            
		    var directive = {
        	link: link,
	        templateUrl: url,
    	    restrict: 'E',
        	replace: true,
            scope: true,
            bindToController: true 
    	    };	
            return directive;   
	    }
    


