/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Tratta_auto {

    int id;
    java.sql.Timestamp orario_partenza;
    String orario_partenza_string;
    int enumerazione;
    Timestamp data;
    int viaggio_fk;
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
        this.id = rs.getInt("t.id");
        this.orario_partenza = rs.getTimestamp("t.orario_partenza");
        this.orario_partenza_string=orario_partenza.toString();
        this.data = rs.getTimestamp("t.data");
        this.enumerazione = rs.getInt("t.enumerazione");
        this.viaggio_fk = rs.getInt("t.viaggio_fk");
        this.prezzo = rs.getInt("t.prezzo");
        this.distanza = rs.getInt("t.distanza");
        this.posti = rs.getInt("t.posti");
        this.lat_partenza = rs.getDouble("t.lat_partenza");
        this.lng_partenza = rs.getDouble("t.lng_partenza");
        this.lat_arrivo = rs.getDouble("t.lat_arrivo");
        this.lng_arrivo = rs.getDouble("t.lng_arrivo");
        this.denominazione_partenza = rs.getString("t.denominazione_partenza");
        this.denominazione_arrivo = rs.getString("t.denominazione_arrivo");

    }

    public Tratta_auto(JSONObject jsonObject) throws JSONException {
        long date = jsonObject.getLong("orario_partenza");
        orario_partenza = new Timestamp(date);
        enumerazione = jsonObject.getInt("enumerazione");
        prezzo = jsonObject.getInt("prezzo");
        distanza = jsonObject.getInt("distanza");
        posti = jsonObject.getInt("posti");
        lng_partenza = jsonObject.getDouble("lng_partenza");
        lat_partenza = jsonObject.getDouble("lat_partenza");
        lng_arrivo = jsonObject.getDouble("lng_arrivo");
        lat_arrivo = jsonObject.getDouble("lat_arrivo");
        denominazione_partenza = jsonObject.getString("denominazione_partenza");
        denominazione_arrivo = jsonObject.getString("denominazione_arrivo");
    }


    public void addPrezzo(int add){
        this.prezzo=this.prezzo+add;
    }        
    public void addDistanza(int add){
        this.distanza=this.distanza+add;
    }    
    public void addPosti(int posti){
        if(posti<this.posti){
            this.posti=posti;
        }
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setOrario_partenza(Timestamp orario_partenza) {
        this.orario_partenza = orario_partenza;
    }

    public void setEnumerazione(int enumerazione) {
        this.enumerazione = enumerazione;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setViaggio_fk(int viaggio_fk) {
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

    public int getId() {
        return id;
    }

    public Timestamp getOrario_partenza() {
        return orario_partenza;
    }

    public int getEnumerazione() {
        return enumerazione;
    }

    public Timestamp getData() {
        return data;
    }

    public int getViaggio_fk() {
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
