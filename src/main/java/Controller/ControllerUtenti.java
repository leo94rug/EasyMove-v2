/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelDB.Utente;
import Model.Response.UtenteRes;
import Model.Response.Viaggio_autoRes;
import Repository.RelazioneRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
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

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getbyemail/{email}")
    public void getByEmail(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "email") final String email) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetByEmail(email));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getviaggi/{id: [0-9]+}")
    public void getviaggi(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetviaggi(id));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getpercorsi/{id: [0-9]+}")
    public void getpercorsi(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetpercorsi(id));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getprofilo/{id: [0-9]+}")
    public void getProfilo(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetProfilo(id));
        });
    }

    @GET
    @Path(value = "checkfriend/{user1: [0-9]+}/{user2: [0-9]+}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public void checkfriend(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "user1") final int user1, @PathParam(value = "user2") final int user2) {
        executorService.submit(() -> {
            asyncResponse.resume(doCheckfriend(user1, user2));
        });
    }

    @PUT
    @Path(value = "updateprofilo/{id: [0-9]+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void updateProfilo(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final Utente payload, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doUpdateProfilo(context, payload, id));
        });
    }

    @PUT
    @Path(value = "updateimage")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void updateImage(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doUpdateImage(context, payload));
        });
    }

    private Response doUpdateImage(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            JSONObject obj = new JSONObject(payload);
            String immagine = obj.getString("immagine");
            int id = obj.getInt("id");
            int n = userRepository.updateUtenteImage(id, immagine);
            if (n == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException | JSONException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("travelnumber/{id: [0-9]+}")
    public void travelnumber(@Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "id")
            final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doTravelnumber(id));
        });
    }

    private Response doGetviaggi(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            List<Viaggio_autoRes> viaggio_auto = userRepository.getViaggi(id);
            return Response.ok(new Gson().toJson(viaggio_auto)).build();
        } catch (SQLException ex) {
            //
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetByEmail(@PathParam("email") String email) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            Utente utente = userRepository.getUtenteByEmail(email);
            if (utente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(new Gson().toJson(utente)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetpercorsi(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);

            List<Viaggio_autoRes> viaggio_auto = userRepository.getViaggi(id);
            return Response.ok(new Gson().toJson(viaggio_auto)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetProfilo(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            UtenteRes utenteRes = userRepository.getUtente(id);
            if(utenteRes==null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(new Gson().toJson(utenteRes)).build();
        } catch (SQLException | ParseException ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doCheckfriend(@PathParam("user1") int user1, @PathParam("user2") int user2) {
        try (Connection connection = ds.getConnection()) {
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);

            int friend = relazioneRepository.getRelazioneApprovato(user1, user2);
            return Response.ok(new Gson().toJson(friend)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doUpdateProfilo(@Context UriInfo context, Utente utenteRqt, @PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            int i = userRepository.updateUser(utenteRqt, id);
            if (i == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415
        }
    }

    private Response doTravelnumber(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            UserRepository userRepository = new UserRepository(connection);

            int k = userRepository.getTravelNumber(id, date);
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
