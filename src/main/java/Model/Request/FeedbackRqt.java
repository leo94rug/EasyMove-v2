/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Feedback;

/**
 *
 * @author leo
 */
public class FeedbackRqt extends Feedback {


    public FeedbackRqt(String testo,int utente_recensito,int utente_recensore,int valutazione_guida,int valutazione_puntualita,int valutazione_disponibilita) {
        super(testo,utente_recensito,utente_recensore,valutazione_guida,valutazione_puntualita,valutazione_disponibilita);
    }

    public FeedbackRqt() {
    }
    
    
}
