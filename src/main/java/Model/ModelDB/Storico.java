/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author leo
 */
public class Storico {

    private int id;
    private Timestamp datesql;
    private String descrizione;
    private String date;

    public Storico(ResultSet rs) throws SQLException {
        this.id = rs.getInt("s.id");
        this.datesql = rs.getTimestamp("s.data");
        this.descrizione = rs.getString("s.descrizione");
        this.date = datesql.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDatesql(Timestamp datesql) {
        this.datesql = datesql;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Timestamp getDatesql() {
        return datesql;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getDate() {
        return date;
    }

}
