/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelDB.Tratta_auto;
import Repository.RouteRepository;
import Utilita.Filter.Secured;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("merge")
public class ControllerMerged {

    @Context
    private UriInfo context;
    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * Creates a new instance of ControllerMerged
     */
    public ControllerMerged() {
    }

    @POST
    @Secured
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "dettagliopercorso/{tratta1}/{tratta2}")
    public void gettraveldetail(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "tratta1") final String tratta1, @PathParam(value = "tratta2") final String tratta2) {
        executorService.submit(() -> {
            asyncResponse.resume(doGettraveldetail(tratta1, tratta2));
        });
    }

    private Response doGettraveldetail(@PathParam("tratta1") String tratta1, @PathParam("tratta2") String tratta2) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);

            Tratta_auto tratta_auto = routeRepository.getTravelDetail(tratta1, tratta2);
            return Response.ok(new Gson().toJson(tratta_auto)).build();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
