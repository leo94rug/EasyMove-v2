(function() {
        'use strict';

        angular
            .module('app')
            .controller('Offer2Controller', Offer2Controller);

        Offer2Controller.$inject = ['$timeout','FlashService','$store', '$scope', 'UserService', '$location', '$rootScope', 'RouteService', '$mdpDatePicker', '$mdpTimePicker', 'AuthenticationService'];

        function Offer2Controller($timeout,FlashService,$store, $scope, UserService, $location, $rootScope, RouteService, $mdpDatePicker, $mdpTimePicker, AuthenticationService) {
            var vm = this;
            vm.utente = $store.get('utente');
            $('body,html').animate({scrollTop:0},800);
            $timeout(function(){
                var myEl = angular.element( document.querySelector( '#headerOffer' ) );
                myEl.addClass('active');           
            });      
            vm.options = [{
                    category: 'posti',
                    name: '1',
                    value: 1
                },
                {
                    category: 'posti',
                    name: '2',
                    value: 2
                },
                {
                    category: 'posti',
                    name: '3',
                    value: 3
                },
                {
                    category: 'posti',
                    name: '4',
                    value: 4
                },
                {
                    category: 'posti',
                    name: '5',
                    value: 5
                },
                {
                    category: 'posti',
                    name: '6',
                    value: 6
                },
                {
                    category: 'bagaglio',
                    name: 'Piccolo',
                    value: 1
                },
                {
                    category: 'bagaglio',
                    name: 'Medio',
                    value: 2
                },
                {
                    category: 'bagaglio',
                    name: 'Grande',
                    value: 3
                },
                {
                    category: 'deviazioni',
                    name: 'Non faccio deviazioni',
                    value: 1
                },
                {
                    category: 'deviazioni',
                    name: 'Faccio piccole deviazioni',
                    value: 2
                },
                {
                    category: 'deviazioni',
                    name: 'Faccio deviazioni',
                    value: 3
                },
                {
                    category: 'ritardo',
                    name: 'Sarò puntuale',
                    value: 1
                },
                {
                    category: 'ritardo',
                    name: '+/- 15 minuti',
                    value: 2
                },
                {
                    category: 'ritardo',
                    name: '+/- 30 minuti',
                    value: 3
                },
                {
                    category: 'ritardo',
                    name: '+/- 1 ora',
                    value: 4
                },
                {
                    category: 'ritardo',
                    name: '+/- 2 ore',
                    value: 5
                },
            ];
            vm.list = [];
            vm.typesData = [{
                    label: 'Si',
                    value: 1
                },
                {
                    label: 'No',
                    value: 2
                },
            ];

            vm.data = {
                group2: 1,
                type: ''
            };
            vm.submit = submit;
            vm.min = 1;
            vm.max = 10;
            vm.step = 1;
            vm.initialize = initialize;
            vm.types = ''
            vm.andata = [];
            vm.ritorno = [];
            vm.up = up;
            vm.down = down;
            initialize();
            vm.logout = logout;
            function logout() {
                AuthenticationService.ClearCredentials();
                FlashService.set({title: "Logout effettuato", body: "", type: "info"});
                $location.path('/');
            }

            function up(i) {
                vm.andata[i].prezzo++;
                if (vm.ritorno[0] != undefined) {
                    vm.ritorno[vm.ritorno.length - i - 1].prezzo++;
                }
            }

            function down(i) {
                vm.andata[i].prezzo--;
                if (vm.ritorno[0] != undefined) {
                    vm.ritorno[vm.ritorno.length - i - 1].prezzo--;
                }
            }

            function initialize() {
                if ($rootScope.offerDetail != undefined) {
                    vm.offerDetail = {
                        andata: $rootScope.offerDetail.andata,
                        ritorno: $rootScope.offerDetail.ritorno,
                        viaggio: $rootScope.offerDetail.viaggio
                    };
                    vm.andata=vm.offerDetail.andata;
                    UserService.GetAuto(vm.utente.id).then(function(response) {
                        if (response.success === false) {
                            $location.path('/error');
                        } else {
                            
                            vm.auto = response.res.data;
                            if (vm.auto === undefined) {} else {
                                for (var i = 0; i < vm.auto.length; i++) {
                                    vm.options.push({
                                        category: 'cars',
                                        name: vm.auto[i].modello,
                                        value: vm.auto[i].modello
                                    });
                                }
                            }
                        }
                    });
                } 
                else {
                    //stiamo ricaricando la pagina 
                    //in futuro geatire salvando i dati in memoria
                    $location.path('/offer1');
                }
            }


            function submit() {

                if ($scope.offer.$valid) {
                    var sum = 0;
                    for (var i = 0; i < vm.offerDetail.andata.length; i++) {
                        sum = sum + vm.offerDetail.andata[i].distanza;
                        vm.offerDetail.andata[i].posti = vm.posti;

                    }
                    for (var i = 0; i < vm.offerDetail.ritorno.length; i++) {
                        vm.offerDetail.ritorno[i].posti = vm.posti;
                    }
                    if (sum > 30000) {
                        vm.offerDetail.viaggio.tipo = 1;
                    }
                    $rootScope.offerDetail=vm.offerDetail;
                    RouteService.Create(vm.offerDetail).then(function(response) {
                        if (response.success === false) {
                            $location.path('/error');
                        } 
                        else {
                            $rootScope.saveOffer = undefined;
                            $rootScope.offerDetail = undefined;
                            vm.response = response;
                            FlashService.set({title: "Passaggio inserito!", body: "", type: "success"});
                            $location.path('/');
                        }
                    });
                } 

            }
            Math.log = (function() {
                var log = Math.log;
                return function(n, base) {
                    return log(n) / (base ? log(base) : 1);
                };
            })();
        }
    
})();