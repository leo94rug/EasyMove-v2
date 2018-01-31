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
        executorService.submit(() -> {
            asyncResponse.resume(doModificaPassword(context, payload));
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
    public void resendEmail(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
        executorService.submit(() -> {
            asyncResponse.resume(doResendEmail(context, payload));
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
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            ICrypt crypt = new SimpleCrypt();
            String encript = crypt.encrypt(user1);
            if (encript.equalsIgnoreCase(user2)) {
                userRepository.updateUserEmailStatus(user1);
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response doLogin(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            ICrypt crypt = new Encryptor();
            JSONObject obj = new JSONObject(payload);
            String email = obj.getString("email");
            String password = crypt.encrypt(obj.getString("password"));
            UtenteRes utenteRes = userRepository.getUtenteByEmail(email);
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
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            ICrypt crypt = new SimpleCrypt();

            JSONObject obj = new JSONObject(payload);
            String hash = obj.getString("hash");
            String email = obj.getString("email");
            if (crypt.encrypt(email).equalsIgnoreCase(hash)) {
                UtenteRes utenteRes = userRepository.getUtenteByEmail(email);
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

    private Response doModificaPassword(@Context UriInfo context, String payload) {
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            JSONObject obj = new JSONObject(payload);
            String id = obj.getString("id");
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
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            ICrypt crypt = new SimpleCrypt();
            if (userRepository.existingUser(utenteRqt.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            utenteRqt = userRepository.formalizer(utenteRqt);
            userRepository.insertUser(utenteRqt);
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.ConfermaRegistrazione);
            msg.buildEmail(new String[]{utenteRqt.getEmail(), crypt.encrypt(utenteRqt.getEmail())});
            msg.sendEmail(utenteRqt.getEmail());
            return Response.ok().build();

        } catch (JSONException |
                SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            try (Connection connection = ds.getConnection()) {
                UserRepository userRepository = new UserRepository(connection);
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
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
            UtenteRes utenteRes = userRepository.getUtenteByEmail(email);
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
        try (Connection connection = ds.getConnection()) {
            UserRepository userRepository = new UserRepository(connection);
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
