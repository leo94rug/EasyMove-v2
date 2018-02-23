/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import static DatabaseConstants.Tratta_auto.DATA;
import static DatabaseConstants.Tratta_auto.DISTANZA;
import static DatabaseConstants.Tratta_auto.ENUMERAZIONE;
import static DatabaseConstants.Tratta_auto.ORARIO_PARTENZA;
import static DatabaseConstants.Tratta_auto.POSTI;
import static DatabaseConstants.Tratta_auto.PREZZO;
import static DatabaseConstants.Tratta_auto.VIAGGIO_FK;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Tratta_auto {

    String id;
    String orario_partenza;
    int enumerazione;
    String data;
    String viaggio_fk;
    int prezzo;
    int distanza;
    int posti;
    double lat_partenza;
    double lng_partenza;
    double lat_arrivo;
    double lng_arrivo;
    String denominazione_partenza;
    String denominazione_arrivo;

    public Tratta_auto(ResultSet rs) throws SQLException {
        this.id = rs.getString("t.id");
        this.orario_partenza = rs.getString("t."+ORARIO_PARTENZA);
        this.data = rs.getString("t."+DATA);
        this.enumerazione = rs.getInt("t."+ENUMERAZIONE);
        this.viaggio_fk = rs.getString("t."+VIAGGIO_FK);
        this.prezzo = rs.getInt("t."+PREZZO);
        this.distanza = rs.getInt("t."+DISTANZA);
        this.posti = rs.getInt("t."+POSTI);
        this.lat_partenza = rs.getDouble("t.lat_partenza");
        this.lng_partenza = rs.getDouble("t.lng_partenza");
        this.lat_arrivo = rs.getDouble("t.lat_arrivo");
        this.lng_arrivo = rs.getDouble("t.lng_arrivo");
        this.denominazione_partenza = rs.getString("t.denominazione_partenza");
        this.denominazione_arrivo = rs.getString("t.denominazione_arrivo");

    }

    public Tratta_auto(JSONObject jsonObject) throws JSONException {
        orario_partenza = jsonObject.getString(ORARIO_PARTENZA);
        enumerazione = jsonObject.getInt(ENUMERAZIONE);
        prezzo = jsonObject.getInt(PREZZO);
        distanza = jsonObject.getInt(DISTANZA);
        if (jsonObject.has(POSTI)) {
            posti = jsonObject.getInt(POSTI);
        }
        lng_partenza = jsonObject.getDouble("lng_partenza");
        lat_partenza = jsonObject.getDouble("lat_partenza");
        lng_arrivo = jsonObject.getDouble("lng_arrivo");
        lat_arrivo = jsonObject.getDouble("lat_arrivo");
        denominazione_partenza = jsonObject.getString("denominazione_partenza");
        denominazione_arrivo = jsonObject.getString("denominazione_arrivo");
    }

    public void addPrezzo(int add) {
        this.prezzo = this.prezzo + add;
    }

    public void addDistanza(int add) {
        this.distanza = this.distanza + add;
    }

    public void addPosti(int posti) {
        if (posti < this.posti) {
            this.posti = posti;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrario_partenza(String orario_partenza) {
        this.orario_partenza = orario_partenza;
    }

    public void setEnumerazione(int enumerazione) {
        this.enumerazione = enumerazione;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setViaggio_fk(String viaggio_fk) {
        this.viaggio_fk = viaggio_fk;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public void setDistanza(int distanza) {
        this.distanza = distanza;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

    public void setLat_partenza(double lat_partenza) {
        this.lat_partenza = lat_partenza;
    }

    public void setLng_partenza(double lng_partenza) {
        this.lng_partenza = lng_partenza;
    }

    public void setLat_arrivo(double lat_arrivo) {
        this.lat_arrivo = lat_arrivo;
    }

    public void setLng_arrivo(double lng_arrivo) {
        this.lng_arrivo = lng_arrivo;
    }

    public void setDenominazione_partenza(String denominazione_partenza) {
        this.denominazione_partenza = denominazione_partenza;
    }

    public void setDenominazione_arrivo(String denominazione_arrivo) {
        this.denominazione_arrivo = denominazione_arrivo;
    }

    public String getId() {
        return id;
    }

    public String getOrario_partenza() {
        return orario_partenza;
    }

    public int getEnumerazione() {
        return enumerazione;
    }

    public String getData() {
        return data;
    }

    public String getViaggio_fk() {
        return viaggio_fk;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public int getDistanza() {
        return distanza;
    }

    public int getPosti() {
        return posti;
    }

    public double getLat_partenza() {
        return lat_partenza;
    }

    public double getLng_partenza() {
        return lng_partenza;
    }

    public double getLat_arrivo() {
        return lat_arrivo;
    }

    public double getLng_arrivo() {
        return lng_arrivo;
    }

    public String getDenominazione_partenza() {
        return denominazione_partenza;
    }

    public String getDenominazione_arrivo() {
        return denominazione_arrivo;
    }

}
