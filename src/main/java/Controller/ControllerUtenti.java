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
import static Utilita.Constants.defaultImagePath;
import Utilita.Image.ImageUtility;
import Utilita.Storage.DropboxService;
import com.dropbox.core.DbxException;
import com.google.gson.Gson;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
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
import org.apache.commons.io.FilenameUtils;

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
    @Path(value = "getviaggi/{id}")
    public void getviaggi(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetviaggi(id));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getpercorsi/{id}")
    public void getpercorsi(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetpercorsi(id));
        });
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(value = "getprofilo/{id}")
    public void getProfilo(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetProfilo(id));
        });
    }

    @GET
    @Path(value = "checkfriend/{user1}/{user2}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public void checkfriend(@Suspended final AsyncResponse asyncResponse,
            @PathParam(value = "user1") final String user1,
            @PathParam(value = "user2") final String user2) {
        executorService.submit(() -> {
            asyncResponse.resume(doCheckfriend(user1, user2));
        });
    }

    @PUT
    @Path(value = "updateprofilo/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void updateProfilo(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final Utente payload, @PathParam(value = "id") final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doUpdateProfilo(context, payload, id));
        });
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("travelnumber/{id}")
    public void travelnumber(@Suspended
            final AsyncResponse asyncResponse, @PathParam(value = "id")
            final String id) {
        executorService.submit(() -> {
            asyncResponse.resume(doTravelnumber(id));
        });
    }

    @PUT
    @Path(value = "updateimage/{id}/{filename}")
    @Consumes(value = MediaType.APPLICATION_OCTET_STREAM)
    public void uploadFile(@Suspended final AsyncResponse asyncResponse, final InputStream payload,
            @PathParam(value = "id") final String id,
            @PathParam(value = "filename") final String filename) {
        executorService.submit(() -> {
            asyncResponse.resume(doUploadFile(payload, id, filename));
        });
    }

    private Response doGetviaggi(@PathParam("id") String id) {
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
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetpercorsi(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);

            List<Viaggio_autoRes> viaggio_auto = userRepository.getViaggi(id);
            return Response.ok(new Gson().toJson(viaggio_auto)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doGetProfilo(@PathParam("id") String id) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            UtenteRes utenteRes = userRepository.getUtente(id);
            if (utenteRes == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(new Gson().toJson(utenteRes)).build();
        } catch (Exception ex) {

            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doCheckfriend(@PathParam("user1") String user1, @PathParam("user2") String user2) {
        try (Connection connection = ds.getConnection()) {
            RelazioneRepository relazioneRepository = new RelazioneRepository(connection);

            int friend = relazioneRepository.getRelazioneApprovato(user1, user2);
            return Response.ok(new Gson().toJson(friend)).build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doUpdateProfilo(@Context UriInfo context, Utente utenteRqt, @PathParam("id") String id) {
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

    private Response doTravelnumber(@PathParam("id") String id) {
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

    private Response doUploadFile(InputStream is, @PathParam("id") String id, @PathParam("filename") String filename) {
        try (Connection connection = ds.getConnection()) {
            DropboxService drop = new DropboxService();
            UserRepository userRepository = new UserRepository(connection);

            Utente utente = userRepository.getUtente(id);
            if (!utente.getImage_path().equals(defaultImagePath)) {
                drop.delete(utente.getImage_path());
            }
            String ext = FilenameUtils.getExtension(filename);
            Image image = ImageIO.read(is);
            BufferedImage bi = ImageUtility.createResizedCopy(image, 180, 180, true);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, ext, os);
            InputStream is2 = new ByteArrayInputStream(os.toByteArray());
            String dropboxPath = "/immagine-profilo/" + id + "/" + UUID.randomUUID();
            drop.uploadStream(is2, dropboxPath);
            String url = drop.getFileUrl(dropboxPath);
            String newUrl = url.replace("dl=0", "raw=1");
            int n = userRepository.updateUtenteImage(id, newUrl, dropboxPath);
            if (n == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }

    }

}
