(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', 'CONFIG'];
    function UserService($http, CONFIG) {
        var service = {};
        var config = {
            headers: { 'Content-Type': 'application/json;' }
        };
        //        var config2={
        //            headers : {'Content-Type': 'multipart/form-data;'}
        //        };
        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var controller = 'user';
        var BASEURL = host + root + '/' + controller + '/';
        service.GetByEmail = GetByEmail;
        service.GetProfiloUtenti = GetProfiloUtenti;
        service.GetAuto = GetAuto;
        service.Update = Update;
        service.UpdateImage = UpdateImage;
        service.DeleteCar = DeleteCar;
        service.GetProfilo = GetProfilo;
        service.GetViaggi = GetViaggi;
        service.GetPercorsi = GetPercorsi;
        service.CreateCar = CreateCar;
        service.CheckFriend = CheckFriend;
        service.TravelNumber = TravelNumber;
        service.GetPrenotazioni = GetPrenotazioni;
        service.Prenotazione = Prenotazione;
        return service;

        function GetPrenotazioni(id) {
            return $http.get(BASEURL + 'getprenotazioni/' + id).then(handleSuccess, handleError);
        }
        function CheckFriend(user1, user2) {
            return $http.get(BASEURL + 'checkfriend/' + user1 + '/' + user2).then(handleSuccess, handleError);
        }
        function GetByEmail(email) {
            return $http.get(BASEURL + 'getbyemail/' + email).then(handleSuccess, handleError);
        }
        function GetProfilo(email) {
            return $http.get(BASEURL + 'getprofilo/' + email).then(handleSuccess, handleError);
        }
        function GetViaggi(email) {
            return $http.get(BASEURL + 'getviaggi/' + email).then(handleSuccess, handleError);
        }
        function GetPercorsi(email) {
            return $http.get(BASEURL + 'getpercorsi/' + email).then(handleSuccess, handleError);
        }
        function GetProfiloUtenti(email) {
            return $http.get(BASEURL + 'getprofiloutenti/' + email).then(handleSuccess, handleError);
        }
        function GetAuto(email) {
            return $http.get(BASEURL + 'getauto/' + email).then(handleSuccess, handleError);
        }
        function CreateCar(car) {
            return $http.post(BASEURL + 'addcar', car, config).then(handleSuccess, handleError);
        }
        function Prenotazione(prenotazione) {
            return $http.post(BASEURL + 'prenotazione', prenotazione, config).then(handleSuccess, handleError);
        }
        function Update(user, id) {
            return $http.put(BASEURL + 'updateprofilo/' + id, user, config).then(handleSuccess, handleError);
        }
        function UpdateImage(user) {
            return $http.put(BASEURL + 'updateimage', user, config).then(handleSuccess, handleError);
        }
        function DeleteCar(id) {
            return $http.delete(BASEURL + 'deletecar/' + id).then(handleSuccess, handleError);
        }
        function TravelNumber(email) {
            return $http.get(BASEURL + 'travelnumber/' + email).then(handleSuccess, handleError);
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
