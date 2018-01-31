/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class Relazione {
    private String utente_1;
    private String utente_2;
    private int approvato;
    private int raccomandato;
    private int da_valutare;
    private String da_valutare_data;

    public Relazione(ResultSet rs) throws SQLException {
        this.utente_1=rs.getString("utente_1");
        this.utente_2=rs.getString("utente_2");
        this.approvato=rs.getInt("approvato");
        this.raccomandato=rs.getInt("raccomandato");
        this.da_valutare=rs.getInt("da_valutare");
        this.da_valutare_data = rs.getString("da_valutare_data");
    }

    public String getUtente_1() {
        return utente_1;
    }

    public void setUtente_1(String utente_1) {
        this.utente_1 = utente_1;
    }

    public String getUtente_2() {
        return utente_2;
    }

    public void setUtente_2(String utente_2) {
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

    public String getDa_valutare_data() {
        return da_valutare_data;
    }

    public void setDa_valutare_data(String da_valutare_data) {
        this.da_valutare_data = da_valutare_data;
    }

}
