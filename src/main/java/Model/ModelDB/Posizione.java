/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Posizione {

    private int id;
    private String nome;
    private String nome_fermata;
    private String locality;
    private String route;
    private String premise;
    private String aal3;
    private String aal2;
    private String aal1;
    private String country;
    private String formatted_address;
    private double lat;
    private double lng;
    private int storico_fk;
    private int error_type;

    public Posizione(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("id")) {
            id = jsonObject.getInt("id");
        }
        if (jsonObject.has("nome_fermata")) {
            nome_fermata = jsonObject.getString("nome_fermata");
        }
        if (jsonObject.has("formatted_address")) {
            formatted_address = jsonObject.getString("formatted_address");
        }        
        if (jsonObject.has("nome")) {
            nome = jsonObject.getString("nome");
        }
        if (jsonObject.has("locality")) {
            locality = jsonObject.getString("locality");
        }
        if (jsonObject.has("premise")) {
            premise = jsonObject.getString("premise");
        }        
        if (jsonObject.has("aal1")) {
            aal1 = jsonObject.getString("aal1");
        }
        if (jsonObject.has("aal2")) {
            aal2 = jsonObject.getString("aal2");
        }
        if (jsonObject.has("aal3")) {
            aal3 = jsonObject.getString("aal3");
        }        
        if (jsonObject.has("lat")) {
            lat = jsonObject.getDouble("lat");
        }
        if (jsonObject.has("country")) {
            country = jsonObject.getString("country");
        }
        if (jsonObject.has("lng")) {
            lng = jsonObject.getDouble("lng");
        }        
        if (jsonObject.has("route")) {
            route = jsonObject.getString("route");
        }
        if (jsonObject.has("storico_fk")) {
            storico_fk = jsonObject.getInt("storico_fk");
        }        
        if (jsonObject.has("error_type")) {
            error_type = jsonObject.getInt("error_type");
        }

    }
    public Posizione(String nome) {
        this.nome = nome;
    }
    public Posizione(ResultSet rs) throws SQLException {
        this.id = rs.getInt("p.id");
        this.nome = rs.getString("p.nome");
        this.nome_fermata = rs.getString("p.nome_fermata");
        this.locality = rs.getString("p.locality");
        this.route = rs.getString("p.route");
        this.premise = rs.getString("p.premise");
        this.aal3 = rs.getString("p.aal3");
        this.aal2 = rs.getString("p.aal2");
        this.aal1 = rs.getString("p.aal1");
        this.country = rs.getString("p.country");
        this.formatted_address = rs.getString("p.formatted_address");
        this.lat = rs.getDouble("p.lat");
        this.lng = rs.getDouble("p.lon");
        this.storico_fk=rs.getInt("p.storico_fk");
        this.error_type=rs.getInt("p.error_type");
    }

    public Posizione() {
    }

    public Posizione(String nome, double lat, double lng) {
        this.nome = nome;
        this.lat = lat;
        this.lng = lng;
    }

    public void setUnknown(String tipo, String longname) {
        if (tipo.equalsIgnoreCase("route")) {
            this.setRoute(longname);
        }
        if (tipo.equalsIgnoreCase("locality")) {
            this.setLocality(longname);
        }
        if (tipo.equalsIgnoreCase("premise")) {
            this.setPremise(longname);
        }
        if (tipo.equalsIgnoreCase("administrative_area_level_3")) {
            this.setAal3(longname);
        }
        if (tipo.equalsIgnoreCase("administrative_area_level_2")) {
            this.setAal2(longname);
        }
        if (tipo.equalsIgnoreCase("administrative_area_level_1")) {
            this.setAal1(longname);
        }
        if (tipo.equalsIgnoreCase("country")) {
            this.setCountry(longname);
        }
    }

    public void setStorico_fk(int storico_fk) {
        this.storico_fk = storico_fk;
    }

    public int getStorico_fk() {
        return storico_fk;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNome_fermata(String nome_fermata) {
        this.nome_fermata = nome_fermata;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public void setAal3(String aal3) {
        this.aal3 = aal3;
    }

    public void setAal2(String aal2) {
        this.aal2 = aal2;
    }

    public void setAal1(String aal1) {
        this.aal1 = aal1;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setError_type(int error_type) {
        this.error_type = error_type;
    }

    public int getError_type() {
        return error_type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lng = lon;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNome_fermata() {
        return nome_fermata;
    }

    public String getLocality() {
        return locality;
    }

    public String getRoute() {
        return route;
    }

    public String getPremise() {
        return premise;
    }

    public String getAal3() {
        return aal3;
    }

    public String getAal2() {
        return aal2;
    }

    public String getAal1() {
        return aal1;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lng;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
