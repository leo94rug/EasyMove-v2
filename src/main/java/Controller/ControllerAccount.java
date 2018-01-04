/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static DatabaseConstants.TableConstants.Utente_tipologia.NON_CONFERMATO;
import static DatabaseConstants.TableConstants.Utente_tipologia.OSPITE;
import Interfaces.ICrypt;
import Model.ModelDB.Utente;
import Model.Request.UtenteRqt;
import Model.Response.UtenteRes;
import Repository.UserRepository;
import Utilita.Crypt.Encryptor;
import Utilita.Crypt.SimpleCrypt;
import Utilita.email.MsgFactory;
import Utilita.email.SendEmail;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.sql.DataSource;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author leo
 */
@Path("account")
public class ControllerAccount {

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;
    @Context
    private UriInfo context;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    public ControllerAccount() {

    }

    @POST
    @Path(value = "deleteall")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void deleteall(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(dodeleteall(context, payload));
        });
    }

    @POST
    @Path(value = "login")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void login(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doLogin(context, payload));
        });
    }

    @PUT
    @Path(value = "confermaemail")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void confermaEmail(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doConfermaEmail(context, payload));
        });
    }

    @POST
    @Path(value = "modificapsw")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void modificaPassword(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        Future<?> submit = executorService.submit(() -> {
            asyncResponse.resume(doModificaPassword(context, payload, ds));
        });
    }

    @POST
    @Path(value = "register")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void register(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final Utente payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doRegister(context, payload));
        });
    }

    @POST
    @Path(value = "resendemail")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void resendEmail(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String email) {
        executorService.submit(() -> {
            asyncResponse.resume(doResendEmail(context, email));
        });
    }

    @POST
    @Path(value = "recuperapsw")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public void recuperopsw(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doRecuperoPassword(context, payload));
        });
    }

    @GET
    @Path(value = "check/{user1: [0-9]+}/{user2: [0-9]+}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public void updateEmailStatus(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "user1") final String user1, @PathParam(value = "user2") final String user2) {
        executorService.submit(() -> {
            asyncResponse.resume(doUpdateEmailStatus(user1, user2));
        });
    }

    private Response doUpdateEmailStatus(@PathParam("user1") String user1, @PathParam("user2") String user2) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new SimpleCrypt();
            String encript = crypt.encrypt(user1);
            if (encript.equalsIgnoreCase(user2)) {
                userRepository.updateUserEmailStatus(user1);
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415
        }
    }

    private Response doLogin(@Context UriInfo context, String payload) {
        try {
            Connection connection = ds.getConnection();
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new Encryptor();
            JSONObject obj = new JSONObject(payload);
            String email = obj.getString("email");
            String password = crypt.encrypt(obj.getString("password"));
            UtenteRes utenteRes = userRepository.getUtente(email);
            if (utenteRes == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (utenteRes.getTipo() == NON_CONFERMATO) {
                return Response.status(499).build();
            }
            if (!utenteRes.getPassword().equals(password)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (utenteRes.getTipo() == OSPITE) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            //String token = Utilita.MyToken.getToken(email);
            utenteRes.calcolaEta();
            //utenteRes.setToken(token);
            return Response.ok(new Gson().toJson(utenteRes)).build();

        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response doConfermaEmail(@Context UriInfo context, String payload) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new SimpleCrypt();

            JSONObject obj = new JSONObject(payload);
            String hash = obj.getString("hash");
            String email = obj.getString("email");
            if (crypt.encrypt(email).equalsIgnoreCase(hash)) {
                UtenteRes utenteRes = userRepository.getUtente(email);
                if (utenteRes == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                if (utenteRes.getTipo() != NON_CONFERMATO) {
                    return Response.status(498).build();
                }

                boolean result = userRepository.userConfirm(email);
                if (result) {
                    return Response.noContent().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response doModificaPassword(@Context UriInfo context, String payload, DataSource ds) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new Encryptor();
            JSONObject obj = new JSONObject(payload);
            int id = Integer.parseInt(obj.getString("id"));
            String psw = obj.getString("password");
            int i = userRepository.setPassword(id, psw);
            if (i == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok().build();
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415
        }
    }

    private Response doRegister(@Context UriInfo context, Utente utenteRqt) {
        UserRepository userRepository = new UserRepository(ds);
        try {
            ICrypt crypt = new SimpleCrypt();
            if (userRepository.existingUser(utenteRqt.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            String foto_utente="data:image/png;base64,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";
            utenteRqt.setFoto_utente(foto_utente);
            int rsint = userRepository.insertUser(utenteRqt);
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.ConfermaRegistrazione);

            msg.buildEmail(new String[]{utenteRqt.getEmail(), crypt.encrypt(utenteRqt.getEmail())});
            msg.sendEmail(utenteRqt.getEmail());
            return Response.ok().build();

        } catch (JSONException |
                SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            try {
                userRepository.deleteUser(utenteRqt.getId());
            } catch (SQLException ex1) {
                Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return Response.serverError().build();
        } catch (NoSuchAlgorithmException |
                MessagingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException |
                IllegalBlockSizeException |
                NoSuchPaddingException |
                UnsupportedEncodingException |
                BadPaddingException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    private Response doResendEmail(@Context final UriInfo context, final String email) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            UtenteRes utenteRes = userRepository.getUtente(email);
            if (utenteRes == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (utenteRes.getTipo() != NON_CONFERMATO) {
                return Response.status(498).build();
            }
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.ConfermaRegistrazione);
            ICrypt crypt = new SimpleCrypt();
            msg.buildEmail(new String[]{email, crypt.encrypt(email)});
            msg.sendEmail(email);
            return Response.ok().build();
        } catch (MessagingException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Impossibile inviare l'email").build(); //415
        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Non è stato possibile criptare il valore").build(); //415
        }
    }

    private Response doRecuperoPassword(@Context UriInfo context, String payload) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            String email = payload;
            String new_psw = RandomStringUtils.randomAlphabetic(6);
            Utente utente = userRepository.getUtenteByEmail(email);
            if (utente != null) {
                int i = userRepository.setPassword(utente.getId(), new_psw);
                if (i == 0) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.CambiaPassword);
                    msg.buildEmail(new String[]{new_psw});
                    msg.sendEmail(email);
                    return Response.ok().build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415
        } catch (MessagingException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Impossibile inviare l'email").build(); //415
        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Non è stato possibile criptare il valore").build(); //415
        }
    }

    private Object dodeleteall(UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            String query = "DELETE FROM utente WHERE 1";
            PreparedStatement ps = connection.prepareStatement(query);
            int rs = ps.executeUpdate();
            query = "DELETE FROM auto WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM feedback WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM notifica WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM prenotazione WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM relazione WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM tratta_auto WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            query = "DELETE FROM viaggio_auto WHERE 1";
            ps = connection.prepareStatement(query);
            rs = ps.executeUpdate();
            return Response.ok().build();
        } catch (SQLException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }
    }

}
