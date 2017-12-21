(function () {
    'use strict';

    angular
        .module('app')
        .factory('MapsService', MapsService);

    MapsService.$inject = ['$http'];
    function MapsService($http) {
        var service = {};
        var config = {
            headers : {
                'Content-Type': 'application/json;',
                'Access-Control-Allow-Origin' : '*',
                'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT',
                'Accept': 'application/json'
            }
        };
       var distanceMatrixKey="AIzaSyAoKSYKoDQGyLfv4YEA-VTq6sjfuI5C0oQ";

        service.DistanceMatrixJs=DistanceMatrixJs;
        service.GeocodeFromCoord=GeocodeFromCoord;
        service.DistanceMatrix=DistanceMatrix;
                return service;

        function DistanceMatrixJs(loc1,loc2){
                var service = new google.maps.DistanceMatrixService();
                return service.getDistanceMatrix({
                        origins: [loc1],
                        destinations: [loc2],
                        travelMode: 'DRIVING',
                        }, callback);

        }
        function GeocodeFromCoord(lat,lng){
        	return $http.get('https:maps.googleapis.com/maps/api/geocode/json?latlng=' + lat +',' + lng + '&key=AIzaSyAMnaHa1NjnGpnuM0jCvmBMZP9-C4_kzR8').then(handleSuccess, handleError('Error getting user by id'));
        }
        function DistanceMatrix(loc1,loc2){
        	return $http.get('https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins='+loc1+'&destinations='+loc2+'&key='+distanceMatrixKey,config).then(handleSuccess, handleError('Error getting user by id'));
        }
        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
        function callback(response, status) {
                
                if(status=='OK'){
                        return response;
                }
                else{
                        return 'mapsError';
                }
                // See Parsing the Results for
                // the basics of a callback function.
        }
    }

})();




