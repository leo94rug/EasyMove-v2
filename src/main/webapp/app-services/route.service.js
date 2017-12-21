(function () {
    'use strict';

    angular
        .module('app')
        .factory('RouteService', RouteService);

    RouteService.$inject = ['$http','CONFIG'];
    function RouteService($http,CONFIG) {
        var service = {};
        var host=CONFIG.HOST;
        var config = {
            headers : {
                'Content-Type': 'application/json;',
                'Access-Control-Allow-Origin' : 'http://localhost:8084'
            }
        };
        service.GetPercorso=GetPercorso;
        service.GetBusTravel=GetBusTravel;
        service.Search = Search;
        service.Create = Create;
        service.Delete = Delete;
        service.Cerca = Cerca;
        service.GetDettaglioPercorso=GetDettaglioPercorso;
        service.GetViaggio=GetViaggio;
        return service;
        function Cerca(obj){
            return $http.post(host + 'rest/route/cercaauto', obj, config).then(handleSuccess, handleError);
        }
        function GetPercorso(t1) {//non usata
            return $http.get(host + 'rest/route/percorso/' + t1).then(handleSuccess, handleError);
        }        
        function GetViaggio(id) {
            return $http.get(host + 'rest/route/viaggio/' + id).then(handleSuccess, handleError);
        }
   
        function GetDettaglioPercorso(t1,t2) {
            return $http.get(host + 'rest/route/dettagliopercorso/' + t1 + '/' + t2).then(handleSuccess, handleError);
        }
        function Search(lap,lop,laa,loa,dis,cam,g,m,a,lup,lua,type) {
            return $http.get(host + 'rest/route/standard/' + lap + '/' + lop + '/' + laa + '/' + loa + '/' + dis + '/' + cam + '/' + g + '/' + m + '/' + a + '/' + lup + '/' + lua + '/' + type).then(handleSuccess, handleError);
        }
        function Create(route) {
            return $http.post(host + 'rest/route/insert', route, config).then(handleSuccess, handleError);
        }        
        function Delete(id) {
            
            return $http.delete(host + 'rest/route/delete/' + id).then(handleSuccess, handleError);
        }        
                function GetBusTravel(t1,t2) {
            return $http.get(host + 'rest/route/singolobus/' + t1 + '/' + t2).then(handleSuccess, handleError);
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
