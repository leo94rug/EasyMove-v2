(function () {
    'use strict';

    angular
        .module('app')
        .factory('NotificationsService', NotificationsService);

    NotificationsService.$inject = ['$http','CONFIG'];
    function NotificationsService($http,CONFIG) {
        var service = {};
        var host=CONFIG.HOST;
        var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        };
        service.GetNotifiche = GetNotifiche;
        service.NotificationNumber=NotificationNumber;
        service.eliminaNotifica=eliminaNotifica;
        service.inviaNotifica=inviaNotifica;
        
        return service;
        function GetNotifiche(email) {
            return $http.get(host + 'rest/notification/getnotifiche/' + email).then(handleSuccess, handleError);
        }
        function NotificationNumber(email) {
            return $http.get(host + 'rest/notification/notificationnumber/' + email).then(handleSuccess, handleError);
        }  
        function eliminaNotifica(id) {
            return $http.delete(host + 'rest/notification/deletenotification/' + id).then(handleSuccess, handleError);
        }
        function inviaNotifica(notifica){
            return $http.post(host + 'rest/notification/invianotifica', notifica, config).then(handleSuccess, handleError);
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
