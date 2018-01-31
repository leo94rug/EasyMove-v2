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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Viaggio_autoRes extends Viaggio_auto{
    Tratta_auto tratta_auto;
    List<Tratta_auto> tratte_auto;
    Utente utente;
    String id_partenza;
    String id_arrivo;

    public Viaggio_autoRes(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    public Viaggio_autoRes(ResultSet rs) throws SQLException {
        super(rs);
    }

    public Viaggio_autoRes() {
        super();
    }

    
    public void setTratte_auto(List<Tratta_auto> tratte_auto) {
        this.tratte_auto = tratte_auto;
    }

    public void setTratta_auto(Tratta_auto tratta_auto) {
        this.tratta_auto = tratta_auto;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public void setId_partenza(String id_partenza) {
        this.id_partenza = id_partenza;
    }

    public void setId_arrivo(String id_arrivo) {
        this.id_arrivo = id_arrivo;
    }

    public List<Tratta_auto> getTratte_auto() {
        return tratte_auto;
    }

    public Tratta_auto getTratta_auto() {
        return tratta_auto;
    }

    public Utente getUtente() {
        return utente;
    }

    public String getId_partenza() {
        return id_partenza;
    }

    public String getId_arrivo() {
        return id_arrivo;
    }

}
