(function () {
    'use strict';
angular
  .module('app')
  .controller('RecuperaPswController', RecuperaPswController);

    RecuperaPswController.$inject = ['$timeout','AccountService','AuthenticationService', '$scope','$routeParams','$store','$location','FlashService'];

    function RecuperaPswController($timeout,AccountService,AuthenticationService,$scope,$routeParams,$store,$location,FlashService){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.recuperaPsw=recuperaPsw;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerLogin' ) );
            myEl.addClass('active');           
        }); 
        function recuperaPsw() {
            AccountService.RecuperaPsw(vm.email).then(function (response) {
                if(response.success===false){
                    debugger;
                    switch(response.res.status){
                        case 500:{
                            $location.path('/error');
                            break;
                        }                      
                        case 404:{
                            $('body,html').animate({scrollTop:0},800);
                            FlashService.pop({title: "Attenzione!", body: "Email non trovata", type: "warning"});
                            break;                        
                        }
                        default:{
                            $location.path('/error');
                            break;                             
                        }
                    }                
                }
                else{
                    FlashService.set({title: "Abbiamo reinviato una email di conferma", body: "", type: "info"});
                    $location.path('/modificapsw');
                }
            });
        }
}


  })();