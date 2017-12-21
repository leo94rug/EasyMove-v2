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
import static Utilita.DatabaseUtils.ottieniIdValido;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
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

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("viaggio/{id}")
    public Response getviaggio(@PathParam("id") int id) {
        try {
            return Response.ok(new Gson().toJson(RouteRepository.getViaggioAuto(id, ds))).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("percorso/{viaggio_fk}")
    public Response gettravel(@PathParam("viaggio_fk") int viaggio_fk) {
        try {
            List<Tratta_auto> tratteList = RouteRepository.getListTratteAuto(viaggio_fk, ds);
            return Response.ok(new Gson().toJson(tratteList)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("dettagliopercorso/{tratta1}/{tratta2}")
    public Response gettraveldetail(@PathParam("tratta1") int tratta1, @PathParam("tratta2") int tratta2) {
        try {
            Tratta_auto tratta_auto = RouteRepository.getTravelDetail(tratta1, tratta2, ds);
            return Response.ok(new Gson().toJson(tratta_auto)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    @POST
    @Path("getDistanceMatrix")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistanceMatrix(@Context UriInfo context, String payload) {
                    return Response.ok().build();

    }
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post2(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            JSONArray andata = obj.getJSONArray("andata");
            JSONArray ritorno = obj.getJSONArray("ritorno");
            JSONObject viaggio = obj.getJSONObject("viaggio");
            Viaggio_auto viaggio_auto = new Viaggio_auto(viaggio);
            int idviaggio = ottieniIdValido("viaggio_auto", ds);
            viaggio_auto.setId(idviaggio);
            viaggio_auto.insert(ds);
            for (int i = 0; i < andata.length(); ++i) {
                JSONObject tratta = andata.getJSONObject(i);
                Tratta_auto tratta_auto = new Tratta_auto(tratta);
                tratta_auto.setViaggio_fk(idviaggio);
                tratta_auto.insert(ds);
            }
            if(ritorno.length()>0){
                idviaggio = ottieniIdValido("viaggio_auto", ds);
                viaggio_auto.setId(idviaggio);
                viaggio_auto.insert(ds);
                for (int i = 0; i < ritorno.length(); ++i) {
                    JSONObject tratta = ritorno.getJSONObject(i);
                    Tratta_auto tratta_auto = new Tratta_auto(tratta);
                    tratta_auto.setViaggio_fk(idviaggio);
                    tratta_auto.insert(ds);
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

    @POST
    @Path("cercaauto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response cercaAuto(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            Ricerca ricerca = new Ricerca(obj);
            List<Viaggio_autoRes> cercaAuto = RouteRepository.cercaAuto(ricerca, ds);
            return Response.ok(new Gson().toJson(cercaAuto)).build();
        } catch (JSONException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("delete/{id: [0-9]+}")
    public Response delete1(@PathParam("id") int id) {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE viaggio_auto SET visibile=0 WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            int i = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerPercorsi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }
}
