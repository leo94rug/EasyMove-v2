/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Ricerca {

    private int id;
    private double lat_p;
    private double lat_a;
    private double lng_p;
    private double lng_a;
    private Date date;
    private Timestamp date_search;
    private int distanza;
    private int distanza_tra;
    private int tipo;
    private String utente_fk;
    private int cambio;

    public Ricerca(JSONObject jsonObject) throws JSONException {
        long data = jsonObject.getLong("date");
        this.date = new Date(data);
        distanza = jsonObject.getInt("distanza");
        distanza_tra = jsonObject.getInt("distanza_tra");
        cambio = jsonObject.getInt("cambio");
        tipo = jsonObject.getInt("tipo");
        lng_p = jsonObject.getDouble("lngp");
        lat_p = jsonObject.getDouble("latp");
        lng_a = jsonObject.getDouble("lnga");
        lat_a = jsonObject.getDouble("lata");
        if (jsonObject.has("utente_fk")) {
            utente_fk = jsonObject.getString("utente_fk");
        }
    }

    public void insert(DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO ricerca("
                    + "date, "
                    + "distanza, "
                    + "distanza_tra, "
                    + "cambio, "
                    + "tipo, "
                    + "lngp, "
                    + "latp, "
                    + "lnga,"
                    + "lata,"
                    + "utente_fk) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, date);
            ps.setInt(2, distanza);
            ps.setInt(3, distanza_tra);
            ps.setInt(4, cambio);
            ps.setInt(5, tipo);
            ps.setDouble(6, lng_p);
            ps.setDouble(7, lat_p);
            ps.setDouble(8, lng_a);
            ps.setDouble(9, lat_a);
            ps.setString(10, utente_fk);
            int i = ps.executeUpdate();
        }
    }

    public void setDistanza_tra(int distanza_tra) {
        this.distanza_tra = distanza_tra;
    }

    public int getDistanza_tra() {
        return distanza_tra;
    }

    public void setId(int id) {
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate_search(Timestamp date_search) {
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

    public int getId() {
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

    public Date getDate() {
        return date;
    }

    public Timestamp getDate_search() {
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
