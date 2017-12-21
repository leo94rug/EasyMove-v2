(function () {
  'use strict';

  angular
  .module('app')
  .factory("FlashService", FlashService); 
  FlashService.$inject = ['$rootScope'];
  function FlashService($rootScope) {  
    var queue = [], currentMessage = {};
    $rootScope.$on('$routeChangeSuccess', function() {
      if (queue.length > 0) {
        var message = queue.shift();
          switch(message.type) {
          case 'success':
            toastr.success(message.body, message.title);
            break;
          case 'info':
            toastr.info(message.body, message.title);
            break;
          case 'warning':
            toastr.warning(message.body, message.title);
            break;
          case 'error':
            toastr.error(message.body, message.title);
            break;
        }
      }
      else{

        }    
    });
  
    return {
      set: function(message) {
        //var msg = message;
        queue.push(message);

      },
      get: function(message) {
        return currentMessage;
      },
      pop: function(message) {
        switch(message.type) {
          case 'success':
            toastr.success(message.body, message.title);
            break;
          case 'info':
            toastr.info(message.body, message.title);
            break;
          case 'warning':
            toastr.warning(message.body, message.title);
            break;
          case 'error':
            toastr.error(message.body, message.title);
            break;
        }
      }
    }
  }
})();