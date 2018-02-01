/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import static DatabaseConstants.Viaggio_auto.DESCRIZIONE;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Viaggio_auto {

    String id;
    String descrizione;
    String ritardo_max;
    String bagaglio_max;
    String disponibilita_deviazioni;
    String utente_fk;
    String auto;
    String data;
    int tipologia;
    public Viaggio_auto(){}
    
    public Viaggio_auto(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("descrizione")) {
            descrizione = jsonObject.getString("descrizione");
        }
        ritardo_max = jsonObject.getString("ritardo_max");
        bagaglio_max = jsonObject.getString("bagaglio_max");
        disponibilita_deviazioni = jsonObject.getString("disponibilita_deviazioni");
        utente_fk = jsonObject.getString("utente_fk");
        auto = jsonObject.getString("auto");
        tipologia = jsonObject.getInt("tipologia");
    }
    public Viaggio_auto(ResultSet rs) throws SQLException {
        this.id = rs.getString("vi.id");
        this.tipologia = rs.getInt("vi.tipologia");
        this.ritardo_max = rs.getString("vi.ritardo_max");
        this.bagaglio_max = rs.getString("vi.bagaglio_max");
        this.data = rs.getString("vi.data");
        this.disponibilita_deviazioni = rs.getString("vi.disponibilita_deviazioni");
        this.utente_fk = rs.getString("vi.utente_fk");
        this.auto = rs.getString("vi.auto");
        this.descrizione = rs.getString("vi." + DESCRIZIONE);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipologia(int tipologia) {
        this.tipologia = tipologia;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setRitardo_max(String ritardo_max) {
        this.ritardo_max = ritardo_max;
    }

    public void setBagaglio_max(String bagaglio_max) {
        this.bagaglio_max = bagaglio_max;
    }

    public void setDisponibilita_deviazioni(String disponibilita_deviazioni) {
        this.disponibilita_deviazioni = disponibilita_deviazioni;
    }

    public void setUtente_fk(String utente_fk) {
        this.utente_fk = utente_fk;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getRitardo_max() {
        return ritardo_max;
    }

    public String getBagaglio_max() {
        return bagaglio_max;
    }

    public String getDisponibilita_deviazioni() {
        return disponibilita_deviazioni;
    }

    public String getUtente_fk() {
        return utente_fk;
    }

    public String getAuto() {
        return auto;
    }

    public int getTipologia() {
        return tipologia;
    }

}
