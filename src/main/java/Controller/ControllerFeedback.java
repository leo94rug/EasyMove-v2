/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DatabaseConstants.TableConstants.Notifica_tipologia;
import static DatabaseConstants.TableConstants.Notifica_tipologia.E_STATO_INSERITO_FEEDBACK;
import DatabaseConstants.TableConstants.Relazione_da_valutare;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.FEEDBACK_GIA_INSERITO;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.IMPOSSIBILE_INSERIRE_FEEDBACK;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.NON_E_ANCORA_POSSIBILE;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.PUO_INSERIRE_FEEDBACK;
import Eccezioni.ObjectNotFound;
import Interfaces.IDate;
import Model.ModelDB.Notifica;
import Model.ModelDB.Relazione;
import Model.ModelDB.Tratta_auto;
import Model.Request.FeedbackRqt;
import Model.Request.NotificaRqt;
import Model.Response.FeedbackRes;
import Repository.FeedbackRepository;
import Repository.NotificationRepository;
import Repository.RelazioneRepository;
import Repository.RouteRepository;
import Repository.UserRepository;
import Utilita.DatesConversion;
import Utilita.Filter.Secured;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Secured
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getfeedback/{id}")
    public void getfeedback(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetfeedback(id));
        });
    }

    @POST
    @Secured
    @Path(value = "checkispossibleinsertfeedback")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void checkispossibleinsertfeedback(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doCheckispossibleinsertfeedback(context, payload));
        });
    }

    @POST
    @Secured
    @Path(value = "insertfeedback")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void insertfeedback(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final FeedbackRqt feedbackRqt) {
        executorService.submit(() -> {
            asyncResponse.resume(doInsertfeedback(context, feedbackRqt));
        });
    }

    @POST
    @Secured
    @Path(value = "possibilitainserirefeedback")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void possibilitainserirefeedback(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doPossibilitainserirefeedback(context, payload));
        });
    }

    private Response doGetfeedback(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            FeedbackRepository feedbackRepository = new FeedbackRepository(connection);
            FeedbackRes feedbackRes = feedbackRepository.getFeedback(id);
            return Response.ok(new Gson().toJson(feedbackRes)).build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doCheckispossibleinsertfeedback(@Context final UriInfo context, final String payload) {
        try (Connection connection = ds.getConnection()) {
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            IDate dateUtility = new DatesConversion();
            JSONObject obj = new JSONObject(payload);
            String mittente = obj.getString("mittente");
            String destinatario = obj.getString("destinatario");
            Relazione relazione = relazioneRepository.getRelazione(mittente, destinatario);
            if (relazione != null) {
                switch (relazione.getDa_valutare()) {
                    case IMPOSSIBILE_INSERIRE_FEEDBACK: {
                        return Response.ok(new Gson().toJson(0)).build();
                    }
                    case NON_E_ANCORA_POSSIBILE: {
                        if (dateUtility.before(relazione.getDa_valutare_data(), dateUtility.now())) {
                            return Response.ok(new Gson().toJson(2)).build();
                        } else {
                            return Response.ok(new Gson().toJson(1)).build();
                        }
                    }
                    case PUO_INSERIRE_FEEDBACK: {
                        return Response.ok(new Gson().toJson(2)).build();
                    }
                    case FEEDBACK_GIA_INSERITO: {
                        notificationRepository.checkNotificationExistAndDelete(destinatario, mittente, Notifica_tipologia.INSERISCI_FEEDBACK);
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

    private Response doInsertfeedback(@Context UriInfo context, FeedbackRqt feedbackRqt) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);
            IDate dateUtility = new DatesConversion();
            NotificationRepository notificationRepository = new NotificationRepository(connection);
            FeedbackRepository feedbackRepository = new FeedbackRepository(connection);
            if (feedbackRepository.existingFeedback(feedbackRqt.getUtente_recensore(), feedbackRqt.getUtente_recensito())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            feedbackRqt.setDate(dateUtility.now());
            feedbackRqt.setId(UUID.randomUUID().toString());
            int i = feedbackRepository.insertFeedback(feedbackRqt);
            if (i != 0) {
                relazioneRepository.updateRelazioneDaValutare(feedbackRqt.getUtente_recensore(), feedbackRqt.getUtente_recensito(), Relazione_da_valutare.FEEDBACK_GIA_INSERITO, null);
                notificationRepository.checkNotificationExistAndDelete(feedbackRqt.getUtente_recensito(), feedbackRqt.getUtente_recensore(), Notifica_tipologia.INSERISCI_FEEDBACK);
                Notifica notifica = new Notifica(
                        UUID.randomUUID().toString(),
                        feedbackRqt.getUtente_recensito(),
                        feedbackRqt.getUtente_recensore(),
                        E_STATO_INSERITO_FEEDBACK,
                        dateUtility.now(),
                        dateUtility.addYears(1),
                        dateUtility.now(),
                        userRepository.getNomeCognome(feedbackRqt.getUtente_recensito()),
                        0,
                        userRepository.getNomeCognome(feedbackRqt.getUtente_recensore())
                );
                notificationRepository.insertNotifica(notifica);
            }
            return Response.ok().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    } 

    private Response doPossibilitainserirefeedback(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);
            RouteRepository routeRepository = new RouteRepository(connection);
            IDate dateUtility = new DatesConversion();
            JSONObject obj = new JSONObject(payload);
            String utente_1 = obj.getString("passeggero");
            String utente_2 = obj.getString("autista");
            String id_tappa = obj.getString("id_tappa");
            Tratta_auto tratta_auto = routeRepository.getTravelDetail(id_tappa, id_tappa);
            Relazione relazione = relazioneRepository.getRelazione(utente_1, utente_2);

            if (relazione != null) {
                switch (relazione.getDa_valutare()) {
                    case 0: {
                        int i = relazioneRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, tratta_auto.getOrario_partenza());
                        return Response.ok(new Gson().toJson(0)).build();
                    }
                    case 1: {
                        if (dateUtility.before(relazione.getDa_valutare_data(), tratta_auto.getOrario_partenza())) {
                            relazioneRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, tratta_auto.getOrario_partenza());
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
        } catch (JSONException | ParseException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ObjectNotFound ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
