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
public class Societa {
    private int id;
    private String nome;
    private String citta;
    private int tipologia;
    
        public Societa(ResultSet rs) throws SQLException {
        this.id = rs.getInt("s.id");
        this.nome = rs.getString("s.nome");
        this.citta = rs.getString("s.citta");
        this.tipologia = rs.getInt("s.tipologia");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setTipologia(int tipologia) {
        this.tipologia = tipologia;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCitta() {
        return citta;
    }

    public int getTipologia() {
        return tipologia;
    }
        
}
