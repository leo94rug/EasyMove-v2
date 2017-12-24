(function () {
    'use strict';

    angular
        .module('app')
        .factory('FeedbackService', FeedbackService);

    FeedbackService.$inject = ['$http','CONFIG'];
    function FeedbackService($http,CONFIG) {
        var service = {};
        var controller="feedback";
        var config = {
            headers : {'Content-Type': 'application/json;'}
        };

        var host=CONFIG.HOST;
        service.GetFeedback = GetFeedback;
        service.possibilitaInserireFeedback=possibilitaInserireFeedback;
        service.checkIsPossibleInsertFeedback=checkIsPossibleInsertFeedback;
        service.InsertFeedback=InsertFeedback;
        return service;

        function checkIsPossibleInsertFeedback(item){
            return $http.post(host + 'rest/'+controller+'/checkispossibleinsertfeedback', item, config).then(handleSuccess, handleError);            
        }
        function possibilitaInserireFeedback(item){
            return $http.post(host + 'rest/'+controller+'/possibilitainserirefeedback', item, config).then(handleSuccess, handleError);            
        }
        function GetFeedback(email) {
            return $http.get(host + 'rest/'+controller+'/getfeedback/' + email).then(handleSuccess, handleError);
        }        
        function InsertFeedback(feedback){
            debugger;
            return $http.post(host + 'rest/'+controller+'/insertfeedback', feedback, config).then(handleSuccess, handleError);
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
