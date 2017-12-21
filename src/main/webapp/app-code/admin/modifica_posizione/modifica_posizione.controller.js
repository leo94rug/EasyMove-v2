(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModificaPosizioneController', ModificaPosizioneController);

    ModificaPosizioneController.$inject = ['$mdDialog', '$location', '$rootScope', 'GestioneService','AuthenticationService'];
    function ModificaPosizioneController($mdDialog, $location, $rootScope, GestioneService,AuthenticationService) {
        var vm = this;
        vm.initialize = initialize;
        vm.showAdvanced=showAdvanced; 
        vm.showMap=showMap;
        vm.delete=deletePosition;
        vm.logout=logout;
        vm.initialize();

        function logout(){
            AuthenticationService.ClearCredentials();
            $location.path('/');
        }
        function initialize() {
            if ($rootScope.admin===undefined) {
                $location.path('/gestionestorico');
            }
            else{
                GestioneService.GetAllAutobusPosition($rootScope.admin.currentStorico).then(function (response) {
                    if(response.success===false){}
                    else{
                        vm.position=response;
                    }
                });
            }
        }

        Date.createFromMysql = function(mysql_string){ 
          var t, result = null;
          if( typeof mysql_string === 'string' )   {
            t = mysql_string.split(/[- :]/);
            result = new Date(t[0], t[1] - 1, t[2], t[3] || 0, t[4] || 0, t[5] || 0);          
          }
          return result;   
        }
        function showMap(id){
            $location.path('/mappa_posizione/' + id);
        }
        function deletePosition(obj){
                                obj=null;
            GestioneService.DeletePosition(obj.id).then(function(response){
                if(response.success===false){}
                else{
                    
                }                    
            });
        }
        function showAdvanced(ev,id) {
            $rootScope.admin = {
                currentPosition: id
            } 
            $mdDialog.show({
                controller: DialogController,
                controllerAs:'vm',
                templateUrl: 'app-code/admin/modifica_posizione/dialog.tmpl.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true,
                fullscreen: false // Only for -xs, -sm breakpoints.
            })
            .then(function(answer) {
                
                GestioneService.ModifyPosition(answer).then(function(response){
                    if(response.success===false){}
                    else{
                                      

                    }        
                });
            }, function() {});
        };

  function DialogController($mdDialog,$rootScope) {
    var vm=this;
    vm.hide=hide;
    vm.answer=answer;
    vm.cancel=cancel;
    vm.obj=$rootScope.admin.currentPosition;
    function hide() {
      $mdDialog.hide();
    };

    function cancel() {
      $mdDialog.cancel();
    };

    function answer() {
      $mdDialog.hide(vm.obj);
    };

  }

    }

})();
