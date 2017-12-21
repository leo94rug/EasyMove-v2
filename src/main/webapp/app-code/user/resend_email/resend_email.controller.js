(function () {
    'use strict';
angular
  .module('app')
  .controller('ResendEmailController', ResendEmailController);

    ResendEmailController.$inject = ['$timeout','UserService','$location','FlashService'];

    function ResendEmailController($timeout,UserService,$location,FlashService){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.resendEmail=ResendEmail;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerLogin' ) );
            myEl.addClass('active');           
        }); 
        function ResendEmail() {
            if(vm.utente!=undefined){
                UserService.Login(vm.utente).then(function (response) {
                    if(response.success===false){
                        $location.path('/error');
                    }
                    else{
                        switch(response){
                            case 0:{
                                $('body,html').animate({scrollTop:0},800);
                                FlashService.pop({title: "Password o email errati", body: "", type: "warning"});
                                break;
                            }
                            case 1:{
                                $('body,html').animate({scrollTop:0},800);
                                FlashService.pop({title: "La tua email è già stata confermata!", body: "", type: "warning"});
                                break;
                            }                        
                            case 2:{
                                $('body,html').animate({scrollTop:0},800);
                                FlashService.pop({title: "Sei l'admin!", body: "", type: "warning"});
                                break;
                            }
                            case 3:{
                                $('body,html').animate({scrollTop:0},800);
                                vm.notconfirmed=true;
                                UserService.ResendEmail(vm.utente.email).then(function (response) {
                                    if(response.success===false){
                                        $location.path('/error');
                                    }
                                    else{
                                        FlashService.set({title: "Ti abbiamo reinviato un email di conferma", body: "", type: "info"});
                                        $location.path('/'); 
                                    }
                                });                                
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
})();