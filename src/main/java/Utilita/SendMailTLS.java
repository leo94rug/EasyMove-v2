/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita;

/**
 *
 * @author leo
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

    private static final String USERNAME = "info1.easymove@gmail.com";
    private static final String PASSWORD = "pokjin838";
    private static final String HOST  = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String AUTH = "true";
    private static final String STARTTLS = "true";
    private static final String INTESTAZIONE = "EasyMove";

    public static void sendMailConfermaRegistrazione(String userEmail) throws MessagingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException {
        //String content = "<a href='http://localhost:8085/EasyMove/checkemail/" + userEmail + "/" + Utilita.encrypt(userEmail) + "'>Clicca qui per confermare l'indirizzo email</a>";
        String content="<a href='http://easymove.me/checkemail/" + userEmail + "/" + Crypt.encrypt(userEmail) + "'>Clicca qui per confermare l'indirizzo email</a>";

        Properties props = new Properties();
        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(INTESTAZIONE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
        message.setSubject("Conferma registrazione");
        message.setContent(content, "text/html");

        Transport.send(message);
    }

    public static void sendMailCambiaPassword(String userEmail, String new_psw) throws MessagingException, NoSuchAlgorithmException {
        String content = "La tua nuova password è: " + new_psw;

        Properties props = new Properties();
        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(INTESTAZIONE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
        message.setSubject("Richiesta password");
        message.setContent(content, "text/html");

        Transport.send(message);
    }
}
