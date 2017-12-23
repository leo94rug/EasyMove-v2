/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelDB.Utente;
import Model.Request.UtenteRqt;
import Model.Response.UtenteRes;
import Repository.UserRepository;
import Utilita.Crypt;
import Utilita.email.MsgFactory;
import Utilita.email.SendEmail;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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

    /**
     * Creates a new instance of ControllerAccount
     */
    public ControllerAccount() {
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context UriInfo context, String payload) {
        try {

            JSONObject obj = new JSONObject(payload);
            String email = obj.getString("email");
            String password = Crypt.encrypt(obj.getString("password"));
            UtenteRes utenteRes = UserRepository.getUtente(email, password, ds);
            if (utenteRes != null) {
                //String token = Utilita.MyToken.getToken(email);
                utenteRes.calcolaEta();
                //utenteRes.setToken(token);
                return Response.ok(new Gson().toJson(utenteRes)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (JSONException |
                SQLException |
                BadPaddingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @POST
    @Path("modificapsw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificapsw(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            int id = Integer.parseInt(obj.getString("id"));
            String new_psw_crypt = Crypt.encrypt(obj.getString("password"));
            int i = UserRepository.setPassword(id, new_psw_crypt, ds);
            if (i == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok().build();
            }
        } catch (JSONException |
                SQLException |
                BadPaddingException |
                NoSuchPaddingException |
                NoSuchAlgorithmException |
                IllegalBlockSizeException |
                InvalidKeyException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //415           
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post2(@Context UriInfo context, String payload) {
        UtenteRqt utenteRqt = null;
        try {
            JSONObject obj = new JSONObject(payload);
            utenteRqt = new UtenteRqt(obj);
            if (Repository.UserRepository.existingUser(utenteRqt.getEmail(), ds)) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            int rsint = UserRepository.insertUser(utenteRqt, ds);
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.CambiaPassword);
            msg.buildEmail(new String[]{utenteRqt.getEmail(), Crypt.encrypt(utenteRqt.getEmail())});
            msg.sendEmail(utenteRqt.getEmail());
            return Response.ok().build();

        } catch (JSONException |
                SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Repository.UserRepository.deleteUser(utenteRqt.getId(), ds);
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

    @POST
    @Path("recuperapsw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recuperopsw(@Context UriInfo context, String payload) {
        try {
            String email = payload;
            String new_psw = RandomStringUtils.randomAlphabetic(6);
            String new_psw_crypt = Crypt.encrypt(new_psw);
            Utente utente = UserRepository.getUtenteByEmail(email, ds);
            if (utente != null) {
                int i = UserRepository.setPassword(utente.getId(), new_psw_crypt, ds);
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
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Non è stato possibile criptare il valore").build(); //415           
        }
    }

    @POST
    @Path(value = "resendemail")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response resendemail(@Context final UriInfo context, final String email) {
        try {
            SendEmail msg = MsgFactory.getBuildedEmail(MsgFactory.type.CambiaPassword);
            msg.buildEmail(new String[]{email, Crypt.encrypt(email)});
            msg.sendEmail(email);
            return Response.ok().build();
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Non è stato possibile criptare il valore").build(); //415           
        } catch (MessagingException ex) {
            Logger.getLogger(ControllerAccount.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Impossibile inviare l'email").build(); //415           
        }
    }

    @PUT
    @Path("confermaemail")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confermaemail(@Context UriInfo context, String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            String hash = obj.getString("hash");
            String email = obj.getString("email");
            if (Crypt.encrypt(email).equalsIgnoreCase(hash)) {
                boolean result = UserRepository.userConfirm(email, ds);
                if (result) {
                    return Response.noContent().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (JSONException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | SQLException ex) {
            Logger.getLogger(ControllerUtenti.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
