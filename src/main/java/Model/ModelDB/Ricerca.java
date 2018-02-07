/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leo
 */
@XmlRootElement

public class Ricerca {

    private String id;
    @XmlElement public double lat_p;
    @XmlElement public double lat_a;
    @XmlElement public double lng_p;
    @XmlElement public double lng_a;
    private String date;
    @XmlElement public String date_search;
    @XmlElement public int distanza;
    @XmlElement public int distanza_tra;
    @XmlElement public int tipo;
    @XmlElement public String utente_fk;
    @XmlElement public int cambio;

    public void setDistanza_tra(int distanza_tra) {
        this.distanza_tra = distanza_tra;
    }

    public int getDistanza_tra() {
        return distanza_tra;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat_p(double lat_p) {
        this.lat_p = lat_p;
    }

    public void setLat_a(double lat_a) {
        this.lat_a = lat_a;
    }

    public void setLng_p(double lng_p) {
        this.lng_p = lng_p;
    }

    public void setLng_a(double lng_a) {
        this.lng_a = lng_a;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate_search(String date_search) {
        this.date_search = date_search;
    }

    public void setDistanza(int distanza) {
        this.distanza = distanza;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setUtente_fk(String utente_fk) {
        this.utente_fk = utente_fk;
    }

    public void setCambio(int cambio) {
        this.cambio = cambio;
    }

    public String getId() {
        return id;
    }

    public double getLat_p() {
        return lat_p;
    }

    public double getLat_a() {
        return lat_a;
    }

    public double getLng_p() {
        return lng_p;
    }

    public double getLng_a() {
        return lng_a;
    }

    public String getDate() {
        return date;
    }

    public String getDate_search() {
        return date_search;
    }

    public int getDistanza() {
        return distanza;
    }

    public int getTipo() {
        return tipo;
    }

    public String getUtente_fk() {
        return utente_fk;
    }

    public int getCambio() {
        return cambio;
    }

}
