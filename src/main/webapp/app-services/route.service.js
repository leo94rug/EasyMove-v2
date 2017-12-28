(function () {
    'use strict';

    angular
        .module('app')
        .factory('RouteService', RouteService);

    RouteService.$inject = ['$http', 'CONFIG'];
    function RouteService($http, CONFIG) {
        var service = {};
        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var controller = 'route';
        var BASEURL = host +'/'+ root + '/' + controller + '/';
        var config = {
            headers: {
                'Content-Type': 'application/json;',
                'Access-Control-Allow-Origin': 'http://localhost:8084'
            }
        };
        service.GetPercorso = GetPercorso;
        service.GetBusTravel = GetBusTravel;
        service.Search = Search;
        service.Create = Create;
        service.Delete = Delete;
        service.Cerca = Cerca;
        service.GetDettaglioPercorso = GetDettaglioPercorso;
        service.GetViaggio = GetViaggio;
        return service;
        function Cerca(obj) {
            return $http.post(BASEURL + 'cercaauto', obj, config).then(handleSuccess, handleError);
        }
        function GetPercorso(t1) {//non usata
            return $http.get(BASEURL + 'percorso/' + t1).then(handleSuccess, handleError);
        }
        function GetViaggio(id) {
            return $http.get(BASEURL + 'viaggio/' + id).then(handleSuccess, handleError);
        }
        function GetDettaglioPercorso(t1, t2) {
            return $http.get(BASEURL + 'dettagliopercorso/' + t1 + '/' + t2).then(handleSuccess, handleError);
        }
        function Search(lap, lop, laa, loa, dis, cam, g, m, a, lup, lua, type) {
            return $http.get(BASEURL + 'standard/' + lap + '/' + lop + '/' + laa + '/' + loa + '/' + dis + '/' + cam + '/' + g + '/' + m + '/' + a + '/' + lup + '/' + lua + '/' + type).then(handleSuccess, handleError);
        }
        function Create(route) {
            return $http.post(BASEURL + 'insert', route, config).then(handleSuccess, handleError);
        }
        function Delete(id) {
            return $http.delete(BASEURL + 'delete/' + id).then(handleSuccess, handleError);
        }
        function GetBusTravel(t1, t2) {
            return $http.get(BASEURL + 'singolobus/' + t1 + '/' + t2).then(handleSuccess, handleError);
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
