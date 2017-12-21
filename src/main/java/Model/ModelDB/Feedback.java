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
public class Feedback {

    private int id;
    private int valutazione_guida;
    private int valutazione_puntualita;
    private int valutazione_disponibilita;
    private String testo;
    private Timestamp date;
    private int utente_recensore;
    private int utente_recensito;

    public Feedback() {
    }

    public Feedback(String testo, int utente_recensito,  int utente_recensore,int valutazione_guida, int valutazione_puntualita, int valutazione_disponibilita) {
        this.valutazione_guida = valutazione_guida;
        this.valutazione_puntualita = valutazione_puntualita;
        this.valutazione_disponibilita = valutazione_disponibilita;
        this.testo = testo;
        this.utente_recensore = utente_recensore;
        this.utente_recensito = utente_recensito;
    }

    public Feedback(ResultSet rs) throws SQLException {
        this.id = rs.getInt("f.id");
        this.testo = rs.getString("f.testo");
        this.valutazione_guida = rs.getInt("f.valutazione_guida");
        this.valutazione_puntualita = rs.getInt("f.valutazione_puntualita");
        this.valutazione_disponibilita = rs.getInt("f.valutazione_disponibilita");
        this.date = rs.getTimestamp("f.data");
        this.utente_recensito = rs.getInt("f.utente_recensito");
        this.utente_recensore = rs.getInt("utente_recensore");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValutazione_guida() {
        return valutazione_guida;
    }

    public void setValutazione_guida(int valutazione_guida) {
        this.valutazione_guida = valutazione_guida;
    }

    public int getValutazione_puntualita() {
        return valutazione_puntualita;
    }

    public void setValutazione_puntualita(int valutazione_puntualita) {
        this.valutazione_puntualita = valutazione_puntualita;
    }

    public int getValutazione_disponibilita() {
        return valutazione_disponibilita;
    }

    public void setValutazione_disponibilita(int valutazione_disponibilita) {
        this.valutazione_disponibilita = valutazione_disponibilita;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getUtente_recensore() {
        return utente_recensore;
    }

    public void setUtente_recensore(int utente_recensore) {
        this.utente_recensore = utente_recensore;
    }

    public int getUtente_recensito() {
        return utente_recensito;
    }

    public void setUtente_recensito(int utente_recensito) {
        this.utente_recensito = utente_recensito;
    }
    
}
