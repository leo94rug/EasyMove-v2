/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DatabaseConstants.TableConstants.Relazione_approvato;
import Eccezioni.ObjectNotFound;
import Interfaces.IDate;
import Model.ModelDB.Notifica;
import Model.Request.NotificaRqt;
import Model.Response.NotificaRes;
import Repository.NotificationRepository;
import Repository.PrenotazioneRepository;
import Repository.RelazioneRepository;
import Repository.RouteRepository;
import Utilita.DatesConversion;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    @Context
    private UriInfo context;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * Creates a new instance of ControllerNotifiche
     */
    public ControllerNotifiche() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("notificationnumber/{id}")
    public void notificationnumber(@Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doNotificationnumber(id));
        });
    }

    @POST
    @Path(value = "invianotifica")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void invianotifica(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doInvianotifica(context, payload));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getnotifiche/{id}")
    public void getnotifiche(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetnotifiche(id));
        });
    }

    @DELETE
    @Path(value = "deletenotification/{id}")
    public void delete(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doDelete(id));
        });
    }

    private Response doNotificationnumber(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            int notificationNumber = notificationRepository.getNoticationNumber(id);
            return Response.ok(new Gson().toJson(notificationNumber)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build(); //500           
        }
    }

    private Response doGetnotifiche(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            List<NotificaRes> notificaRes = notificationRepository.getNotifiche(id);
            return Response.ok(new Gson().toJson(notificaRes)).build();
        } catch (SQLException ex) {
            //Utilita.Utilita.segErrore(ex, ds);
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doDelete(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
                        IDate dateUtility = new DatesConversion();
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            Notifica notifica = notificationRepository.getNotifica(id);
            if (notifica == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (notifica.getStato() == 2 || notifica.getStato() == 3) {
                return Response.status(Response.Status.GONE).build();
            } else if (dateUtility.before(notifica.getFine_validita(), dateUtility.now())) {
                return Response.status(Response.Status.GONE).build();
            } else {
                int i = notificationRepository.eliminaNotifica(id);
                if (i == 0) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.noContent().build();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doInvianotifica(@Context final UriInfo context, final String payload) {
        try (Connection connection = ds.getConnection()) {
                                    IDate dateUtility = new DatesConversion();
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);
            RouteRepository routeRepository = new RouteRepository(connection);
            PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(connection);
            NotificaRqt notifica = new NotificaRqt(new JSONObject(payload), connection);
            switch (notifica.getTipologia()) {
                // E' stata inviata una richiesta di amicizia
                case 1: {
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    notificationRepository.checkNotificationExistAndDelete(notifica.getMittente(), notifica.getDestinatario(), notifica.getTipologia());
                    relazioneRepository.setRelazioneApprovato(notifica.getMittente(), notifica.getDestinatario(), Relazione_approvato.IN_ATTESA);
                    break;
                }
                case 2: {
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    notificationRepository.checkNotificationExistAndDelete(notifica.getMittente(), notifica.getDestinatario(), notifica.getTipologia());
                    relazioneRepository.setRelazioneApprovato(notifica.getMittente(), notifica.getDestinatario(), Relazione_approvato.APPROVATO);
                    relazioneRepository.setRelazioneApprovato(notifica.getDestinatario(), notifica.getMittente(), Relazione_approvato.APPROVATO);
                    break;
                }
                case 3: {
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    break;
                }
                // Prenotazione accettata
                case 4: {
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    if (!prenotazioneRepository.getDisponibilitaViaggio(notifica)) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                    if (!prenotazioneRepository.getDisponibilitaPosti(notifica)) {
                        return Response.status(Response.Status.GONE).build();
                    }
                    //PRENOTARE
                    routeRepository.decreasePosti(notifica);
                    notifica.setId(UUID.randomUUID().toString());
                    prenotazioneRepository.insertPrenotation(notifica);
                    try {
                        notificationRepository.insertFeedbackNotification(notifica.getMittente(), notifica.getDestinatario(), notifica);
                        notificationRepository.insertFeedbackNotification(notifica.getDestinatario(), notifica.getMittente(), notifica);
                    } catch (SQLException | ParseException ex) {
                        Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);

                    }
                    break;
                }
                case 5: {
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    break;
                }
                case 6: { // Amicizia rifutata 
                    notifica.setInizio_validita(dateUtility.now());
                    notifica.setFine_validita(dateUtility.addYears());
                    relazioneRepository.setRelazioneApprovato(notifica.getMittente(), notifica.getDestinatario(), Relazione_approvato.BLOCCATO);
                    break;
                }
                case 7: {

                    break;
                }
            }
            //UserRepository.eliminaNotifica(notifica.getId(), ds);
            String id = UUID.randomUUID().toString();
            notifica.setId(id);
            notifica.setData(dateUtility.now());
            notificationRepository.insertNotifica(notifica);
            return Response.ok(new Gson().toJson(id)).build();
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerNotifiche.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
