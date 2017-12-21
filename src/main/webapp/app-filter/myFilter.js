(function () {
  'use strict';

  angular
    .module('app')
    .factory('myFilter', myFilter);

    function myFilter(){
      
      return function (a,b,c,d,items) {
        
        var filtered = [];
        var letterMatch = new RegExp(letter, 'i');
        for (var i = 0; i < items.length; i++) {
          var item = items[i];
          if ((item.type===0)&&(a===1)) {
            filtered.push(item);
          }
          if ((item.type===1)&&(b===1)) {
            filtered.push(item);
          }      
          if ((item.type===2)&&(c===1)) {
            filtered.push(item);
          }
          if ((item.type===3)&&(d===1)) {
            filtered.push(item);
          }
        }
      return filtered;
    };
  };
});

