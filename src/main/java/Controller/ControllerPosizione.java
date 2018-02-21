/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelDB.Posizione;
import Repository.PosizioneRepository;
import Utilita.Filter.Secured;
import com.google.gson.Gson;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("posizione")
public class ControllerPosizione {

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Secured
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "")
    public void getByEmail(@Suspended final AsyncResponse asyncResponse) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetPositions());
        });
    }

    private Response doGetPositions() {
        try (Connection connection = ds.getConnection()) {
            PosizioneRepository posizioneRepository = new PosizioneRepository(connection);
            List<Posizione> posizioni = posizioneRepository.getPosizioni();
            return Response.ok(new Gson().toJson(posizioni)).build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

}

