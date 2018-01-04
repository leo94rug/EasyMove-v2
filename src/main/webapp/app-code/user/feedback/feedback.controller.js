(function () {
    'use strict';
    angular
        .module('app')
        .controller('FeedbackController', FeedbackController);

    FeedbackController.$inject = ['FeedbackService', 'FlashService', 'AuthenticationService', '$routeParams', '$store', '$scope', '$location'];

    function FeedbackController(FeedbackService, FlashService, AuthenticationService, $routeParams, $store, $scope, $location) {
        var vm = this;
        vm.initialize = initialize;
        vm.inserisci = inserisci;
        vm.logout = logout;
        initialize();
        function logout() {
            AuthenticationService.ClearCredentials();
            FlashService.set({ title: "Logout effettuato", body: "", type: "info" });
            $location.path('/');
        }
        function initialize() {
            vm.utente = $store.get('utente');
            $('body,html').animate({ scrollTop: 0 }, 800);
            vm.destinatario = $routeParams.destinatario;
            vm.disable = true;
            var user = {
                "mittente": vm.utente.id,
                "destinatario": vm.destinatario
            }
            FeedbackService.checkIsPossibleInsertFeedback(user).then(function (response) {
                if (response.success === false) {
                    $location.path('/error');
                }
                else {
                    debugger;
                    switch (response.res.data) {
                        case 0: {
                            FlashService.set({ title: "Attenzione", body: "Non è possibile inserire un feedback", type: "error" });
                            $location.path("/");
                            //non è possibile
                        }
                        case 1: {
                            FlashService.set({ title: "Attenzione", body: "Non è ancora possibile inserire un feedback", type: "error" });
                            $location.path("/");
                            //non è ancora possibile
                        }
                        case 2: {
                            vm.disable = false;
                            //ok
                        }
                        case 3: {
                            FlashService.set({ title: "Attenzione", body: "Hai già inserito un feedback a questo utente", type: "error" });
                            $location.path("/");
                            //già inserito
                        }
                    }
                }
            });
        }
        function inserisci() {
            debugger;
            if (vm.feedback.valutazione_guida != undefined && vm.feedback.valutazione_puntualita != undefined && vm.feedback.valutazione_disponibilita != undefined) {
                vm.feedback.utente_recensito = parseInt(vm.destinatario);
                vm.feedback.utente_recensore = vm.utente.id;
                FeedbackService.InsertFeedback(vm.feedback).then(function (response) {
                    if (response.success === false) {
                        switch (response.res.status) {
                            case 500:
                                {
                                    $location.path('/error');
                                    break;
                                }
                            case 409:
                                {
                                    $('body,html').animate({ scrollTop: 0 }, 800);
                                    //vm.notconfirmed = true;
                                    FlashService.pop({ title: "Attenzione!", body: "Feedback già inserito", type: "warning" });
                                    vm.disable = true;
                                    break;
                                }
                            default:
                                {
                                    $location.path('/error');
                                    break;
                                }
                        }
                    }
                    else {
                        FlashService.set({ title: "Feedback inserito", body: "", type: "success" });
                        $location.path('/');
                    }
                });
            }
            else {
                $('body,html').animate({ scrollTop: 0 }, 800);
            }
        }
    }

})();