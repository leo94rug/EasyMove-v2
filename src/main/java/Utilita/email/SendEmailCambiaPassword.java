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
public class SendEmailCambiaPassword extends SendEmail {

    public SendEmailCambiaPassword() {
        this.setSubject("Richiesta password");
        String content = "La tua nuova password è: %s";
        this.setContent(content);
    }
}
