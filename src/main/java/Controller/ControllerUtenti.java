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
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();            
        }
    }

    @GET
    @Path("checkfriend/{user1: [0-9]+}/{user2: [0-9]+}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkfriend(@PathParam("user1") int user1, @PathParam("user2") int user2) {
        try {
            int friend = Repository.UserRepository.checkFriend(user1, user2, ds);
            return Response.ok(new Gson().toJson(friend)).build();
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
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
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
