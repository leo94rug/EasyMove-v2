/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author leo
 */
public class Tratta_autoRes extends Tratta_auto{
    List<Utente> utente;

    public Tratta_autoRes(ResultSet rs) throws SQLException {
        super(rs);
    }

    public List<Utente> getUtente() {
        return utente;
    }

    public void setUtente(List<Utente> utente) {
        this.utente = utente;
    }

}
