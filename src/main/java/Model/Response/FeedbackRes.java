/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Feedback;
import Model.ModelDB.Utente;
import static java.lang.Math.round;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author leo
 */
public class FeedbackRes extends Feedback{
    private Utente utente;
    private List<FeedbackRes> listaFeedback;
    int media_valutazione_1;
    int media_valutazione_2;
    int media_valutazione_3;
    double media_valutazione;
    int cont;

    public FeedbackRes() {
    }    

        public String dateString;
        public void calcoloMedie() {
        Iterator it = listaFeedback.iterator();
        double sum = 0,sum2 = 0,sum3 = 0;
        double cont1 = 0,cont2 = 0, cont3 = 0;

        while (it.hasNext()) {
            FeedbackRes f = (FeedbackRes) it.next();            
            sum = sum + f.getValutazione_guida();
            cont1=cont1+1;
            sum2 = sum2 + f.getValutazione_puntualita();
            cont2=cont2+1;
            sum3 = sum3 + f.getValutazione_disponibilita();
            cont3=cont3+1;
        }
        media_valutazione_1=(int) round(sum/cont1);
        media_valutazione_2=(int) round(sum2/cont2);        
        media_valutazione_3=(int) round(sum3/cont3);
        if(cont1!=0){
        media_valutazione=(sum+sum2+sum3)/(cont1+cont2+cont3);
        }
    }

    public int getMedia_valutazione_1() {
        return media_valutazione_1;
    }

    public void setMedia_valutazione_1(int media_valutazione_1) {
        this.media_valutazione_1 = media_valutazione_1;
    }

    public int getMedia_valutazione_2() {
        return media_valutazione_2;
    }

    public void setMedia_valutazione_2(int media_valutazione_2) {
        this.media_valutazione_2 = media_valutazione_2;
    }

    public int getMedia_valutazione_3() {
        return media_valutazione_3;
    }

    public void setMedia_valutazione_3(int media_valutazione_3) {
        this.media_valutazione_3 = media_valutazione_3;
    }

    public double getMedia_valutazione() {
        return media_valutazione;
    }

    public void setMedia_valutazione(double media_valutazione) {
        this.media_valutazione = media_valutazione;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }
        
    public FeedbackRes(ResultSet rs) throws SQLException {
        super(rs);
    }

    public List<FeedbackRes> getListaFeedback() {
        return listaFeedback;
    }

    public void setListaFeedback(List<FeedbackRes> listaFeedback) {
        this.listaFeedback = listaFeedback;
    }


    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
}
