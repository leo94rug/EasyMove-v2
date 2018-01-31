(function() {
    'use strict';

    angular
        .module('app')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['DateService','$timeout','$store','RouteService', '$location', '$rootScope', 'FlashService','AuthenticationService'];

    function SearchController(DateService,$timeout,$store,RouteService, $location, $rootScope, FlashService,AuthenticationService) {
        var vm = this;

        vm.pagina=1;
        vm.screenType=screenType;
        vm.route=new Object();
        var date=new Date();
        vm.localizzato=0;
        vm.submit = submit;
        vm.geolocate = geolocate;
        vm.currentNavItem = 'page1';
        vm.cambio=cambio;
        vm.visualizza=visualizza;
        vm.logout=logout;
        vm.initialize=initialize;
        vm.dettaglioAutobus=dettaglioAutobus;
        vm.dettaglioIbrido=dettaglioIbrido;
        vm.auto=1;
        vm.autobus=0;
        vm.busbus=1;
        vm.busauto=0;
        this.save=1;
        vm.search = {
            cambio: 0,
            distanza: 1,
            date:date
        };
        vm.cambioData = [{label: '0',value: 0},{label: '1(non supportato)',value: 1}];
        vm.distanzaData = [{label: 'Piccola',value: 0},{label: 'Media',value: 1},{label: 'Grande',value: 2}];
        vm.initialize();      
        function screenType(){
            var screen=window.screen.width;
            if (screen<=980) {
                return true;
            }
            else{
                return false;
            }
        }
        function dettaglioAutobus(route){
            $rootScope.travel = {
                currentTravel: {
                    cambioBus: route
                }

            };               
            $location.path('/showautobuscambio');
        }        
        function dettaglioIbrido(route){
            $rootScope.travel = {
                currentTravel: {
                    cambioBus: route
                }

            };               
            $location.path('/showautobusibrido');
        }
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function initialize(){
            $('body,html').animate({scrollTop:0},800);
            vm.utente = $store.get('utente');
            $timeout(function(){
                var myEl = angular.element( document.querySelector( '#headerSearch' ) );
                myEl.addClass('active');           
            });  
            if ($rootScope.search!=undefined) {
                vm.search={
                    cambio: $rootScope.search.cambio,
                    distanza: $rootScope.search.distanza,
                    date:$rootScope.search.date,
                    partenza:$rootScope.search.partenza,
                    arrivo:$rootScope.search.arrivo
                }
                vm.submit();
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
                if (response.success === false) {
                    $location.path('/error');
                } 
                else {
                    if (response.rows[0].elements[0].status==='ZERO_RESULTS') {
            FlashService.pop({title: "Non sono stati ottenuti risultati", body: "", type: "info"});
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
                            date:DateService.stringFromDate($rootScope.search.date),
                            cambio:$rootScope.search.cambio,
                            distanza:$rootScope.search.distanza,
                            utente_fk:null,
                            distanza_tra:distance
                        };
                        RouteService.Cerca(info).then(function(response) {
                            if(response.success===false){
                                $location.path('/error');
                            }
                            else{
                                vm.routeAuto=response.res.data;
                                vm.routeAuto.forEach( function (arrayItem){
                                    arrayItem.tratta_auto.orario_partenza = DateService.dateFromString(arrayItem.tratta_auto.orario_partenza);
                                });
                            }
                        });
                    }
                }                        
            });

        }
        function visualizza(tratta1,tratta2){
            $location.path('/prenotation/' + tratta1 + '/' + tratta2);

        }
        function cambio(page){
            vm.pagina=page;
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

        //navigator.geolocation.getCurrentPosition(success, error, options);
        function success(pos) {

            var crd = pos.coords;

            console.log('Your current position is:');
            console.log('Latitude : ${crd.latitude}');
            console.log('Longitude: ${crd.longitude}');
            console.log('More or less ${crd.accuracy} meters.');
        };

        function error(err) {

            console.warn('ERROR(${err.code}): ${err.message}');
        };
        var options = {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        };

    }
})();
