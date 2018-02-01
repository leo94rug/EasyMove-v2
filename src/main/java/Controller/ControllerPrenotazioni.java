/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Response.PrenotazioneRes;
import Repository.PrenotazioneRepository;
import com.google.gson.Gson;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("prenotation")
public class ControllerPrenotazioni {

    @Context
    private UriInfo context;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
    @Resource(name = "jdbc/webdb2")
    private DataSource ds;

    public ControllerPrenotazioni() {
    }

//    @POST
//    @Path(value = "prenotazione")
//    @Consumes(value = MediaType.APPLICATION_JSON)
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public void prenotazione(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
//        executorService.submit(() -> {
//            asyncResponse.resume(doPrenotazione(context, payload));
//        });
//    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getprenotazioni/{id}")
    public void getPrenotazioni(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetPrenotazioni(id));
        });
    }

    private Response doGetPrenotazioni(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(connection);
            List<PrenotazioneRes> prenotazione = prenotazioneRepository.getPrenotazione(id);
            return Response.ok(new Gson().toJson(prenotazione)).build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

//    private Response doPrenotazione(@Context final UriInfo context, final String payload) {
//        try (Connection connection = ds.getConnection()) {
//            RouteRepository routeRepository = new RouteRepository(connection);
//            PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(connection);
//            PrenotazioneRqt prenotazione = new PrenotazioneRqt(new JSONObject(payload));
//            //errori
//            //il viaggio non esiste
//            //i posti non sono disponibili
//            //prezzo incongruente ?
//
//            //cercare per ogni tappa la disponibilita
//            //diminuire disponibilita
//            if (prenotazioneRepository.getDisponibilitaPosti(prenotazione)) {
//                //PRENOTARE
//                routeRepository.decreasePosti(prenotazione);
//                prenotazioneRepository.setPrenotation(prenotazione);
//                return Response.status(Response.Status.OK).build();
//            } else {
//                //I POSTI NON SONO PIU DISPONIBILI
//                return Response.status(Response.Status.GONE).build();
//            }
//
//        } catch (JSONException | SQLException ex) {
//            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
