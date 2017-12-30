(function () {
    'use strict';

    angular
            .module('app')
            .factory('DateService', DateService);

    function DateService() {
        var service = {};
        var pattern = "yyyy-mm-dd hh:mm:ss";

        service.stringFromDate = stringFromDate;
        service.dateFromString = dateFromString;
        return service;

        function stringFromDate(now) {
            var year = "" + now.getFullYear();
            var month = "" + (now.getMonth() + 1);
            if (month.length === 1) {
                month = "0" + month;
            }
            var day = "" + now.getDate();
            if (day.length === 1) {
                day = "0" + day;
            }
            var hour = "" + now.getHours();
            if (hour.length === 1) {
                hour = "0" + hour;
            }
            var minute = "" + now.getMinutes();
            if (minute.length === 1) {
                minute = "0" + minute;
            }
            var second = "" + now.getSeconds();
            if (second.length === 1) {
                second = "0" + second;
            }
            return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        }
        function dateFromString(mysql_string) {
            var t, result = null;
            if (typeof mysql_string === 'string') {
                t = mysql_string.split(/[- :]/);
                result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);
            }
            return result;
        }

    }

})();
