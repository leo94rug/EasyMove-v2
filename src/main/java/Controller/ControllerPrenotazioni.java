/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Request.PrenotazioneRqt;
import Model.Response.PrenotazioneRes;
import Repository.PrenotazioneRepository;
import Repository.RouteRepository;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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
@Path("prenotation")
public class ControllerPrenotazioni {

    @Context
    private UriInfo context;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    public ControllerPrenotazioni() {
    }
    @POST
    @Path(value = "prenotazione")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public void prenotazione(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doPrenotazione(context, payload));
        });
    }    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getprenotazioni/{id: [0-9]+}")
    public void getPrenotazioni(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetPrenotazioni(id));
        });
    }

    private Response doGetPrenotazioni(@PathParam("id") int id) {
        try {
            PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(ds);
            List<PrenotazioneRes> prenotazione = prenotazioneRepository.getPrenotazione(id);
            return Response.ok(new Gson().toJson(prenotazione)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    private Response doPrenotazione(@Context final UriInfo context, final String payload) {
        try {
            RouteRepository routeRepository = new RouteRepository(ds);
            PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(ds);
            PrenotazioneRqt prenotazione = new PrenotazioneRqt(new JSONObject(payload));
            //errori
            //il viaggio non esiste
            //i posti non sono disponibili
            //prezzo incongruente ?
            
            //cercare per ogni tappa la disponibilita
            //diminuire disponibilita
            int checkPosti = prenotazioneRepository.getDisponibilitaPrenotazione(prenotazione);
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
                    boolean result = routeRepository.decreasePosti(prenotazione);
                    prenotazioneRepository.setPrenotation(prenotazione);
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
}
