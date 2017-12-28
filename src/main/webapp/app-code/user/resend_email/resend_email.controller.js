(function () {
    'use strict';
angular
  .module('app')
  .controller('ResendEmailController', ResendEmailController);

    ResendEmailController.$inject = ['$timeout','AccountService','$location','FlashService'];

    function ResendEmailController($timeout,AccountService,$location,FlashService){
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.resendEmail=ResendEmail;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerLogin' ) );
            myEl.addClass('active');           
        }); 
        function ResendEmail() {
            if(vm.utente!=undefined){
                $('body,html').animate({scrollTop:0},800);
                vm.notconfirmed=true;
                AccountService.ResendEmail(vm.utente.email).then(function (response) {
                    if(response.success===false){
                        switch (response.res.status) {
                            case 500:
                            {
                                $location.path('/error');
                                break;
                            }
                            case 498:
                            {
                                $('body,html').animate({scrollTop: 0}, 800);
                                FlashService.pop({title: "Attenzione!", body: "Email già confermata", type: "warning"});
                                break;
                            }
                            case 404:
                            {
                                $('body,html').animate({scrollTop: 0}, 800);
                                FlashService.pop({title: "Attenzione!", body: "Email errata", type: "warning"});
                                break;
                            }
                            default:
                            {
                                $location.path('/error');
                                break;
                            }  
                        }
                    }
                    else{
                        FlashService.set({title: "Ti abbiamo reinviato un email di conferma", body: "", type: "info"});
                        $location.path('/'); 
                    }
                });                                
            }
        }
    }
})();