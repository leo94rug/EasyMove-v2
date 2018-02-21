(function () {
    'use strict';

    angular
            .module('app')
            .controller('OfferShortController', OfferShortController);
    OfferShortController.$inject = ['RouteService','PosizioneService', 'DateService', '$timeout', '$q', '$location', '$rootScope', '$mdpDatePicker', '$mdpTimePicker', 'AuthenticationService', '$store', 'FlashService'];
    function OfferShortController(RouteService,PosizioneService, DateService, $timeout, $q, $location, $rootScope, $mdpDatePicker, $mdpTimePicker, AuthenticationService, $store, FlashService) {
        var vm = this;
        vm.submit = submit;
        vm.logout = logout;
        vm.initialize = initialize;
        vm.initialize();
        function initialize() {
            vm.simulateQuery = false;
            vm.isDisabled = false;
            loadAll();
            // list of `state` value/display objects
            vm.querySearch = querySearch;
            vm.selectedItemChangePartenza = selectedItemChangePartenza;
            vm.searchTextChangePartenza = searchTextChangePartenza;
            vm.selectedItemChangeArrivo = selectedItemChangeArrivo;
            vm.searchTextChangeArrivo = searchTextChangeArrivo;

            vm.dataprecerror = false;
            vm.today = new Date();
            vm.color = "red";
            vm.utente = $store.get('utente');
            $('body,html').animate({scrollTop: 0}, 800);
            $timeout(function () {
                var myEl = angular.element(document.querySelector('#headerOffer'));
                myEl.addClass('active');
            });
            vm.radioData = [
                {label: 'Andata', value: 1},
                {label: 'Andata e ritorno', value: 2}
            ];
            vm.partenzaList = [{0: vm.partenza}];
            vm.arrivoList = [{0: vm.arrivo}];
            vm.tappeList = [];
            vm.tappeModel = [
                {1: vm.tappa1},
                {2: vm.tappa2},
                {3: vm.tappa3},
                {4: vm.tappa4}
            ];
            vm.data = {
                group2: 1,
                type: ''
            };
            vm.contTappa = 0;
            vm.localizzato = 0;
            vm.types = '';
            vm.andataTratta = [];
            vm.ritornoTratta = [];
            if ($rootScope.saveOffer !== undefined) {
                vm.partenzaList = $rootScope.saveOffer.partenza;
                vm.tappeList = $rootScope.saveOffer.tappe;
                vm.arrivoList = $rootScope.saveOffer.arrivo;
                vm.andataTime = $rootScope.saveOffer.dateTimeA;
                vm.ritornoTime = $rootScope.saveOffer.dateTimeR;
                vm.data.group2 = $rootScope.saveOffer.ar;
            }
        }
        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function submit() {
            if (vm.data.group2 === 2) {
                if (vm.andataTime > vm.ritornoTime) {
                    vm.offer.$valid = false;
                    vm.dataprecerror = true;
                }
            }
            if (vm.offer.$valid) {
                vm.dataprecerror = false;
                var promises = [];
                var enumeratore = vm.partenzaList.concat(vm.tappeList, vm.arrivoList);
                for (var i = 0; i < enumeratore.length; i++) {
                    enumeratore[i].numero = i;
                }
                $rootScope.saveOffer = {
                    partenza: vm.partenzaList,
                    tappe: vm.tappeList,
                    arrivo: vm.arrivoList,
                    dateTimeA: vm.andataTime,
                    dateTimeR: vm.ritornoTime,
                    ar: vm.data.group2
                };
                for (var i = 0; i < enumeratore.length - 1; i++) {
                    promises.push(creaTratta(enumeratore[i], enumeratore[i + 1], i, vm.andataTime, 0));
                }
                if (vm.data.group2 === 2) {
                    var j = 0;
                    for (var i = enumeratore.length - 1; i > 0; i--) {
                        promises.push(creaTratta(enumeratore[i], enumeratore[i - 1], j, vm.ritornoTime, 1));
                        j++;
                    }
                }
                $q.all(promises).then(
                        function () {
                            var viaggio = {
                                utente_fk: vm.utente.id,
                                tipologia: 0
                            };
                            $rootScope.offerDetail = {
                                andata: vm.andataTratta,
                                ritorno: vm.ritornoTratta,
                                viaggio: viaggio
                            };
                        },
                        function () {
                            $location.path('/error');
                        }
                ).finally(function () {
                    vm.offerDetail = {
                        andata: $rootScope.offerDetail.andata,
                        ritorno: $rootScope.offerDetail.ritorno,
                        viaggio: $rootScope.offerDetail.viaggio
                    };
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
                    RouteService.Create(vm.offerDetail).then(function (response) {
                        if (response.success === false) {
                            switch (response.res.status) {
                                case 500:
                                {
                                    $location.path('/error');
                                    break;
                                }
                                case 401:
                                {
                                    $('body,html').animate({scrollTop: 0}, 800);
                                    FlashService.set({title: "Attenzione!", body: "Effettua il login per continuare", type: "warning"});
                                    $location.path('/login');
                                    break;
                                }
                                default:
                                {
                                    $location.path('/error');
                                    break;
                                }
                            }
                        } else {
                            $rootScope.saveOffer = undefined;
                            $rootScope.offerDetail = undefined;
                            vm.response = response;
                            FlashService.set({title: "Passaggio inserito!", body: "", type: "success"});
                            $location.path('/');
                        }
                    });
                });
            } else {

            }
        }
        function creaTratta(obj1, obj2, i, date, a) {
            var deferred = $q.defer();
            var componentForm = {
                route: 'long_name',
                locality: 'long_name',
                premise: 'short_name'
            };
            var route1;
            var locality1;
            var premise1;
            var lat1 = obj1.geometry.location.lat();
            var lng1 = obj1.geometry.location.lng();
            var formatted_address1 = obj1.formatted_address;
            for (var j = 0; j < obj1.address_components.length; j++) {
                var addressType = obj1.address_components[j].types[0];
                if (componentForm[addressType]) {
                    if (addressType === 'route') {
                        route1 = obj1.address_components[j][componentForm[addressType]];
                    }
                    if (addressType === 'locality') {
                        locality1 = obj1.address_components[j][componentForm[addressType]];
                    }
                    if (addressType === 'premise') {
                        premise1 = obj1.address_components[j][componentForm[addressType]];
                    }
                }
            }
            var nome1 = "";
            if (premise1 !== undefined) {
                nome1 = premise1 + ', ';
            }
            if (route1 !== undefined) {
                nome1 = nome1 + route1 + ', ';
            }
            if (locality1 !== undefined) {
                nome1 = nome1 + locality1;
            }
            var route2;
            var locality2;
            var premise2;
            var lat2 = obj2.geometry.location.lat();
            var lng2 = obj2.geometry.location.lng();
            var formatted_address2 = obj2.formatted_address;

            for (var j = 0; j < obj2.address_components.length; j++) {
                var addressType = obj2.address_components[j].types[0];
                if (componentForm[addressType]) {
                    if (addressType === 'route') {
                        route2 = obj2.address_components[j][componentForm[addressType]];
                    }
                    if (addressType === 'locality') {
                        locality2 = obj2.address_components[j][componentForm[addressType]];
                    }
                    if (addressType === 'premise') {
                        premise2 = obj2.address_components[j][componentForm[addressType]];
                    }
                }
            }
            var nome2 = "";
            if (premise2 !== undefined) {
                nome2 = premise2 + ', ';
            }
            if (route2 !== undefined) {
                nome2 = nome2 + route2 + ', ';
            }
            if (locality2 !== undefined) {
                nome2 = nome2 + locality2;
            }
            var distance;
            var duration;
            var time;
            var prezzo;
            var tratta;
            var service = new google.maps.DistanceMatrixService();
            service.getDistanceMatrix({
                origins: [formatted_address1],
                destinations: [formatted_address2],
                travelMode: 'DRIVING'
            },
                    function (response, status) {
                        if (response.success === false) {
                            deferred.reject("fail");
                            return deferred.promise;
                        } else {
                            if (response.rows[0].elements[0].status === 'ZERO_RESULTS') {
                                deferred.reject("fail");
                                return deferred.promise;
                            } else {
                                distance = response.rows[0].elements[0].distance.value;
                                duration = response.rows[0].elements[0].duration.value * 60000;
                                prezzo = Math.pow(2, (Math.log(distance / 1000, 2)) - 4.2) - (distance / 1000000);
                                debugger;
                                tratta = {
                                    orario_partenza: DateService.stringFromDate(date),
                                    enumerazione: i + 1,
                                    prezzo: prezzo,
                                    distanza: distance,
                                    posti: vm.posti,
                                    lat_partenza: lat1,
                                    lng_partenza: lng1,
                                    lat_arrivo: lat2,
                                    lng_arrivo: lng2,
                                    denominazione_partenza: nome1,
                                    denominazione_arrivo: nome2,
                                    durata: duration
                                };
                                if (a === 0) {
                                    vm.dateA = date.getTime() + duration;
                                    vm.andataTratta.push(tratta);
                                } else {
                                    vm.dateR = date.getTime() + duration;
                                    vm.ritornoTratta.push(tratta);
                                }
                                deferred.resolve("ok");
                                return deferred.promise;
                            }
                        }
                    });
            return deferred.promise;
        }
        Math.log = (function () {
            var log = Math.log;
            return function (n, base) {
                return log(n) / (base ? log(base) : 1);
            };
        })();




        // ******************************
        // Internal methods
        // ******************************

        function querySearch(query) {
            var results = query ? vm.states.filter(createFilterFor(query)) : vm.states;
            return results;
        }

        function searchTextChangePartenza(text) {
            console.log("searchTextChange");
        }

        function selectedItemChangePartenza(item) {
            vm.partenzaList[0] = item.address_components;
            console.log("selectedItemChange");
        }
        function searchTextChangeArrivo(text) {
            console.log("searchTextChange");
        }

        function selectedItemChangeArrivo(item) {
            vm.arrivoList[0] = item.address_components;
            console.log("selectedItemChange");
        }
        /**
         * Build `states` list of key/value pairs
         */
        function loadAll() {
            PosizioneService.GetPosizioni().then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                } else {
                    vm.states = response.res.data;
                    return vm.states.map(function (repo) {
                        repo.address_components = JSON.parse(repo.address_components);
                        repo.address_components.geometry.location.lat = function () {
                            return repo.lat;
                        };
                        repo.address_components.geometry.location.lng = function () {
                            return repo.lng;
                        };
                        return repo;
                    });
                }
            });

        }

        function createFilterFor(query) {
            var lowercaseQuery = angular.lowercase(query);

            return function filterFn(state) {
                return (angular.lowercase(state.nome).indexOf(lowercaseQuery) === 0);
            };

        }
    }
})();
