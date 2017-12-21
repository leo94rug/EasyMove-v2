(function () {
    'use strict';

    angular
        .module('app')
        .factory('GestioneService', GestioneService);

    GestioneService.$inject = ['$http','CONFIG'];
    function GestioneService($http,CONFIG) {
        var service = {};
        var host=CONFIG.HOST;
        var config = {
            headers : {'Content-Type': 'application/json;',
       }
        };
        service.GetAllAzienda=GetAllAzienda;
        service.GetAllStorico=GetAllStorico;
        service.GetAllAutobusPosition=GetAllAutobusPosition;
        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Add=Add;
        service.DeletePosition=DeletePosition;
        service.getStoricoAttivo=getStoricoAttivo;
        service.CreatePosition=CreatePosition;
        service.ModifyPosition=ModifyPosition;
        service.setStoricoAttivo=setStoricoAttivo;
        service.getStoricoAttivo=getStoricoAttivo;
        service.GetAutobusPosition=GetAutobusPosition;
        return service;
        //ritorna tutti gli autobus
        function getStoricoAttivo(id){
            return $http.get(host + 'rest/admin/getstoricoattivo/' + id).then(handleSuccess, handleError('Error getting all users'));
        }        
        function setStoricoAttivo(id){
            return $http.put(host + 'rest/admin/setstoricoattivo', id).then(handleSuccess, handleError('Error getting all users'));
        }
        function GetAllAutobusPosition(id){
            return $http.get(host + 'rest/admin/getallautobusposition/' + id).then(handleSuccess, handleError('Error getting all users'));
        }
        function GetAllStorico(id){
            return $http.get(host + 'rest/admin/getallstorico/' + id).then(handleSuccess, handleError('Error getting all users'));
        }        
        function GetAllAzienda(){
            return $http.get(host + 'rest/admin/getallazienda').then(handleSuccess, handleError('Error getting all users'));
        }
        function GetAll() {
            return $http.get(host + 'rest/admin/getAllBus').then(handleSuccess, handleError('Error getting all users'));
        }
        //ritorna le tratte di un autobus
        function GetById(id) {
            return $http.get(host + 'rest/admin/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }
        function GetAutobusPosition(id) {
            return $http.get(host + 'rest/admin/autobusposition/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function Create(tratta) {
            
            return $http.post(host + 'rest/admin/ins', tratta, config).then(handleSuccess, handleError('Error creating user'));
        }
        function CreatePosition(position) {
            //mancante
            return $http.post(host + 'rest/admin/insertposition', position, config).then(handleSuccess, handleError('Error creating user'));
        }
        function Add(tratta) {
            
            return $http.post(host + 'rest/admin/add', tratta, config).then(handleSuccess, handleError('Error creating user'));
        }
        function ModifyPosition(position) {
            return $http.put(host + 'rest/admin/modifyposition', position, config).then(handleSuccess, handleError('Error creating user'));
        }
        function DeletePosition(id){
            return $http.delete(host + 'rest/admin/deleteposition/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
