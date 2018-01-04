(function () {
    'use strict';

    angular
        .module('app')
        .controller('BachecaController', BachecaController);

    BachecaController.$inject = ['PrenotationService','$timeout','FeedbackService','NotificationsService','RouteService','$scope','$mdDialog','$store','UserService', '$location', 'FlashService','AuthenticationService'];
    function BachecaController(PrenotationService,$timeout,FeedbackService,NotificationsService,RouteService,$scope,$mdDialog,$store,UserService, $location, FlashService,AuthenticationService) {
        var vm = this;
        $('body,html').animate({scrollTop:0},800);
        vm.attenzione="";
        vm.bottone1=bottone1;
        vm.bottone2=bottone2;
        vm.utente = $store.get('utente');
        vm.initialize=initialize;
        vm.aggiornaNotifiche=aggiornaNotifiche;
        vm.removeNotification=removeNotification;
        vm.logout=logout;
        vm.profilo=profilo;
        $timeout(function(){
            var myEl = angular.element( document.querySelector( '#headerBacheca' ) );
            myEl.addClass('active');           
        });      
        initialize();
        function profilo(item){
            $location.path('/profilo-utenti/' + item.mittente);
        }
        function logout(){
            AuthenticationService.ClearCredentials();
            FlashService.set({title: "Logout effettuato", body: "", type: "info"});
            $location.path('/');
        }
        function removeNotification(id){

            NotificationsService.eliminaNotifica(id).then(function (response) {
                if(response.success===false){
                    switch(response.res.status){
                        case 500:{
                            $location.path('/error');
                            break;
                        }                    
                        case 404:{   
                            removeLocalNotification(id); 
                            FlashService.pop({title: "La notifica potrebbe essere già stata rimossa", body: "", type: "warning"});
                            getNotificationNumber(vm.utente.id);
                            $location.path('/error');
                            break;                        
                        }                        
                        case 410:{
                            removeLocalNotification(id); 
                            getNotificationNumber(vm.utente.id);  
                            FlashService.pop({title: "La notifica è stata già rimossa in precedenza", body: "", type: "warning"}); 
                            $location.path('/error');
                            break;                        
                        }
                    }
                }
                else{
                    removeLocalNotification(id);
                    vm.number=vm.number-1;
                }
            });
        }
        function removeLocalNotification(id){
            var index = vm.notifiche.findIndex(i => i.id === id);
            if (index > -1) {
                vm.notifiche.splice(index, 1);
            }
        }
        function bottone1(item,ev){
            debugger;
            switch(item.tipologia){
                case 1:{
                    accettaCondivisione(item);
                    FlashService.pop({title: "L'utente è stato aggiunto agli amici", body: "", type: "info"});
                    break;
                }
                case 2:{
                    prenota(item,ev);
                    break;
                }
                case 3:{
                    accettaPrenotazione(item);
                    break;
                }
                case 4:{
                    break;
                }
                case 5:{
                    /*non ci sono opzioni per chi riceve l'esito negativo per la prenotazione*/
                    break;
                }                
                case 6:{
                    /*non ci sono opzioni per chi riceve l'esito positivo per la amicizia*/
                    break;
                }
                case 7:{
                    debugger;
                    addFeedback(item.mittente);
                    break;
                }                
                case 8:{
                    $location.path("/feedback-ricevuti");
                }
            }
        }        
        function bottone2(item){
            switch(item.tipologia){
                case 1:{
                    rifiutaAmicizia(item);
                    FlashService.pop({title: "Hai rifiutato l'amicizia", body: "", type: "info"});
                    break;
                }
                case 2:{
                    /*non ci sono opzioni per chi riceve la notifica dell'amicizia non accettata*/
                    break;
                }  
                case 3:{
                    rifiutaPrenotazione(item);
                    FlashService.pop({title: "Hai rifiutato la prenotazione", body: "", type: "info"});
                    break;
                }
                case 4:{
                    /*non ci sono opzioni per chi riceve l'esito positivo per la prenotazione*/
                    break;
                }                
                case 5:{
                    /*non ci sono opzioni per chi riceve l'esito negativo per la prenotazione*/
                    break;
                }                
                case 6:{
                    /*non ci sono opzioni per chi riceve l'esito positivo per la amicizia*/
                    break;
                }
                case 7:{
                    break;
                }
            }  
        }
        function prenota(item,ev) {
            RouteService.GetDettaglioPercorso(item.id_partenza,item.id_arrivo).then(function(response){
                if(response.success===false){
                    switch(response.res.status){
                        case 500:{
                            $location.path('/error');
                            break;
                        }                    
                        case 404:{   
                            removeLocalNotification(id); 
                            FlashService.pop({title: "Il viaggio potrebbe essere stato rimosso", body: "", type: "info"});
                            break;                        
                        }                        
                        case 410:{
                            removeLocalNotification(id); 
                            getNotificationNumber(vm.utente.id);  
                            FlashService.pop({title: "Il viaggio potrebbe essere già stato effettuato", body: "", type: "info"}); 
                            break;                        
                        }
                    }
                }
                else{
                    var dettaglioPercorso=response.res.data;
                    item.posti=dettaglioPercorso.posti;
                    $mdDialog.show({
                        controller: DialogController,
                        templateUrl: 'app-code/user/personal_bacheca/prenotation_dialog.app.tmpl.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose:true,
                        locals: {
                            items: item
                        },
                    })
                    .then(function(answer) {
                        vm.aggiornaNotifiche();
                    }, function() {
                        
                    });
                }
            });
        };
        function DialogController($scope, $mdDialog,items) {
            
            $scope.item=items;
            $scope.attenzione="";
            $scope.posti=0;
            $scope.options = [
                { category: 'posti', name: '1',value:1 },
                { category: 'posti', name: '2',value:2 },
                { category: 'posti', name: '3',value:3 },
                { category: 'posti', name: '4',value:4 },
                { category: 'posti', name: '5',value:5 },
                { category: 'posti', name: '6',value:6 },
            ];
            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.answer = function(answer) {
                if ($scope.offer.$valid) {
                    if($scope.posti<=items.posti){
                        var send=new Object();
                        var stringPosti=" posti";
                        if ($scope.posti==1) {stringPosti=" posto"}
                        send.messaggio=items.nome_destinatario + ' vorrebbe prenotare ' + $scope.posti + stringPosti;
                        send.posti_da_prenotare=$scope.posti;
                        send.mittente=items.destinatario;
                        send.destinatario=items.mittente;
                        send.tipologia=3;
                        send.id_viaggio=items.id_viaggio;
                        send.fine_validita=items.fine_validita.getTime();
                        send.inizio_validita=items.inizio_validita.getTime();
                        send.posti=items.posti;
                        send.nome_viaggio=items.nome_viaggio;
                        send.id_partenza=items.id_partenza;
                        send.id_arrivo=items.id_arrivo;
                        NotificationsService.inviaNotifica(send).then(function(response) {
                            if(response.success===false){
                                $location.path('/error');
                            }
                            else{
                                FlashService.pop({title: "Hai inviato una richiesta di prenotazione", body: "", type: "info"});
                                removeNotification(items.id);
                                $mdDialog.hide(answer);
                            }
                        });
                    }
                    else{
                        $scope.attenzione="ATTENZIONE";
                    }
                }
            };
        }
        function aggiornaNotifiche(){
            NotificationsService.GetNotifiche(vm.utente.id).then(function (response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    vm.notifiche=response.res.data;
                    vm.notifiche.forEach( function (arrayItem){
                        arrayItem.data = new Date(arrayItem.data);
                        arrayItem.fine_validita = new Date(arrayItem.fine_validita);
                        arrayItem.inizio_validita = new Date(arrayItem.inizio_validita);
                    });
                }
            });
        }
        function initialize(){
            UserService.GetProfilo(vm.utente.id).then(function (response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    vm.user=response.res.data;
                    
                    getNotificationNumber(vm.utente.id);
                    NotificationsService.GetNotifiche(vm.utente.id).then(function (response) {
                        debugger;
                        if(response.success===false){
                            $location.path('/error');
                        }
                        else{
                            console.log(response.res.data);
                            vm.notifiche=response.res.data;
                            vm.notifiche.forEach( function (arrayItem){
                                arrayItem.data = new Date(arrayItem.data);
                                arrayItem.fine_validita = new Date(arrayItem.fine_validita);
                                arrayItem.inizio_validita = new Date(arrayItem.inizio_validita);
                            });
                        }
                    });
                }
            });
        }
        function getNotificationNumber(idUtente){
            NotificationsService.NotificationNumber(idUtente).then(function (response) {
                if(response.success===false){
                $location.path('/error');
                }
                else{
                    vm.number=response.res.data;
                }
            });  
        }
        function accettaCondivisione(item){
            var send=new Object();
            send.messaggio="L'amicizia è stata accettata, ora puoi visualizzare i suoi dati personali e prenotare un passaggio";
            send.mittente=vm.utente.id;
            send.destinatario=item.mittente;
            send.tipologia=2;
            send.id_viaggio=item.id_viaggio;
            send.fine_validita=item.fine_validita.getTime();
            send.inizio_validita=item.inizio_validita.getTime();
            send.posti=item.posti;
            send.nome_viaggio=item.nome_viaggio;
            send.id_partenza=item.id_partenza;
            send.id_arrivo=item.id_arrivo;
            NotificationsService.inviaNotifica(send).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    removeNotification(item.id);
                }
            });
        }        
        function rifiutaPrenotazione(item){
            var send=new Object();
            send.messaggio="E' stata rifiutata la tua richiesta di prenotazione";
            send.mittente=vm.utente.id;
            send.destinatario=item.mittente;
            send.tipologia=5;
            send.id_viaggio=item.id_viaggio;
            send.fine_validita=item.fine_validita.getTime();
            send.inizio_validita=item.inizio_validita.getTime();
            send.posti=item.posti;
            send.nome_viaggio=item.nome_viaggio;
            send.id_partenza=item.id_partenza;
            send.id_arrivo=item.id_arrivo;
            NotificationsService.inviaNotifica(send).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    removeNotification(item.id);
                }
            });
        }
        function sendFeedbackToMe(item){
            var send=new Object();
            send.messaggio="Inserisci un feedback";
            send.mittente=item.mittente;
            send.destinatario=item.destinatario;
            send.tipologia=7;
            send.id_viaggio=item.id_viaggio;
            send.fine_validita=item.fine_validita.getTime();
            //send.inizio_validita= set server-side
            send.nome_viaggio=item.nome_viaggio;
            send.id_partenza=item.id_partenza;
            send.id_arrivo=item.id_arrivo;
            NotificationsService.inviaNotifica(send).then(function(response) {
                debugger;
                if(response.success===false){
                    $location.path('/error');
                }
                else{

                }
            });
        }
        function sendFeedbackToYou(item){
            var send=new Object();
            send.messaggio="Inserisci un feedback";
            send.mittente=item.destinatario;
            send.destinatario=item.mittente;
            send.tipologia=7;
            send.id_viaggio=item.id_viaggio;
            send.fine_validita=item.fine_validita.getTime();
            //send.inizio_validita= set server-side
            send.nome_viaggio=item.nome_viaggio;
            send.id_partenza=item.id_partenza;
            send.id_arrivo=item.id_arrivo;
            NotificationsService.inviaNotifica(send).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
debugger;
                }
            });
        }
        function rifiutaAmicizia(item){
            var send=new Object();
            send.messaggio="E' stata rifiutata la tua richiesta di condividere i dati personali";
            send.mittente=vm.utente.id;
            send.destinatario=item.mittente;
            send.tipologia=6;
            send.id_viaggio=item.id_viaggio;
            send.fine_validita=item.fine_validita.getTime();
            send.inizio_validita=item.inizio_validita.getTime();
            send.posti=item.posti;
            send.nome_viaggio=item.nome_viaggio;
            send.id_partenza=item.id_partenza;
            send.id_arrivo=item.id_arrivo;
            NotificationsService.inviaNotifica(send).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    removeNotification(item.id);
                }
            });
        }
        function accettaPrenotazione(item){
            var prenotazione = {
                passeggero:item.mittente,
                autista:item.destinatario,
                id_partenza:item.id_partenza,
                id_arrivo:item.id_arrivo,
                posti:item.posti
            }
            PrenotationService.Prenotazione(prenotazione).then(function(response){
                debugger;
                if(response.success===false){
                    switch(response.res.status){
                        case 500:{
                            $location.path('/error');
                            break;
                        }                    
                        case 404:{   
                            //removeLocalNotification(id); 
                            FlashService.pop({title: "Il viaggio potrebbe essere stato rimosso", body: "", type: "info"});
                            break;                        
                        }                        
                        case 410:{
                            removeLocalNotification(id); 
                            getNotificationNumber(vm.utente.id);  
                            FlashService.pop({title: "Il viaggio potrebbe essere già stato effettuato", body: "", type: "info"}); 
                            break;                        
                        }
                    }
                }
                else{
                    var send=new Object();
                    send.messaggio="E' stata accettata la tua richiesta di prenotazione";
                    send.mittente=vm.utente.id;
                    send.destinatario=item.mittente;
                    send.tipologia=4;
                    send.id_viaggio=item.id_viaggio;
                    send.fine_validita=item.fine_validita.getTime();
                    send.inizio_validita=item.inizio_validita.getTime();
                    send.posti=item.posti;
                    send.nome_viaggio=item.nome_viaggio;
                    send.id_partenza=item.id_partenza;
                    send.id_arrivo=item.id_arrivo;
                    NotificationsService.inviaNotifica(send).then(function(response) {
                        debugger;
                        if(response.success===false){
                            $location.path('/error');
                        }
                        else{
                            debugger;
                            possibilitaInserireFeedback(item);
                            removeNotification(item.id);
                        }
                    });
                }
            });
        }       
        function addFeedback(id){
            $location.path('/feedback/' +id);
        }  
        function possibilitaInserireFeedback(item){
            var send1={
                'passeggero':item.mittente,
                'autista':item.destinatario,
                'id_tappa':item.id_partenza
            };
            var send2={
                'passeggero':item.destinatario,
                'autista':item.mittente,
                'id_tappa':item.id_partenza
            };
            debugger;
            FeedbackService.possibilitaInserireFeedback(send1).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                                   debugger;             //invia notifica conferma viaggio
                    switch(response.res.data){
                        case 0:{
                            break;
                        }                        
                        case 1:{
                            sendFeedbackToMe(item);
                            break;
                        }                        
                        case 2:{
                            break;
                        }                        
                        case 3:{
                            //gia inserito
                            break;
                        }
                    }
                }
            });            
            FeedbackService.possibilitaInserireFeedback(send2).then(function(response) {
                if(response.success===false){
                    $location.path('/error');
                }
                else{
                    switch(response.res.data){
                        case 0:{
                            break;
                        }                        
                        case 1:{
                            sendFeedbackToYou(item);
                            break;
                        }                        
                        case 2:{
                            break;
                        }                        
                        case 3:{
                            //gia inserito
                            break;
                        }
                    }
                }
            });
        }
    }

})();

