/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Notifica;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author leo
 */
public class NotificaRes extends Notifica{
    private int id_tipologia;
    private String valore;
    private String descrizione;
    private String bottone_1;
    private String bottone_2;
    private String titolo;
    private String email;
    private String foto_utente;

    public NotificaRes(ResultSet rs) throws SQLException {
        super(rs);
        this.id_tipologia=rs.getInt("nt.id_tipologia");
        this.valore=rs.getString("nt.valore");
        this.descrizione=rs.getString("nt.descrizione");
        this.bottone_1=rs.getString("nt.bottone_1");
        this.bottone_2=rs.getString("nt.bottone_2");
        this.titolo=rs.getString("nt.titolo");
        this.email=rs.getString("u.email");
        this.foto_utente=rs.getString("u.foto_utente");
    }

    public void setId_tipologia(int id_tipologia) {
        this.id_tipologia = id_tipologia;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setBottone_1(String bottone_1) {
        this.bottone_1 = bottone_1;
    }

    public void setBottone_2(String bottone_2) {
        this.bottone_2 = bottone_2;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFoto_utente(String foto_utente) {
        this.foto_utente = foto_utente;
    }

    public int getId_tipologia() {
        return id_tipologia;
    }

    public String getValore() {
        return valore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getBottone_1() {
        return bottone_1;
    }

    public String getBottone_2() {
        return bottone_2;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto_utente() {
        return foto_utente;
    }
    
}
