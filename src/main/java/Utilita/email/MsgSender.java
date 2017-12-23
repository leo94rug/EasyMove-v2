/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita.email;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author leo
 */
public class MsgSender {

    private static final String USERNAME = "info1.easymove@gmail.com";
    private static final String SENDER = "info1.easymove@gmail.com";
    private static final String PASSWORD = "pokjin838";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String AUTH = "true";
    private static final String STARTTLS = "true";

    public MsgSender() {

    }

    /**
     * Build and send text message
     *
     * @param textBody the text to be sent
     * @param subject subject of the email
     * @param recipients the list of recipients
     * @throws AddressException
     * @throws MessagingException
     */
    public void sendTextMessage(String textBody, String subject, String[] recipients) throws AddressException, MessagingException {

        // create properties so that we can retrieve a session on server
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", STARTTLS);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", AUTH);
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.user", USERNAME);
        properties.setProperty("mail.smtp.password", PASSWORD);
        properties.setProperty("mail.from", SENDER);
        properties.setProperty("mail.smtp.port", PORT);
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Get the Session object
        Session session = Session.getInstance(properties);

        // Construct the message and send it.
        Message msg = new MimeMessage(session);

        if (SENDER != null) {
            msg.setFrom(new InternetAddress(SENDER));
        } else {
            msg.setFrom();
        }

        // build the recipients list of the email
        if (recipients.length > 0) {
            for (String recipient : recipients) {
                msg.addRecipient(RecipientType.TO, (Address) new InternetAddress(recipient));
                // we only use TO recipients but CC and BCC may be use in
                // addition
            }
        }

        // add the email subject from input
        msg.setSubject(subject);

        // add the text content
        msg.setText(textBody);
        msg.setSentDate(new Date());
        Transport transport = session.getTransport("smtp");
        transport.connect(HOST, USERNAME, PASSWORD);
        Address addresses[] = new InternetAddress[recipients.length];
        if (recipients.length > 0) {
            for (int i = 0; i < recipients.length; i++) {
                addresses[i] = new InternetAddress(recipients[i]);
            }
        }
        transport.sendMessage(msg, addresses);
    }
}
