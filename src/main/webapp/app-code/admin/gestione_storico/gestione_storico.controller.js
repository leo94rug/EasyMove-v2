(function () {
    'use strict';

    angular
        .module('app')
        .controller('GestioneStoricoController', GestioneStoricoController);

    GestioneStoricoController.$inject = ['$cookies','$scope','$mdDialog', '$location','$filter','$rootScope', 'GestioneService','AuthenticationService'];
    function GestioneStoricoController($cookies,$scope,$mdDialog, $location,$filter, $rootScope, GestioneService,AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;//add $scope
        vm.selectAzienda=selectAzienda;
        vm.logout=logout;
        vm.bool1=true;
        vm.bool2=false;
        vm.bool3=false;
        vm.initialize();
            $scope.$watch("vm.selectedId",function(newValue,oldValue) {
                if ((newValue!=undefined)&&(oldValue!=undefined)) {
                    vm.send="{id: " + newValue + ",old: "+oldValue+"}";
                    GestioneService.setStoricoAttivo(vm.send).then(function(response){
                        if(response.success===false){}
                        else{
                            var user={
                                storico:vm.send.id,
                            }
                            var cookieExp = new Date();
                            cookieExp.setDate(cookieExp.getDate() + 30);
                            $cookies.putObject('admin', user, { expires: cookieExp });
                        }                
                    });
                }
            });

        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        function initialize() {
            GestioneService.GetAllAzienda().then(function (response) {
                if(response.success===false){}
                else{
                    vm.azienda=response;
                }
            });
        }
        function selectAzienda(id){
            GestioneService.getStoricoAttivo(id).then(function(response){
                if(response.success===false){}
                else{
                    vm.selectedId=response;
                    $rootScope.admin = {
                        currentStorico: vm.selectedId
                    } 
                    GestioneService.GetAllStorico(id).then(function (response) {
                        if(response.success===false){}
                        else{
                            vm.storico=response;
                            vm.storico.forEach( function (arrayItem){
                                arrayItem.date = Date.createFromMysql(arrayItem.date);
                            });
                            vm.bool2=true;
                        }
                    });
                }                
            });

        }        


        Date.createFromMysql = function(mysql_string){ 
          var t, result = null;
          if( typeof mysql_string === 'string' )   {
            t = mysql_string.split(/[- :]/);
            result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);          
          }
          return result;   
        }
  }

    

})();
