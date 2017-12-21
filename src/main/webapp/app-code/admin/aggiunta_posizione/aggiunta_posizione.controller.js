(function () {
    'use strict';

    angular
        .module('app')
        .controller('AddPosizioneController', AddPosizioneController);

    AddPosizioneController.$inject = ['UserService', '$location', '$rootScope', 'GestioneService','AuthenticationService'];
    function AddPosizioneController(UserService, $location, $rootScope, GestioneService,AuthenticationService) {
        var vm = this;
        vm.logout=logout;
        vm.geolocate=geolocate;
        vm.localizzato = 0; 
        vm.logout = logout;
        vm.message="";
        vm.sub=sub;
        function sub(){
            GestioneService.CreatePosition(vm.position).then(function (response) {
                if(response.success===false){
                    
                }
                else{
                    vm.response=response;
                    vm.message='inserito';
                }
            }); 
        }
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        function geolocate() {
            if (navigator.geolocation) {
                if (vm.localizzato === 0) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        vm.localizzato = 1;
                        var geolocation = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };
                        var circle = new google.maps.Circle({
                            center: geolocation,
                            radius: position.coords.accuracy
                        });
                        vm.autocompleteOptions = {
                            bounds: circle.getBounds(),
                            types: ['establishment','geocode'],
                            componentRestrictions: {country: 'it'},
                        }
                    });
                }
            }
        }

    }

})();
