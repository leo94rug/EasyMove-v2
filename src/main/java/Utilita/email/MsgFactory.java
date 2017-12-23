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
public class MsgFactory {
    public enum type{
        CambiaPassword,
        ConfermaRegistrazione
    }

    public static SendEmail getBuildedEmail(MsgFactory.type type){
        switch(type){
            case CambiaPassword : {
                return new SendEmailCambiaPassword();
            }            
            case ConfermaRegistrazione : {
                return new SendEmailConfermaRegistrazione();
            }
            default : {
                return null;
            }
        }
    }
    
}
