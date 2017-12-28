(function () {
    'use strict';

    angular
        .module('app')
        .factory('CarService', CarService);

        CarService.$inject = ['$http', 'CONFIG'];
    function CarService($http, CONFIG) {
        var service = {};
        var config = {
            headers: { 'Content-Type': 'application/json;' }
        };
        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var controller = 'auto';
        var BASEURL = host + root + '/' + controller + '/';
        service.GetAuto = GetAuto;
        service.DeleteCar = DeleteCar;
        service.CreateCar = CreateCar;
        return service;

        function GetAuto(email) {
            return $http.get(BASEURL + '' + email).then(handleSuccess, handleError);
        }
        function CreateCar(car) {
            return $http.post(BASEURL + '', car, config).then(handleSuccess, handleError);
        }
        function DeleteCar(id) {
            return $http.delete(BASEURL + 'delete/' + id).then(handleSuccess, handleError);
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
