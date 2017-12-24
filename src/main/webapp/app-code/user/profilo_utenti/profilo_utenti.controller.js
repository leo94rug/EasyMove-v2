
(function () {
    'use strict';
angular
  .module('app')
  .controller('ProfiloController', ProfiloController);

        ProfiloController.$inject = ['$timeout','FeedbackService','UserService','FlashService','$location','$routeParams','$store','AuthenticationService'];

function ProfiloController($timeout,FeedbackService,UserService,FlashService,$location,$routeParams,$store,AuthenticationService){
    var vm = this;
    vm.utente = $store.get('utente');
    $('body,html').animate({scrollTop:0},800);
    vm.id=$routeParams.id;
    vm.logout=logout;
    vm.initialize = initialize;

    vm.initialize();
    function logout(){
        AuthenticationService.ClearCredentials();
        FlashService.set({title: "Logout effettuato", body: "", type: "info"});
        $location.path('/');
    }
    function initialize() {
        UserService.GetProfilo(vm.id).then(function(response) {  
            if(response.success===false){
                $location.path('/error');
            }
            else{
                vm.user=response.res.data;        
            }        
        });        
        UserService.TravelNumber(vm.id).then(function (response) {
            if(response.success===false){
                $location.path('/error');
            }
            else{
                vm.numbertravel=response.res.data;
            }
        });
        FeedbackService.GetFeedback(vm.id).then(function(response) {          
            if(response.success===false){
                $location.path('/error');
            }
            else{
                
                vm.feedback=response.res.data;  
            }    
        });
        UserService.CheckFriend(vm.id,vm.utente.id).then(function(response){
            vm.amicizia=response.res.data;
            debugger;
        })
       }; 
}
})();
