/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita.email;

import Utilita.email.MsgSender;
import javax.mail.MessagingException;

/**
 *
 * @author leo
 */
public class SendEmail {

    private String content;
    private String subject;

    public void buildEmail( String[] params) {
        content = String.format(content, (Object[]) params);
    }

    public void sendEmail(String[] userEmails) throws MessagingException {
        MsgSender message = new MsgSender();
        message.sendTextMessage(content, subject, userEmails);
    }    
    public void sendEmail(String userEmail) throws MessagingException {
        MsgSender message = new MsgSender();
        message.sendTextMessage(content, subject, new String[]{userEmail});
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setSubject(String subject) {
        this.subject = subject;
    }
    
}
