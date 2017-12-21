/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Eccezioni.ObjectNotFound;
import Model.ModelDB.Notifica;
import Model.ModelDB.Tratta_auto;
import Model.Request.NotificaRqt;
import Model.Response.NotificaRes;
import Repository.RouteRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Path("notification")
public class ControllerNotifiche {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ControllerNotifiche
     */
    public ControllerNotifiche() {
    }

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("getnotifiche/{id: [0-9]+}")
    public Response getnotifiche(@PathParam("id") int id) {
        try {
            List<NotificaRes> notificaRes = UserRepository.getNotifiche(id, ds);
            return Response.ok(new Gson().toJson(notificaRes)).build();
        } catch (SQLException ex) {
            //Utilita.Utilita.segErrore(ex, ds);
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

    @DELETE
    @Path("deletenotification/{id: [0-9]+}")
    public Response delete(@PathParam("id") int id) {
        try {
            Notifica notifica = UserRepository.getNotifica(id, ds);
            if (notifica == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (notifica.getStato() == 2 || notifica.getStato() == 3) {
                return Response.status(Response.Status.GONE).build();
            } else {
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                if (notifica.getFine_validita().before(timestamp)) {
                    return Response.status(Response.Status.GONE).build();
                } else {
                    int i = UserRepository.eliminaNotifica(id, ds);
                    if (i == 0) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    } else {
                        return Response.noContent().build();
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

    @POST
    @Path(value = "invianotifica")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response invianotifica(@Context final UriInfo context, final String payload) {
        try {
            NotificaRqt notifica = new NotificaRqt(new JSONObject(payload), ds);
            switch (notifica.getTipologia()) {
                case 2: {
                    UserRepository.setFriendship(notifica.getMittente(), notifica.getDestinatario(), ds);
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    //PrenotazioneRepository.setPrenotation(notifica.getPosti_da_prenotare(), notifica.getId_partenza(), notifica.getId_arrivo(), ds);
                    //TODO creare tabella prenotazioni              
                    break;
                }
                case 7: {
                    UserRepository.checkNotificationExistAndDelete(notifica.getMittente(), notifica.getDestinatario(), notifica.getTipologia(), ds);
                    Tratta_auto tratta_auto = RouteRepository.getTravelDetail(notifica.getId_partenza(), notifica.getId_arrivo(), ds);
                    Calendar calendar = Calendar.getInstance();
                    //notifica.setInizio_validita(tratta_auto.getOrario_partenza());
                    java.util.Date now = calendar.getTime();

                    notifica.setInizio_validita(new java.sql.Timestamp(now.getTime()));

                    calendar.add(Calendar.YEAR, +1);
                    java.util.Date future = calendar.getTime();
                    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(future.getTime());
                    notifica.setFine_validita(currentTimestamp);

                    break;
                }
            }
            //UserRepository.eliminaNotifica(notifica.getId(), ds);
            int id = UserRepository.insertNotifica(notifica, ds);
            return Response.ok(new Gson().toJson(id)).build();
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }
        @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("notificationnumber/{id: [0-9]+}")
    public Response notificationnumber(@PathParam("id") int id) {
        try {
            int notificationNumber = UserRepository.getNoticationNumber(id, ds);
            return Response.ok(new Gson().toJson(notificationNumber)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //500           
        }
    }
}
