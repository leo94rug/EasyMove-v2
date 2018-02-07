/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Request.AutoRqt;
import Repository.CarRepository;
import Utilita.Filter.Secured;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("auto")
public class ControllerAuto {

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    @Context
    private UriInfo context;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * Creates a new instance of ControllerAuto
     */
    public ControllerAuto() {
    }

    @GET
        @Secured
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "{id}")
    public void getauto(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetauto(id));
        });
    }

    @POST
    @Secured
    @Path(value = "")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void addcar(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final AutoRqt autoRqt) {
        executorService.submit(() -> {
            asyncResponse.resume(doAddcar(context, autoRqt));
        });
    }

    @DELETE
    @Secured
    @Path(value = "delete/{id}")
    public void delete(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doDelete(id));
        });
    }

    private Response doGetauto(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            CarRepository carRepository = new CarRepository(connection);
            List<Model.ModelDB.Auto> auto = carRepository.getAuto(id);
            return Response.ok(new Gson().toJson(auto)).build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doAddcar(@Context UriInfo context, AutoRqt autoRqt) {
        try (Connection connection = ds.getConnection()) {
            CarRepository carRepository = new CarRepository(connection);
            String uuid = UUID.randomUUID().toString();
            autoRqt.setId(uuid);
            carRepository.addCar(autoRqt);
            return Response.ok().build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doDelete(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            CarRepository carRepository = new CarRepository(connection);
            int j = carRepository.deleteCar(id);
            return Response.noContent().build();
        } catch (SQLException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
