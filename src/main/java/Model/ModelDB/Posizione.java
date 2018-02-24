/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import static DatabaseConstants.Posizione.ADDRESS_COMPONENTS;
import static DatabaseConstants.Posizione.ID;
import static DatabaseConstants.Posizione.LAT;
import static DatabaseConstants.Posizione.LNG;
import static DatabaseConstants.Posizione.NOME;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class Posizione {
    public String id;
    public String nome;
    public double lat;
    public double lng;
    public String address_components;

    public Posizione(ResultSet rs) throws SQLException {
        this.id = rs.getString(ID);
        this.nome = rs.getString(NOME);
        this.lat = rs.getDouble(LAT);
        this.lng = rs.getDouble(LNG);
        this.address_components = rs.getString(ADDRESS_COMPONENTS);

    }    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress_components() {
        return address_components;
    }

    public void setAddress_components(String address_components) {
        this.address_components = address_components;
    }
    
}
