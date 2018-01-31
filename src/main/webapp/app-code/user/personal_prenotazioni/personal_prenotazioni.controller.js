(function () {
    'use strict';

    angular
        .module('app')
        .controller('PersonalPrenotazioniController', PersonalPrenotazioniController);

    PersonalPrenotazioniController.$inject = ['$timeout','PrenotationService', '$location', '$store', 'FlashService','AuthenticationService'];
    function PersonalPrenotazioniController($timeout,PrenotationService, $location, $store, FlashService,AuthenticationService) {
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.initialize=initialize;
        initialize();
        vm.logout=logout;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerBacheca' ) );
            myEl.addClass('active');           
        });      

        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.pop({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }

        function initialize(){
            vm.utente = $store.get('utente');
            PrenotationService.GetPrenotazioni(vm.utente.id).then(function (response) {
                if(response.success===false){                    
                    $location.path('/error');
                }
                    else{
                    vm.prenotazioni=response.res.data;
                    vm.prenotazioni.forEach( function (arrayItem){
                        arrayItem.tratta_auto.orario_partenza = new Date(arrayItem.tratta_auto.orario_partenza);
                    });
                }
            });
        }
    }

})();
