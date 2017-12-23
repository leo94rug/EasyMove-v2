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
                    $location.path('/error');
                }
                else{
                    FlashService.set({title: "Abbiamo reinviato una email di conferma", body: "", type: "info"});
                    $location.path('/modificapsw');
                }
            });
        }
}


  })();