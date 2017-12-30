/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita.email;

/**
 *
 * @author leo
 */
public class SendEmailConfermaRegistrazione extends SendEmail {
    
    public SendEmailConfermaRegistrazione() {
        this.setSubject("Conferma registrazione");
        String content = "<a href='http://localhost:8085/EasyMove-maven/checkemail/%s/%s'>Clicca qui per confermare l'indirizzo email</a>";
        //String content="<a href='http://easymove.me/checkemail/%s/%s'>Clicca qui per confermare l'indirizzo email</a>";
        this.setContent(content);
    }
    
}
