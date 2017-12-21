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
public class Relazione {
    private int id;
    private int utente_1;
    private int utente_2;
    private int approvato;
    private int raccomandato;
    private int da_valutare;
    private Timestamp da_valutare_data;

    public Relazione(ResultSet rs) throws SQLException {
        this.id=rs.getInt("id");
        this.utente_1=rs.getInt("utente_1");
        this.utente_2=rs.getInt("utente_2");
        this.approvato=rs.getInt("approvato");
        this.raccomandato=rs.getInt("raccomandato");
        this.da_valutare=rs.getInt("da_valutare");
        this.da_valutare_data = rs.getTimestamp("da_valutare_data");
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtente_1() {
        return utente_1;
    }

    public void setUtente_1(int utente_1) {
        this.utente_1 = utente_1;
    }

    public int getUtente_2() {
        return utente_2;
    }

    public void setUtente_2(int utente_2) {
        this.utente_2 = utente_2;
    }

    public int getApprovato() {
        return approvato;
    }

    public void setApprovato(int approvato) {
        this.approvato = approvato;
    }

    public int getRaccomandato() {
        return raccomandato;
    }

    public void setRaccomandato(int raccomandato) {
        this.raccomandato = raccomandato;
    }

    public int getDa_valutare() {
        return da_valutare;
    }

    public void setDa_valutare(int da_valutare) {
        this.da_valutare = da_valutare;
    }

    public Timestamp getDa_valutare_data() {
        return da_valutare_data;
    }

    public void setDa_valutare_data(Timestamp da_valutare_data) {
        this.da_valutare_data = da_valutare_data;
    }

}
