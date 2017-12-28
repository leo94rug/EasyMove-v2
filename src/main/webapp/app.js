(function () {
    'use strict';
    angular
        .module('app', ['ngRoute', 'ngCookies','ngAnimate','ngAria','ngMessages','ngMaterial','google.places','mdPickers', 'jkAngularRatingStars','sideBarAngular','ngFileUpload', 'ngImgCrop'])
        .config(config)
        .run(run)
        .constant('CONFIG', {
            'HOST' : 'http://easymove.me/',
           // 'HOST' : 'http://localhost:8085/EasyMove-maven/',
           'ROOT' : 'rest'           
        })        
        .config(function($mdDateLocaleProvider) {
            $mdDateLocaleProvider.formatDate = function(date) {
              return moment(date).format('DD-MM-YYYY');
            };
          });

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'app-code/user/home/home.view.html',
                controllerAs: 'vm'
            })            
            .when('/home', {
                controller: 'HomeController',
                templateUrl: 'app-code/user/home/home.view.html',
                controllerAs: 'vm'
            })
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'app-code/user/login/login.view.html',
                controllerAs: 'vm'
            })
            .when('/register', {
                controller: 'RegisterController',
                templateUrl: 'app-code/user/register/register.view.html',
                controllerAs: 'vm'
            })
            .when('/feedback/:destinatario/', {
                controller: 'FeedbackController',
                templateUrl: 'app-code/user/feedback/feedback.view.html',
                controllerAs: 'vm'
            })
            .when('/prenotation/:tratta1/:tratta2', {
                controller: 'PrenotationController',
                templateUrl: 'app-code/user/prenotation/prenotation.view.html',
                controllerAs: 'vm'
            })
            .when('/showautobus/:tratta1/:tratta2', {
                controller: 'AutobusController',
                templateUrl: 'app-code/user/visualizza_autobus/visualizza_autobus.view.html',
                controllerAs: 'vm'
            })
            .when('/checkemail/:email/:hash', {
                controller: 'CheckEmailController',
                templateUrl: 'app-code/user/check_email/check_email.view.html',
                controllerAs: 'vm'
            })
            .when('/showautobuscambio', {
                controller: 'AutobusCambioController',
                templateUrl: 'app-code/user/visualizza_autobus_cambio/visualizza_autobus_cambio.view.html',
                controllerAs: 'vm'
            })
            .when('/showautobusibrido', {
                controller: 'AutobusIbridoController',
                templateUrl: 'app-code/user/visualizza_autobus_ibrido/visualizza_autobus_ibrido.view.html',
                controllerAs: 'vm'
            })
            .when('/offer1', {
                controller: 'Offer1Controller',
                templateUrl: 'app-code/user/offer1/offer1.view.html',
                controllerAs: 'vm'
            })            
            .when('/offer2', {
                controller: 'Offer2Controller',
                templateUrl: 'app-code/user/offer2/offer2.view.html',
                controllerAs: 'vm'
            })
            .when('/profilo', {
                controller: 'ProfiloUtenteController',
                templateUrl: 'app-code/user/personal_profilo/personal_profilo.app.view.html',
                controllerAs: 'vm'
            })
            .when('/bacheca', {
                controller: 'BachecaController',
                templateUrl: 'app-code/user/personal_bacheca/personal_bacheca.app.view.html',
                controllerAs: 'vm'
            })
            .when('/passaggi', {
                controller: 'PassaggiController',
                templateUrl: 'app-code/user/personal_passaggi/personal_passaggi.app.view.html',
                controllerAs: 'vm'
            })            
            .when('/prenotazioni', {
                controller: 'PersonalPrenotazioniController',
                templateUrl: 'app-code/user/personal_prenotazioni/personal_prenotazioni.view.html',
                controllerAs: 'vm'
            })
            .when('/feedback-ricevuti', {
                controller: 'FeedbackRicevutiController',
                templateUrl: 'app-code/user/personal_feedback/personal_feedback.app.view.html',
                controllerAs: 'vm'
            })
            .when('/search', {
                controller: 'SearchController',
                templateUrl: 'app-code/user/search/search.view.html',
                controllerAs: 'vm'
            })
            .when('/search/:lap/:lop/:laa/:loa/:dis/:cam/:g/:m/:a/:lup/:lua/:type', {
                controller: 'SearchController',
                templateUrl: 'app-code/user/search/search.view.html',
                controllerAs: 'vm'
            })
            .when('/funzionifermate', {
                controller: 'FunzioniFermateController',
                templateUrl: 'app-code/user/funzioni_fermate/funzioni_fermate.view.html',
                controllerAs: 'vm'
            })
            .when('/inserimentofermate', {
                controller: 'InserimentoFermateController',
                templateUrl: 'app-code/user/inserimento_fermate/inserimento_fermate.view.html',
                controllerAs: 'vm'
            })
            .when('/profilo-utenti/:id', {
                controller: 'ProfiloController',
                templateUrl: 'app-code/user/profilo_utenti/profilo_utenti.app.view.html',
                controllerAs: 'vm'
            })            
            .when('/menugestione', {
                controller: 'MenuGestioneController',
                templateUrl: 'app-code/admin/menu_gestione/menu_gestione.view.html',
                controllerAs: 'vm'
            })
            .when('/addposizione', {
                controller: 'AddPosizioneController',
                templateUrl: 'app-code/admin/aggiunta_posizione/aggiunta_posizione.view.html',
                controllerAs: 'vm'
            })
            .when('/deladdbus', {
                controller: 'DelAddBusController',
                templateUrl: 'app-code/admin/deladd_bus/deladd_bus.view.html',
                controllerAs: 'vm'
            })
            .when('/modificaposizione', {
                controller: 'ModificaPosizioneController',
                templateUrl: 'app-code/admin/modifica_posizione/modifica_posizione.view.html',
                controllerAs: 'vm'
            })            
            .when('/mappa_posizione/:id', {
                controller: 'MappaPosizioneController',
                templateUrl: 'app-code/admin/mappa_posizione/mappa_posizione.view.html',
                controllerAs: 'vm'
            })
            .when('/gestione', {
                controller: 'GestioneController',
                templateUrl: 'app-code/admin/gestione_tratte/gestione_tratte.view.html',
                controllerAs: 'vm'
            })
            .when('/gestionestorico', {
                controller: 'GestioneStoricoController',
                templateUrl: 'app-code/admin/gestione_storico/gestione_storico.view.html',
                controllerAs: 'vm'
            })            
            .when('/error', {
                controller: 'ErrorController',
                templateUrl: 'app-code/user/error/error.view.html',
                controllerAs: 'vm'
            })            
            .when('/recuperapsw', {
                controller: 'RecuperaPswController',
                templateUrl: 'app-code/user/recupera_psw/recupera_psw.view.html',
                controllerAs: 'vm'
            })            
            .when('/modificapsw', {
                controller: 'ModificaPswController',
                templateUrl: 'app-code/user/modifica_psw/modifica_psw.view.html',
                controllerAs: 'vm'
            })            
            .when('/sendconfirm', {
                controller: 'ResendEmailController',
                templateUrl: 'app-code/user/resend_email/resend_email.view.html',
                controllerAs: 'vm'
            })            
            .when('/traveldetail', {
                controller: 'TravelDetailController',
                templateUrl: 'app-code/user/travel_detail/travel_detail.view.html',
                controllerAs: 'vm'
            })
            
            .otherwise({ redirectTo: '/' });
            $locationProvider.html5Mode(true);
    }

    run.$inject = ['$rootScope', '$location', '$cookies', '$http','$store'];
    function run($rootScope, $location, $cookies, $http,$store) {
        // keep user logged in after page refresh
        //$rootScope.globals = $cookies.getObject('globals') || {};
        var utente = $cookies.getObject('globals') || null;
        if (utente!=null) {
            $store.set('utente',utente);
            //$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line

        }
        /*if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }
                    $rootScope.globals = {};
            $cookies.remove('globals');*/

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            var path=$location.path();
            for(var i = 1; i < path.length; i++){
                if (path.charAt(i)==='/') {
                    path=path.slice(0,i);
                    break;
                }
            }

            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray(path, ['/login','/home', '/register','/','/checkemail','/recuperapsw','/error','/sendconfirm']) === -1;
            var adminPage = $.inArray($location.path(), ['/gestionestorico','/gestione','/mappa_posizione','/modificaposizione','/deladdbus','/addposizione','/menugestione']) === -1;
            var loggedIn = $store.get('utente');
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
            if (loggedIn) {
                if (!adminPage && loggedIn.type!=2) {
                    $location.path('/');
                }
            }
        });
    }

})();

function MainCtrl($scope, $store) {
    $store.bind($scope, 'test', 'Some Default Text');
    
    $scope.clearTest = function(){ 
        $store.remove('test'); 
    };
}

