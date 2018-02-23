/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import static DatabaseConstants.Viaggio_auto.AUTO;
import static DatabaseConstants.Viaggio_auto.BAGAGLIO_MAX;
import static DatabaseConstants.Viaggio_auto.DATA;
import static DatabaseConstants.Viaggio_auto.DESCRIZIONE;
import static DatabaseConstants.Viaggio_auto.DISPONIBILITA_DEVIAZIONI;
import static DatabaseConstants.Viaggio_auto.ID;
import static DatabaseConstants.Viaggio_auto.RITARDO_MAX;
import static DatabaseConstants.Viaggio_auto.TIPOLOGIA;
import static DatabaseConstants.Viaggio_auto.UTENTE_FK;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Viaggio_auto {

    public String id;
    public String descrizione;
    public String ritardo_max;
    public String bagaglio_max;
    public String disponibilita_deviazioni;
    public String utente_fk;
    public String auto;
    public String data;
    public int tipologia;

    public Viaggio_auto() {
    }

    public Viaggio_auto(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has(DESCRIZIONE)) {
            descrizione = jsonObject.getString(DESCRIZIONE);
        }
        if (jsonObject.has(RITARDO_MAX)) {
            ritardo_max = jsonObject.getString(RITARDO_MAX);
        }
        if (jsonObject.has(BAGAGLIO_MAX)) {
            bagaglio_max = jsonObject.getString(BAGAGLIO_MAX);
        }
        if (jsonObject.has(DISPONIBILITA_DEVIAZIONI)) {
            disponibilita_deviazioni = jsonObject.getString(DISPONIBILITA_DEVIAZIONI);
        }
        if (jsonObject.has(UTENTE_FK)) {
            utente_fk = jsonObject.getString(UTENTE_FK);
        }
        if (jsonObject.has(AUTO)) {
            auto = jsonObject.getString(AUTO);
        }
        if (jsonObject.has(TIPOLOGIA)) {
            tipologia = jsonObject.getInt(TIPOLOGIA);
        }
    }

    public Viaggio_auto(ResultSet rs) throws SQLException {
        this.id = rs.getString("vi." + ID);
        this.tipologia = rs.getInt("vi." + TIPOLOGIA);
        this.ritardo_max = rs.getString("vi." + RITARDO_MAX);
        this.bagaglio_max = rs.getString("vi." + BAGAGLIO_MAX);
        this.data = rs.getString("vi." + DATA);
        this.disponibilita_deviazioni = rs.getString("vi." + DISPONIBILITA_DEVIAZIONI);
        this.utente_fk = rs.getString("vi." + UTENTE_FK);
        this.auto = rs.getString("vi." + AUTO);
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
