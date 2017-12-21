angular
    .module('app')
    .directive('navBar', navBar);

    navBar.$inject = ['UserService','$rootScope','$location'];
    
    function navBar(UserService, $rootScope,$location) {
            var url = 'app-code/altreComponenti/navbar.app.view.html';
		    var directive = {
        	link: link,
	        templateUrl: url,
    	    restrict: 'E',
        	replace: true,
            scope: true,
            bindToController: true, // because the scope is isolated

            // note: This would be 'ExampleController' (the exported controller name, as string)
            // if referring to a defined controller in its separate file.
            //bindToController: true // because the scope is isolated
    	    };	
            return directive;   
	    }


        function link(scope, element, attrs) {
                
                scope.$watch("vm.utente",function(newValue,oldValue) {
                        
                });

        }


