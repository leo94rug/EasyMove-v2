/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Interfaces.ICrypt;
import Model.ModelDB.Utente;
import Model.Request.UtenteRqt;
import Model.Response.UtenteRes;
import Repository.UserRepository;
import Utilita.Encryptor;
import Utilita.email.MsgFactory;
import Utilita.email.SendEmail;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    public void register(@Suspended final AsyncResponse asyncResponse, @Context final UriInfo context, final String payload) {
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
            ICrypt crypt = new Encryptor();
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
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new Encryptor();
            JSONObject obj = new JSONObject(payload);
            String email = obj.getString("email");
            String password = crypt.encrypt(obj.getString("password"));
            UtenteRes utenteRes = userRepository.getUtente(email, password);
            if (utenteRes != null) {
                //String token = Utilita.MyToken.getToken(email);
                utenteRes.calcolaEta();
                //utenteRes.setToken(token);
                return Response.ok(new Gson().toJson(utenteRes)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (Exception ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response doConfermaEmail(@Context UriInfo context, String payload) {
        try {
            UserRepository userRepository = new UserRepository(ds);
            ICrypt crypt = new Encryptor();

            JSONObject obj = new JSONObject(payload);
            String hash = obj.getString("hash");
            String email = obj.getString("email");
            if (crypt.encrypt(email).equalsIgnoreCase(hash)) {
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
            String new_psw_crypt = crypt.encrypt(obj.getString("password"));
            int i = userRepository.setPassword(id, new_psw_crypt);
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

    private Response doRegister(@Context UriInfo context, String payload) {
        UtenteRqt utenteRqt = null;
        UserRepository userRepository = new UserRepository(ds);
        try {
            ICrypt crypt = new Encryptor();
            JSONObject obj = new JSONObject(payload);
            utenteRqt = new UtenteRqt(obj);
            if (userRepository.existingUser(utenteRqt.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
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
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.CambiaPassword);
            ICrypt crypt = new Encryptor();
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
            ICrypt crypt = new Encryptor();
            String new_psw_crypt = crypt.encrypt(new_psw);
            Utente utente = userRepository.getUtenteByEmail(email);
            if (utente != null) {
                int i = userRepository.setPassword(utente.getId(), new_psw_crypt);
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

}
