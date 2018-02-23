/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.ModelDB.Viaggio_auto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author leo
 */
public class Tratta_autoRes extends Tratta_auto{
    List<Utente> passeggeri;
    Utente utente;
    Viaggio_auto viaggio_auto;

    public Tratta_autoRes(ResultSet rs) throws SQLException {
        super(rs);
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Viaggio_auto getViaggio_auto() {
        return viaggio_auto;
    }

    public void setViaggio_auto(Viaggio_auto viaggio_auto) {
        this.viaggio_auto = viaggio_auto;
    }

    public List<Utente> getPasseggeri() {
        return passeggeri;
    }

    public void setPasseggeri(List<Utente> passeggeri) {
        this.passeggeri = passeggeri;
    }

}
