(function () {
    'use strict';

    angular
        .module('app')
        .factory('AccountService', AccountService);

    AccountService.$inject = ['$http', 'CONFIG'];
    function AccountService($http, CONFIG) {
        var service = {};
        var controller = "account";
        var config = {
            headers: { 'Content-Type': 'application/json;'
        }};
        //        var config2={
        //            headers : {'Content-Type': 'multipart/form-data;'}
        //        };
        var host = CONFIG.HOST;
        var root = CONFIG.ROOT;
        var BASEURL = host + root + '/' + controller + '/';

        service.Create = Create;
        service.ConfermaEmail = ConfermaEmail;
        service.RecuperaPsw = RecuperaPsw;
        service.Login = Login;
        service.ModificaPsw = ModificaPsw;
        service.Check = Check;
        service.ResendEmail = ResendEmail;
        return service;

        function ResendEmail(utente) {
            return $http.post(BASEURL + 'resendemail', utente, config).then(handleSuccess, handleError);
        }
        function ModificaPsw(utente) {
            return $http.post(BASEURL + 'modificapsw', utente, config).then(handleSuccess, handleError);
        }
        function Login(user) {
            return $http.post(BASEURL + 'login', user, config).then(handleSuccess, handleError);
        }
        function RecuperaPsw(email) {
            return $http.post(BASEURL + 'recuperapsw', email, config).then(handleSuccess, handleError);
        }
        function Create(user) {
            return $http.post(BASEURL + 'register', user, config).then(handleSuccess, handleError);
        }
        function ConfermaEmail(user) {
            return $http.put(BASEURL + 'confermaemail', user, config).then(handleSuccess, handleError);
        }
        function Check(user1, user2) {
            return $http.get(BASEURL + 'check/' + user1 + '/' + user2).then(handleSuccess, handleError);
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
