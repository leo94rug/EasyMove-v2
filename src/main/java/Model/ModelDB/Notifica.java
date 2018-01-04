/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author leo
 */
public class Notifica  {

    private int id;
    private int mittente;
    private int destinatario;
    private int tipologia;
    private String messaggio;
    private Timestamp data;
    private Timestamp fine_validita;
    private Timestamp inizio_validita;
    private int id_viaggio;
    private String nome_viaggio;
    private String nome_mittente;
    private int stato;
    private int posti;
    private int posti_da_prenotare;
    private int id_partenza;
    private int id_arrivo;
    private String nome_destinatario;

    public Notifica(){};

    public Notifica(ResultSet rs) throws SQLException {
        this.id = rs.getInt("n.id");
        this.mittente = rs.getInt("n.mittente");
        this.destinatario = rs.getInt("n.destinatario");
        this.tipologia = rs.getInt("n.tipologia");
        this.messaggio = rs.getString("n.messaggio");
        this.data = rs.getTimestamp("n.data");
        this.nome_viaggio = rs.getString("n.nome_viaggio");
        this.id_viaggio = rs.getInt("n.id_viaggio");
        this.nome_mittente = rs.getString("n.nome_mittente");
        this.stato = rs.getInt("n.stato");
        this.fine_validita = rs.getTimestamp("n.fine_validita");
        this.inizio_validita = rs.getTimestamp("n.inizio_validita");
        this.posti = rs.getInt("n.posti");
        this.posti = rs.getInt("n.posti_da_prenotare");
        this.id_partenza = rs.getInt("n.id_partenza");
        this.id_arrivo = rs.getInt("n.id_arrivo");
        this.nome_destinatario=rs.getString("n.nome_destinatario");

    }

    public void setNome_destinatario(String nome_destinatario) {
        this.nome_destinatario = nome_destinatario;
    }

    public void setPosti_da_prenotare(int posti_da_prenotare) {
        this.posti_da_prenotare = posti_da_prenotare;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMittente(int mittente) {
        this.mittente = mittente;
    }

    public void setDestinatario(int destinatario) {
        this.destinatario = destinatario;
    }

    public void setTipologia(int tipologia) {
        this.tipologia = tipologia;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setFine_validita(Timestamp fine_validita) {
        this.fine_validita = fine_validita;
    }

    public void setInizio_validita(Timestamp inizio_validita) {
        this.inizio_validita = inizio_validita;
    }

    public void setId_viaggio(int id_viaggio) {
        this.id_viaggio = id_viaggio;
    }

    public void setNome_viaggio(String nome_viaggio) {
        this.nome_viaggio = nome_viaggio;
    }

    public void setNome_mittente(String nome_mittente) {
        this.nome_mittente = nome_mittente;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

    public void setId_partenza(int id_partenza) {
        this.id_partenza = id_partenza;
    }

    public void setId_arrivo(int id_arrivo) {
        this.id_arrivo = id_arrivo;
    }

    public String getNome_destinatario() {
        return nome_destinatario;
    }
    
    public int getPosti_da_prenotare() {
        return posti_da_prenotare;
    }

    public int getId() {
        return id;
    }

    public int getMittente() {
        return mittente;
    }

    public int getDestinatario() {
        return destinatario;
    }

    public int getTipologia() {
        return tipologia;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public Timestamp getData() {
        return data;
    }

    public Timestamp getFine_validita() {
        return fine_validita;
    }

    public Timestamp getInizio_validita() {
        return inizio_validita;
    }

    public int getId_viaggio() {
        return id_viaggio;
    }

    public String getNome_viaggio() {
        return nome_viaggio;
    }

    public String getNome_mittente() {
        return nome_mittente;
    }

    public int getStato() {
        return stato;
    }

    public int getPosti() {
        return posti;
    }

    public int getId_partenza() {
        return id_partenza;
    }

    public int getId_arrivo() {
        return id_arrivo;
    }

    
    public void setInizio_validitaFromLong(long aInt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aInt);
        Timestamp inizio_validita_1= new Timestamp(calendar.getTimeInMillis());
        this.setInizio_validita(inizio_validita_1);
    }

    public void setFine_validitaFromLong(long aInt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aInt);
        Timestamp fine_validita_1= new Timestamp(calendar.getTimeInMillis());
        this.setFine_validita(fine_validita_1);    }
}
