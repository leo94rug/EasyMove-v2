(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$timeout','NotificationsService','FlashService','RouteService','UserService','AuthenticationService' ,'$rootScope','$location','$compile','$scope','$store'];
    function HomeController($timeout,NotificationsService,FlashService,RouteService,UserService,AuthenticationService, $rootScope,$location,$compile,$scope,$store) {
        var vm = this;
        vm.utente = $store.get('utente');
        $('body,html').animate({scrollTop:0},800);
        vm.initialize = initialize;
        vm.logout=logout;
        vm.number=0;
        vm.geolocate=geolocate;
        vm.submit=submit;
        var date=new Date();
        vm.localizzato=0; 
        vm.search = {
            date:date,
            distanza:1,
            cambio:0
        };
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerHome' ) );
            myEl.addClass('active');           
        });      
        initialize();
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/login');
        }        
        Date.createFromMysql = function(mysql_string){ 
          var t, result = null;
          if( typeof mysql_string === 'string' )   {
            t = mysql_string.split(/[- :]/);
            result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);          
          }
          return result;   
        }
        function geolocate() {
            if (navigator.geolocation) {
                if (vm.localizzato===0) {
                    navigator.geolocation.getCurrentPosition(function(position) {
                        vm.localizzato=1;
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
                            types: ['geocode'],
                            componentRestrictions: { country: 'it' },
                        }
                });
                }
            }
        }  
        function submit() {
            $rootScope.search = vm.search;
            var info;
            var formatted_address1 = $rootScope.search.partenza.formatted_address;
            var formatted_address2 = $rootScope.search.arrivo.formatted_address;
            var service = new google.maps.DistanceMatrixService();
            service.getDistanceMatrix({
                origins: [formatted_address1],
                destinations: [formatted_address2],
                travelMode: 'DRIVING',
            }, 
            function(response, status){
            //MapsService.DistanceMatrix(formatted_address1,formatted_address2).then(function(response){
                if (response.success === false) {
                    $location.path('/error');
                } 
                else {
                    if (response.rows[0].elements[0].status==='ZERO_RESULTS') {
                        var text = 'Posizione non valida';
                    }
                    else{
                        var distance=response.rows[0].elements[0].distance.value;
                        var tipo;
                        if (distance<30000) {
                            tipo=0;
                        }
                        else{
                            tipo=1;
                        }
                        info={
                            latp:$rootScope.search.partenza.geometry.location.lat(),
                            lata:$rootScope.search.arrivo.geometry.location.lat(),                
                            lngp:$rootScope.search.partenza.geometry.location.lng(),
                            lnga:$rootScope.search.arrivo.geometry.location.lng(),
                            tipo:tipo,
                            date:$rootScope.search.date.getTime(),
                            cambio:$rootScope.search.cambio,
                            distanza:$rootScope.search.distanza,
                            utente_fk:vm.utente.id,
                            distanza_tra:distance
                        };
                        RouteService.Cerca(info).then(function(response) {
                            if(response.success===false){
                                $location.path('/error');
                            }
                            else{
                                vm.routeAuto=response;
                                vm.routeAuto.forEach( function (arrayItem){
                                    arrayItem.tratta_auto.orario_partenza = Date.createFromMysql(arrayItem.tratta_auto.orario_partenza_string);
                                });
                            }
                        });
                    }
                }                        
            });

            $location.path("/search")
        }
        function initialize() {
            if (vm.utente) {
                NotificationsService.NotificationNumber(vm.utente.id).then(function (response) {
                    if(response.res.success===false){
                        $location.path('/error');
                    }
                    else{
                        vm.number=response.res.data;
                    }
                });
            }
        }
    }

})();