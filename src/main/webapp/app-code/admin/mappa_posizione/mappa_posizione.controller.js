(function () {
    'use strict';

    angular
        .module('app')
        .controller('MappaPosizioneController', MappaPosizioneController);

    MappaPosizioneController.$inject = ['$scope','UserService', '$location', '$rootScope','$routeParams', 'GestioneService','AuthenticationService'];
    function MappaPosizioneController($scope,UserService, $location, $rootScope,$routeParams, GestioneService,AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;
        vm.coordinate=coordinate;
        vm.modifica=modifica;
        vm.logout=logout;
        vm.addMarker=addMarker;
        vm.labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        vm.labelIndex = 0;
            $scope.$watch("vm.lat",function(newValue,oldValue) {
            });        
            vm.initialize();
            function coordinate(){
                vm.obj.lat=vm.lat;
                vm.obj.lng=vm.lng;
            }
            function modifica(){
                GestioneService.ModifyPosition(vm.obj).then(function(response){
                    if(response.success===false){}
                    else{
                                      
                    }        
                });
            }
        function initialize() {
            GestioneService.GetAutobusPosition($routeParams.id).then(function (response) {
                if(response.success===false){}
                else{
                    vm.obj=response;
                    var location = { lat: vm.obj.lat, lng: vm.obj.lng };
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 12,
                center: location
            });
            vm.addMarker(location, map);
            // This event listener calls addMarker() when the map is clicked.
            google.maps.event.addListener(map, 'click', function(event) {

                        vm.lat=event.latLng.lat();
                vm.lng=event.latLng.lng();
                vm.addMarker(event.latLng, map);

            });
                }
            });


        // Add a marker at the center of the map.
        }
              function addMarker(location, map) {
        // Add the marker at the clicked location, and add the next-available label
        // from the array of alphabetical characters.
        var marker = new google.maps.Marker({
            position: location,
            label: vm.labels[vm.labelIndex++ % vm.labels.length],
            map: map
        });

      }
        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        

        Date.createFromMysql = function(mysql_string){ 
          var t, result = null;
          if( typeof mysql_string === 'string' )   {
            t = mysql_string.split(/[- :]/);
            result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);          
          }
          return result;   
        }
    }

})();
