(function () {
    'use strict';

    angular
        .module('app')
        .controller('FeedbackRicevutiController', FeedbackRicevutiController)
        .config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});

    FeedbackRicevutiController.$inject = ['$timeout','UserService', '$location', '$store', 'FlashService','AuthenticationService'];
    function FeedbackRicevutiController($timeout,UserService, $location, $store, FlashService,AuthenticationService) {
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.initialize=initialize;
        vm.logout=logout;
        initialize();  
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerBacheca' ) );
            myEl.addClass('active');           
        });       
        Date.createFromMysql = function(mysql_string){ 
          var t, result = null;
          if( typeof mysql_string === 'string' )   {
            t = mysql_string.split(/[- :]/);
            result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);          
          }
          return result;   
        }
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.pop({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }

        function initialize(){
            vm.utente = $store.get('utente');
            UserService.GetFeedback(vm.utente.id).then(function (response) {
                if(response.success===false){                    
                    $location.path('/error');
                }
                    else{
                    vm.feedback=response.res.data;
                    vm.feedback.listaFeedback.forEach( function (arrayItem){
                        debugger;
                        arrayItem.date = Date.createFromMysql(arrayItem.dateString);
                    });

                }
            });
        }
    }

})();
