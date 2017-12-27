(function () {
    'use strict';

    angular
        .module('app')
        .factory('PrenotationService', PrenotationService);

        PrenotationService.$inject = ['$http', 'CONFIG'];
    function PrenotationService($http, CONFIG) {
        var service = {};
        var config = {
            headers: { 'Content-Type': 'application/json;' }
        };
        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var controller = 'prenotation';
        var BASEURL = host + root + '/' + controller + '/';

        service.GetPrenotazioni = GetPrenotazioni;
        service.Prenotazione = Prenotazione;
        return service;

        function GetPrenotazioni(id) {
            return $http.get(BASEURL + 'getprenotazioni/' + id).then(handleSuccess, handleError);
        }
        function Prenotazione(prenotazione) {
            return $http.post(BASEURL + 'prenotazione', prenotazione, config).then(handleSuccess, handleError);
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
