/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leo
 */

@XmlRootElement

public class Utente implements Cloneable{
    @Context
SecurityContext securityContext;
    @XmlElement public String id;
    @XmlElement public String email;
    @XmlElement public String nome;
    @XmlElement public String cognome;
    @XmlElement public String anno_nascita;
    @XmlElement public String professione;
    @XmlElement public String telefono1;
    String telefono2;
    @XmlElement public String password;
    @XmlElement public String data;
    @XmlElement public String biografia;
    @XmlElement public String foto_utente;
    @XmlElement public String image_path;
    @XmlElement public String sesso;
    int fumare;
    int animali;
    int tipo;
    int media_feedback;
    
    
    
    
    public Utente() {
    }

    public Utente(ResultSet rs) throws SQLException {
        this.email = rs.getString("u.email");
        this.nome = rs.getString("u.nome");
        this.cognome = rs.getString("u.cognome");
        this.anno_nascita = rs.getString("u.anno_nascita");
        this.telefono1 = rs.getString("u.telefono1");
        this.telefono2 = rs.getString("u.telefono1");
        this.biografia = rs.getString("u.biografia");
        this.foto_utente = rs.getString("u.foto_utente");
        this.image_path = rs.getString("u.image_path");
        this.professione = rs.getString("u.professione");
        this.password = rs.getString("u.psw");
        this.sesso = rs.getString("u.sesso");
        this.animali = rs.getInt("u.animali");
        this.fumare = rs.getInt("u.fumare");
        this.tipo = rs.getInt("u.tipo");
        this.id=rs.getString("u.id");
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getAnno_nascita() {
        return anno_nascita;
    }

    public void setAnno_nascita(String anno_nascita) {
        this.anno_nascita = anno_nascita;
    }

    public String getProfessione() {
        return professione;
    }

    public void setProfessione(String professione) {
        this.professione = professione;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getFoto_utente() {
        return foto_utente;
    }

    public void setFoto_utente(String foto_utente) {
        this.foto_utente = foto_utente;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public int getFumare() {
        return fumare;
    }

    public void setFumare(int fumare) {
        this.fumare = fumare;
    }

    public int getAnimali() {
        return animali;
    }

    public void setAnimali(int animali) {
        this.animali = animali;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getMedia_feedback() {
        return media_feedback;
    }

    public void setMedia_feedback(int media_feedback) {
        this.media_feedback = media_feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Utente clone() throws CloneNotSupportedException {
        return (Utente) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
