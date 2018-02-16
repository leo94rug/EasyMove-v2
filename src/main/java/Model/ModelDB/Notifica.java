/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import Model.Request.NotificaRqt;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class Notifica implements Cloneable{

    private String id;
    private String mittente;
    private String destinatario;
    private int tipologia;
    private String messaggio;
    private String data;
    private String fine_validita;
    private String inizio_validita;
    private String id_viaggio;
    private String nome_viaggio;
    private String nome_mittente;
    private int stato;
    private int posti;
    private int posti_da_prenotare;
    private String id_partenza;
    private String id_arrivo;
    private String nome_destinatario;

    public Notifica(String id, String mittente, String destinatario, int tipologia, String messaggio, String data, String fine_validita, String inizio_validita, String id_viaggio, String nome_viaggio, String nome_mittente, int stato, int posti, int posti_da_prenotare, String id_partenza, String id_arrivo, String nome_destinatario) {
        this.id = id;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.tipologia = tipologia;
        this.messaggio = messaggio;
        this.data = data;
        this.fine_validita = fine_validita;
        this.inizio_validita = inizio_validita;
        this.id_viaggio = id_viaggio;
        this.nome_viaggio = nome_viaggio;
        this.nome_mittente = nome_mittente;
        this.stato = stato;
        this.posti = posti;
        this.posti_da_prenotare = posti_da_prenotare;
        this.id_partenza = id_partenza;
        this.id_arrivo = id_arrivo;
        this.nome_destinatario = nome_destinatario;
    }
    public Notifica(String id, String mittente, String destinatario, int tipologia, String data, String fine_validita, String inizio_validita, String nome_mittente, int stato, String nome_destinatario) {
        this.id = id;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.tipologia = tipologia;
        this.data = data;
        this.fine_validita = fine_validita;
        this.inizio_validita = inizio_validita;
        this.nome_mittente = nome_mittente;
        this.stato = stato;
        this.nome_destinatario = nome_destinatario;
    }
    public Notifica() {
    }

    public Notifica(ResultSet rs) throws SQLException {
        this.id = rs.getString("n.id");
        this.mittente = rs.getString("n.mittente");
        this.destinatario = rs.getString("n.destinatario");
        this.tipologia = rs.getInt("n.tipologia");
        this.messaggio = rs.getString("n.messaggio");
        this.data = rs.getString("n.data");
        this.nome_viaggio = rs.getString("n.nome_viaggio");
        this.id_viaggio = rs.getString("n.id_viaggio");
        this.nome_mittente = rs.getString("n.nome_mittente");
        this.stato = rs.getInt("n.stato");
        this.fine_validita = rs.getString("n.fine_validita");
        this.inizio_validita = rs.getString("n.inizio_validita");
        this.posti = rs.getInt("n.posti");
        this.posti = rs.getInt("n.posti_da_prenotare");
        this.id_partenza = rs.getString("n.id_partenza");
        this.id_arrivo = rs.getString("n.id_arrivo");
        this.nome_destinatario = rs.getString("n.nome_destinatario");
    }

    public void setNome_destinatario(String nome_destinatario) {
        this.nome_destinatario = nome_destinatario;
    }

    public void setPosti_da_prenotare(int posti_da_prenotare) {
        this.posti_da_prenotare = posti_da_prenotare;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setTipologia(int tipologia) {
        this.tipologia = tipologia;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setFine_validita(String fine_validita) {
        this.fine_validita = fine_validita;
    }

    public void setInizio_validita(String inizio_validita) {
        this.inizio_validita = inizio_validita;
    }

    public void setId_viaggio(String id_viaggio) {
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

    public void setId_partenza(String id_partenza) {
        this.id_partenza = id_partenza;
    }

    public void setId_arrivo(String id_arrivo) {
        this.id_arrivo = id_arrivo;
    }

    public String getNome_destinatario() {
        return nome_destinatario;
    }

    public int getPosti_da_prenotare() {
        return posti_da_prenotare;
    }

    public String getId() {
        return id;
    }

    public String getMittente() {
        return mittente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public int getTipologia() {
        return tipologia;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public String getData() {
        return data;
    }

    public String getFine_validita() {
        return fine_validita;
    }

    public String getInizio_validita() {
        return inizio_validita;
    }

    public String getId_viaggio() {
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

    public String getId_partenza() {
        return id_partenza;
    }

    public String getId_arrivo() {
        return id_arrivo;
    }
    @Override
    public NotificaRqt clone() throws CloneNotSupportedException {
        return (NotificaRqt) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
