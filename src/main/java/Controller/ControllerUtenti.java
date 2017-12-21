/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Eccezioni.ObjectNotFound;
import Model.ModelDB.Relazione;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Request.AutoRqt;
import Model.Request.FeedbackRqt;
import Model.Request.PrenotazioneRqt;
import Model.Request.UtenteRqt;
import Model.Response.FeedbackRes;
import Model.Response.PrenotazioneRes;
import Model.Response.UtenteRes;
import Model.Response.Viaggio_autoRes;
import Repository.CarRepository;
import Repository.PrenotazioneRepository;
import Repository.RouteRepository;
import Repository.UserRepository;
import Utilita.Crypt;
import Utilita.SendMailTLS;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("user")
public class ControllerUtenti {

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ControllerUtenti
     */
    public ControllerUtenti() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getbyemail/{email}")
    public Response user(@PathParam("email") String email) {
        try {
            Utente utente = Repository.UserRepository.getUtenteByEmail(email, ds);
            if (utente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(new Gson().toJson(utente)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getfeedback/{id: [0-9]+}")
    public Response getfeedback(@PathParam("id") int id) {
        try {
            FeedbackRes feedbackRes = UserRepository.getFeedback(id, ds);
            return Response.ok(new Gson().toJson(feedbackRes)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getprenotazioni/{id: [0-9]+}")
    public Response getPrenotazioni(@PathParam("id") int id) {
        try {
            List<PrenotazioneRes> prenotazione = PrenotazioneRepository.getPrenotazione(id, ds);
            return Response.ok(new Gson().toJson(prenotazione)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getviaggi/{id: [0-9]+}")
    public Response getviaggi(@PathParam("id") int id) {
        try {
            List<Viaggio_autoRes> viaggio_auto = UserRepository.getViaggi(id, ds);
            return Response.ok(new Gson().toJson(viaggio_auto)).build();
        } catch (SQLException ex) {
            //
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getpercorsi/{id: [0-9]+}")
    public Response getpercorsi(@PathParam("id") int id) {
        try {
            List<Viaggio_autoRes> viaggio_auto = UserRepository.getViaggi(id, ds);
            return Response.ok(new Gson().toJson(viaggio_auto)).build();
        } catch (SQLException ex) {
            //
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getprofilo/{id: [0-9]+}")
    public Response getProfilo(@PathParam("id") int id) {
        try {
            UtenteRes utenteRes = UserRepository.getUtente(id, ds);
            return Response.ok(new Gson().toJson(utenteRes)).build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getauto/{id: [0-9]+}")
    public Response getauto(@PathParam("id") int id) {
        try {
            List<Model.ModelDB.Auto> auto = CarRepository.getAuto(id, ds);
            return Response.ok(new Gson().toJson(auto)).build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("check/{user1: [0-9]+}/{user2: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response check(@PathParam("user1") String user1, @PathParam("user2") String user2) {
        try {
            String encript = Crypt.encrypt(user1);
            if (encript.equalsIgnoreCase(user2)) {
                UserRepository.updateUserEmailStatus(user1, ds);
            }
            return Response.ok().build();
        } catch (SQLException |
                BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @GET
    @Path("checkfriend/{user1: [0-9]+}/{user2: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkfriend(@PathParam("user1") int user1, @PathParam("user2") int user2) {
        try {
            boolean flag = Repository.UserRepository.checkFriend(user1, user2, ds);
            return Response.ok(new Gson().toJson(flag)).build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

    @POST
    @Path("addcar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addcar(@Context UriInfo context, AutoRqt autoRqt) {
        try {
            CarRepository.addCar(autoRqt, ds);
            return Response.ok().build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("insertfeedback")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertfeedback(@Context UriInfo context, FeedbackRqt feedbackRqt) {
        try {

            //TODO controllare se feedback esiste da relazione 
            //TODO cambiare stato relazione
            int i = UserRepository.addFeedback(feedbackRqt, ds);
            return Response.ok().build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context UriInfo context, String payload) {
        try {

            JSONObject obj = new JSONObject(payload);
            String email = obj.getString("email");
            String password = Crypt.encrypt(obj.getString("password"));
            UtenteRes utenteRes = UserRepository.getUtente(email, password, ds);
            if (utenteRes != null) {
                //String token = Utilita.MyToken.getToken(email);
                utenteRes.calcolaEta();
                //utenteRes.setToken(token);
                return Response.ok(new Gson().toJson(utenteRes)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (JSONException |
                SQLException |
                BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @POST
    @Path("modificapsw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificapsw(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            int id = Integer.parseInt(obj.getString("id"));
            String new_psw_crypt = Crypt.encrypt(obj.getString("password"));
            int i = UserRepository.setPassword(id, new_psw_crypt, ds);
            if (i == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok().build();
            }
        } catch (JSONException |
                SQLException |
                BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @POST
    @Path("possibilitainserirefeedback")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response possibilitainserirefeedback(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            JSONObject obj = new JSONObject(payload);
            int utente_1 = obj.getInt("passeggero");
            int utente_2 = obj.getInt("autista");
            int id_tappa = obj.getInt("id_tappa");
            Tratta_auto tratta_auto = RouteRepository.getTravelDetail(id_tappa, id_tappa, ds);
            Relazione relazione = UserRepository.getRelazione(utente_1, utente_2, ds);

            if (relazione != null) {
                switch (relazione.getDa_valutare()) {
                    case 0: {
                        int i = UserRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, tratta_auto.getOrario_partenza(), ds);
                        return Response.ok(new Gson().toJson(0)).build();
                    }
                    case 1: {
                        Timestamp timestamp = relazione.getDa_valutare_data();
                        if (tratta_auto.getOrario_partenza().after(timestamp)) {
                            int i = UserRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, tratta_auto.getOrario_partenza(), ds);
                            return Response.ok(new Gson().toJson(0)).build();
                        }
                        return Response.ok(new Gson().toJson(1)).build();
                    }
                    case 2: {
                        return Response.ok(new Gson().toJson(2)).build();
                    }
                    case 3: {
                        return Response.ok(new Gson().toJson(3)).build();

                    }
                }
            } else {
                throw new ObjectNotFound();
            }
            return Response.ok(new Gson().toJson(0)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (JSONException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post2(@Context UriInfo context, String payload) {
        UtenteRqt utenteRqt = null;
        try {
            JSONObject obj = new JSONObject(payload);
            utenteRqt = new UtenteRqt(obj);
            if (Repository.UserRepository.existingUser(utenteRqt.getEmail(), ds)) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            int rsint = UserRepository.insertUser(utenteRqt, ds);
            SendMailTLS.sendMailConfermaRegistrazione(utenteRqt.getEmail());
            return Response.ok().build();

        } catch (JSONException |
                SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Repository.UserRepository.deleteUser(utenteRqt.getId(), ds);
            } catch (SQLException ex1) {
                Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return Response.serverError().build();
        } catch (NoSuchAlgorithmException |
                MessagingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException |
                IllegalBlockSizeException |
                NoSuchPaddingException |
                UnsupportedEncodingException |
                BadPaddingException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("recuperapsw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recuperopsw(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            String email = payload;
            String new_psw = RandomStringUtils.randomAlphabetic(6);
            String new_psw_crypt = Crypt.encrypt(new_psw);
            Utente utente = UserRepository.getUtenteByEmail(email, ds);
            if (utente != null) {
                int i = UserRepository.setPassword(utente.getId(), new_psw_crypt, ds);
                if (i == 0) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    SendMailTLS.sendMailCambiaPassword(email, new_psw);
                    return Response.ok().build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (SQLException |
                BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                MessagingException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @PUT
    @Path("updateprofilo/{id: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put1(@Context UriInfo context, String payload, @PathParam("id") int id) {
        try {
            JSONObject obj = new JSONObject(payload);
            UtenteRqt utente = new UtenteRqt(obj);
            int i = UserRepository.updateUser(utente, id, ds);
            if (i == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        } catch (SQLException |
                BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                JSONException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @POST
    @Path(value = "checkispossibleinsertfeedback")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response checkispossibleinsertfeedback(@Context final UriInfo context, final String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            int mittente = obj.getInt("mittente");
            int destinatario = obj.getInt("destinatario");
            Relazione relazione = UserRepository.getRelazione(mittente, destinatario, ds);
            if (relazione != null) {
                switch (relazione.getDa_valutare()) {
                    case 0: {
                        return Response.ok(new Gson().toJson(0)).build();
                    }
                    case 1: {
                        Date date = new Date();
                        Timestamp timestamp = relazione.getDa_valutare_data();
                        if (timestamp.after(new Timestamp(date.getTime()))) {
                            return Response.ok(new Gson().toJson(2)).build();
                        } else {
                            return Response.ok(new Gson().toJson(1)).build();
                        }
                    }
                    case 2: {
                        return Response.ok(new Gson().toJson(2)).build();
                    }
                    case 3: {
                        return Response.ok(new Gson().toJson(3)).build();

                    }
                }
            } else {
                throw new ObjectNotFound();
            }
            return Response.ok(new Gson().toJson(0)).build();
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(new Gson().toJson(0)).build();
        }

    }

    /*
    @POST
    @Path(value = "checkispossibleinsertfeedback")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response checkispossibleinsertfeedback(@Context final UriInfo context, final String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            int mittente = obj.getInt("mittente");
            int destinatario = obj.getInt("destinatario");
            Relazione relazione = UserRepository.getRelazione(mittente,destinatario,ds);
            if (relazione!=null) {
                switch (relazione.getDa_valutare()) {
                    case 0: {
                        return Response.ok(new Gson().toJson(0)).build();
                    }
                    case 1: {
                        Date date = new Date();
                        Timestamp timestamp = relazione.getDa_valutare_data();
                        if (timestamp.after(new Timestamp(date.getTime()))) {
                            return Response.ok(new Gson().toJson(2)).build();
                        } else {
                            return Response.ok(new Gson().toJson(1)).build();
                        }
                    }
                    case 2: {
                        return Response.ok(new Gson().toJson(2)).build();
                    }
                    case 3: {
                        return Response.ok(new Gson().toJson(3)).build();

                    }
                }
            } else {
                throw new ObjectNotFound();
            }
            return Response.ok(new Gson().toJson(0)).build();
        } catch (SQLException | JSONException  ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }*/
    @POST
    @Path(value = "prenotazione")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response prenotazione(@Context final UriInfo context, final String payload) {
        try {
            PrenotazioneRqt prenotazione = new PrenotazioneRqt(new JSONObject(payload));
            //errori
            //il viaggio non esiste
            //i posti non sono disponibili
            //prezzo incongruente ?

            //cercare per ogni tappa la disponibilita 
            //diminuire disponibilita
            int checkPosti = PrenotazioneRepository.getDisponibilitaPrenotazione(prenotazione, ds);
            switch (checkPosti) {
                case -1: {
                    //IL VIAGGIO NON E' STATO TROVATO
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                case 0: {
                    //I POSTI NON SONO PIU DISPONIBILI
                    return Response.status(Response.Status.GONE).build();
                }
                case 1: {
                    //PRENOTARE
                    boolean result = RouteRepository.decreasePosti(prenotazione, ds);
                    PrenotazioneRepository.setPrenotation(prenotazione, ds);
                    return Response.status(Response.Status.OK).build();
                }
                default: {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path(value = "resendemail")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response resendemail(@Context final UriInfo context, final String payload) {
        try {
            SendMailTLS.sendMailConfermaRegistrazione(payload);
            return Response.ok().build();
        } catch (BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                MessagingException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @PUT
    @Path("confermaemail")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confermaemail(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            String hash = obj.getString("hash");
            String email = obj.getString("email");
            if (Crypt.encrypt(email).equalsIgnoreCase(hash)) {
                boolean result = UserRepository.userConfirm(email, ds);
                if (result) {
                    return Response.noContent().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (BadPaddingException |
                UnsupportedEncodingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                JSONException |
                SQLException |
                InvalidAlgorithmParameterException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @PUT
    @Path("updateimage")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateimage(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            String immagine = obj.getString("immagine");
            int id = obj.getInt("id");
            int n = UserRepository.updateUtenteImage(id, immagine, ds);
            if (n == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException | JSONException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("deletecar/{id: [0-9]+}")
    public Response delete1(@PathParam("id") int id) {
        try {
            int j = CarRepository.deleteCar(id, ds);
            return Response.noContent().build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("travelnumber/{id: [0-9]+}")
    public Response travelnumber(@PathParam("id") int id) {
        try {
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

            int k = UserRepository.getTravelNumber(id, date, ds);
            if (k == -1) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(new Gson().toJson(k)).build();
            }
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
