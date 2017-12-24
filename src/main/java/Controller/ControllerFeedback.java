/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DatabaseConstants.TableConstants.Notifica_tipologia;
import DatabaseConstants.TableConstants.Relazione_da_valutare;
import Eccezioni.ObjectNotFound;
import Model.ModelDB.Relazione;
import Model.ModelDB.Tratta_auto;
import Model.Request.FeedbackRqt;
import Model.Response.FeedbackRes;
import Repository.FeedbackRepository;
import Repository.NotificationRepository;
import Repository.RouteRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
@Path("feedback")
public class ControllerFeedback {

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ControllerFeedback
     */
    public ControllerFeedback() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getfeedback/{id: [0-9]+}")
    public Response getfeedback(@PathParam("id") int id) {
        try {
            FeedbackRes feedbackRes = FeedbackRepository.getFeedback(id, ds);
            return Response.ok(new Gson().toJson(feedbackRes)).build();
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
            if(FeedbackRepository.existingFeedback(feedbackRqt.getUtente_recensore(), feedbackRqt.getUtente_recensito(), ds)){
                return Response.status(Response.Status.CONFLICT).build();
            }
            int i = FeedbackRepository.addFeedback(feedbackRqt, ds);
            if(i!=0){
                UserRepository.updateRelazioneDaValutare(feedbackRqt.getUtente_recensore(), feedbackRqt.getUtente_recensito(),Relazione_da_valutare.FEEDBACK_GIA_INSERITO ,null, ds);
                NotificationRepository.checkNotificationExistAndDelete(feedbackRqt.getUtente_recensore(), feedbackRqt.getUtente_recensito(), Notifica_tipologia.INSERISCI_FEEDBACK, ds);
            }
            return Response.ok().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("possibilitainserirefeedback")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response possibilitainserirefeedback(@Context UriInfo context, String payload) {
        try {
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
}
