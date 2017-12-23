(function () {
    'use strict';

    angular
        .module('app')
        .factory('AccountService', AccountService);

    AccountService.$inject = ['$http','CONFIG'];
    function AccountService($http,CONFIG) {
        var service = {};
        var controller="account";
        var config = {
            headers : {'Content-Type': 'application/json;'}
        };
//        var config2={
//            headers : {'Content-Type': 'multipart/form-data;'}
//        };
        var host=CONFIG.HOST;

        service.Create = Create;
        service.ConfermaEmail=ConfermaEmail;
        service.RecuperaPsw=RecuperaPsw;
        service.Login=Login;
        service.ModificaPsw=ModificaPsw;
        service.ResendEmail=ResendEmail;
        return service;

        function ResendEmail(utente){
            return $http.post(host + 'rest/'+controller+'/resendemail', utente, config).then(handleSuccess, handleError);
        }        
        function ModificaPsw(utente){
            return $http.post(host + 'rest/'+controller+'/modificapsw', utente, config).then(handleSuccess, handleError);
        }
        function Login(user){
            return $http.post(host + 'rest/'+controller+'/login', user, config).then(handleSuccess, handleError);
        }
        function RecuperaPsw(email){
            return $http.post(host + 'rest/'+controller+'/recuperapsw', email, config).then(handleSuccess, handleError);
        }          
        function Create(user) {
            return $http.post(host + 'rest/'+controller+'/register', user, config).then(handleSuccess, handleError);
        }   
        function ConfermaEmail(user) {
            return $http.put(host + 'rest/'+controller+'/confermaemail', user,config).then(handleSuccess, handleError);
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
