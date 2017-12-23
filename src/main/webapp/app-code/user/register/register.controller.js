(function() {
    'use strict';
    angular
        .module('app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$timeout','$scope','AccountService', '$location', '$store', 'FlashService'];

    function RegisterController($timeout,$scope,AccountService, $location, $store, FlashService) {
        var vm = this;
        $('body,html').animate({scrollTop:0},0);
        vm.utente = $store.get('utente');
        vm.data = new Date(1994, 0, 25);
        vm.register = register;
        vm.minDate = new Date(1900,0,1);
        vm.maxDate = new Date(2016,0,1);
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerRegister' ) );
            myEl.addClass('active');           
        }); 
        function register() {
            if ($scope.registerForm.$valid) {
                vm.user.anno_nascita = vm.data.getTime(); 
                AccountService.Create(vm.user).then(function(response) {
                    if(response.success===false){
                        switch(response.res.status){
                            case 500:{
                                $location.path('/error');
                                break;
                            }                    
                            case 409:{
                                $('body,html').animate({scrollTop:0},800);
                                FlashService.pop({title: "Email già registrata", body: "", type: "warning"});
                                break;                        
                            }
                        }
                    }
                    else{
                        $('body,html').animate({scrollTop:0},800);
                        FlashService.set({title: "Registrazione avvenuta!", body: "A breve riceverai un e-mail per confermare il tuo indirizzo", type: "success"});
                        $location.path('/');
                    }
                });
            }
        }
    }
})();