(function () {
    'use strict';

    angular
        .module('app')
        .factory('PosizioneService', PosizioneService);

    PosizioneService.$inject = ['$http', 'CONFIG'];
    function PosizioneService($http, CONFIG) {
        var service = {};
        var controller = "posizione";
        var config = {
            headers: { 'Content-Type': 'application/json;' }
        };

        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var BASEURL = host + root + '/' + controller + '/';
        service.GetPosizioni = GetPosizioni;
        return service;

        function GetPosizioni() {
            return $http.get(BASEURL + '', config).then(handleSuccess, handleError);
        }
        // private functions

        function handleSuccess(res) {
            console.log(res);

            return { success: true, res: res };
        }

        function handleError(res) {
            return { success: false, res: res };
        }
    }

})();
