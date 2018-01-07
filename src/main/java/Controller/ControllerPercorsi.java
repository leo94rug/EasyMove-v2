/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelDB.Ricerca;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Viaggio_auto;
import Model.Response.Viaggio_autoRes;
import Repository.RouteRepository;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("route")
public class ControllerPercorsi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ControllerPercorsi
     */
    public ControllerPercorsi() {
    }

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "viaggio/{id}")
    public void getviaggio(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetviaggio(id));
        });
    }

    @DELETE
    @Path("delete/{id: [0-9]+}")
    public void delete(@Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "id")
            final int id) {
        executorService.submit(() -> {
            asyncResponse.resume(doDelete(id));
        });
    }

    @POST
    @Path(value = "cercaauto")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = {MediaType.APPLICATION_JSON})
    public void cercaAuto(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doCercaAuto(context, payload));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "percorso/{viaggio_fk}")
    public void gettravel(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "viaggio_fk") final int viaggio_fk) {
        executorService.submit(() -> {
            asyncResponse.resume(doGettravel(viaggio_fk));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "dettagliopercorso/{tratta1}/{tratta2}")
    public void gettraveldetail(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "tratta1") final int tratta1, @PathParam(value = "tratta2") final int tratta2) {
        executorService.submit(() -> {
            asyncResponse.resume(doGettraveldetail(tratta1, tratta2));
        });
    }

    @POST
    @Path(value = "insert")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void post2(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doPost2(context, payload));
        });
    }

    private Response doDelete(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);
            routeRepository.deleteTravel(id);
            return Response.noContent().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doCercaAuto(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            JSONObject obj = new JSONObject(payload);
            Ricerca ricerca = new Ricerca(obj);
            RouteRepository routeRepository = new RouteRepository(connection);

            List<Viaggio_autoRes> cercaAuto = routeRepository.cercaAuto(ricerca);
            return Response.ok(new Gson().toJson(cercaAuto)).build();
        } catch (JSONException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (ParseException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doPost2(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);
            JSONObject obj = new JSONObject(payload);
            JSONArray andata = obj.getJSONArray("andata");
            JSONArray ritorno = obj.getJSONArray("ritorno");
            JSONObject viaggio = obj.getJSONObject("viaggio");
            Viaggio_auto viaggio_auto = new Viaggio_auto(viaggio);
            int idviaggio = routeRepository.ottieniIdValido();
            viaggio_auto.setId(idviaggio);
            routeRepository.insertViaggio_auto(viaggio_auto);
            for (int i = 0; i < andata.length(); ++i) {
                JSONObject tratta = andata.getJSONObject(i);
                Tratta_auto tratta_auto = new Tratta_auto(tratta);
                tratta_auto.setViaggio_fk(idviaggio);
                routeRepository.insertTratta_auto(tratta_auto);
            }
            if (ritorno.length() > 0) {
                idviaggio = routeRepository.ottieniIdValido();
                viaggio_auto.setId(idviaggio);
                routeRepository.insertViaggio_auto(viaggio_auto);
                for (int i = 0; i < ritorno.length(); ++i) {
                    JSONObject tratta = ritorno.getJSONObject(i);
                    Tratta_auto tratta_auto = new Tratta_auto(tratta);
                    tratta_auto.setViaggio_fk(idviaggio);
                    routeRepository.insertTratta_auto(tratta_auto);
                }
            }
            return Response.ok().build();
        } catch (JSONException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGettraveldetail(@PathParam("tratta1") int tratta1, @PathParam("tratta2") int tratta2) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);

            Tratta_auto tratta_auto = routeRepository.getTravelDetail(tratta1, tratta2);
            return Response.ok(new Gson().toJson(tratta_auto)).build();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGettravel(@PathParam("viaggio_fk") int viaggio_fk) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);
            List<Tratta_auto> tratteList = routeRepository.getListTratteAuto(viaggio_fk);
            return Response.ok(new Gson().toJson(tratteList)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetviaggio(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(connection);
            return Response.ok(new Gson().toJson(routeRepository.getViaggioAuto(id))).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
