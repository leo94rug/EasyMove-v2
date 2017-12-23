(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http','CONFIG'];
    function UserService($http,CONFIG) {
        var service = {};
        var config = {
            headers : {'Content-Type': 'application/json;'}
        };
//        var config2={
//            headers : {'Content-Type': 'multipart/form-data;'}
//        };
        var host=CONFIG.HOST;
        service.GetByEmail = GetByEmail;
        service.GetProfiloUtenti = GetProfiloUtenti;
        service.GetAuto = GetAuto;
        service.Update = Update;
        service.UpdateImage = UpdateImage;
        service.DeleteCar = DeleteCar;
        service.GetProfilo = GetProfilo;
        service.GetFeedback = GetFeedback;
        service.GetViaggi = GetViaggi;
        service.GetPercorsi = GetPercorsi; 
        service.CreateCar = CreateCar;
        service.CheckFriend = CheckFriend;
        service.TravelNumber=TravelNumber;
        service.Check=Check;
        service.possibilitaInserireFeedback=possibilitaInserireFeedback;
        service.checkIsPossibleInsertFeedback=checkIsPossibleInsertFeedback;
        service.GetPrenotazioni=GetPrenotazioni;
        service.Prenotazione=Prenotazione;
        service.InsertFeedback=InsertFeedback;
        return service;

        function GetPrenotazioni(id){
            return $http.get(host + 'rest/user/getprenotazioni/' + id).then(handleSuccess, handleError);
        }
        function checkIsPossibleInsertFeedback(item){
            return $http.post(host + 'rest/user/checkispossibleinsertfeedback', item, config).then(handleSuccess, handleError);            
        }
        function possibilitaInserireFeedback(item){
            return $http.post(host + 'rest/user/possibilitainserirefeedback', item, config).then(handleSuccess, handleError);            
        }
        function CheckFriend(user1,user2){
            return $http.get(host + 'rest/user/checkfriend/' + user1 + '/' + user2).then(handleSuccess, handleError);
        }        
        function Check(user1,user2){
            return $http.get(host + 'rest/user/check/' + user1 + '/' + user2).then(handleSuccess, handleError);
        }
        function GetByEmail(email) {
            return $http.get(host + 'rest/user/getbyemail/' + email).then(handleSuccess, handleError);
        }
        function GetProfilo(email) {
            return $http.get(host + 'rest/user/getprofilo/' + email).then(handleSuccess, handleError);
        }        
        function GetFeedback(email) {
            return $http.get(host + 'rest/user/getfeedback/' + email).then(handleSuccess, handleError);
        }        
        function GetViaggi(email) {
            return $http.get(host + 'rest/user/getviaggi/' + email).then(handleSuccess, handleError);
        }           
        function GetPercorsi(email) {
            return $http.get(host + 'rest/user/getpercorsi/' + email).then(handleSuccess, handleError);
        }        
        function GetProfiloUtenti(email) {
            return $http.get(host + 'rest/user/getprofiloutenti/' + email).then(handleSuccess, handleError);
        }
        function GetAuto(email) {
            return $http.get(host + 'rest/user/getauto/' + email).then(handleSuccess, handleError);
        }
        function CreateCar(car) {
            return $http.post(host + 'rest/user/addcar', car, config).then(handleSuccess, handleError);
        }
        function Prenotazione(prenotazione){
            return $http.post(host + 'rest/user/prenotazione', prenotazione, config).then(handleSuccess, handleError);
        }
        function Update(user,id) {
            return $http.put(host + 'rest/user/updateprofilo/' + id, user,config).then(handleSuccess, handleError);
        }                
        function UpdateImage(user) {
            return $http.put(host + 'rest/user/updateimage', user,config).then(handleSuccess, handleError);
        }
        function DeleteCar(id) {
            return $http.delete(host + 'rest/user/deletecar/' + id).then(handleSuccess, handleError);
        }
        function InsertFeedback(feedback){
            debugger;
            return $http.post(host + 'rest/user/insertfeedback', feedback, config).then(handleSuccess, handleError);
        }            

        
        function TravelNumber(email) {
            return $http.get(host + 'rest/user/travelnumber/' + email).then(handleSuccess, handleError);
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
