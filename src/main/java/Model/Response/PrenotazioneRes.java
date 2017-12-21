/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Prenotazione;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class PrenotazioneRes extends Prenotazione{
    Tratta_auto tratta_auto;
    Utente utente;
    public PrenotazioneRes(ResultSet rs) throws SQLException {
        super(rs);
    }

    public Tratta_auto getTratte_auto() {
        return tratta_auto;
    }

    public void setTratte_auto(Tratta_auto tratte_auto) {
        this.tratta_auto = tratte_auto;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
}
