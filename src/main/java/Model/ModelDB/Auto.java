/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leo
 */
@XmlRootElement

public class Auto {

    int id;
    String modello;
    String marca;
    String colore;
    int utente_fk;

    public Auto(int id, String modello, String marca, String colore, int utente_fk) {
        this.id = id;
        this.modello = modello;
        this.marca = marca;
        this.colore = colore;
        this.utente_fk = utente_fk;
    }

    public Auto(ResultSet rs) throws SQLException {
        this.id = rs.getInt("a.id");
        this.modello = rs.getString("a.modello");
        this.marca = rs.getString("a.marca");
        this.colore = rs.getString("a.colore");
        this.utente_fk = rs.getInt("a.utente_fk");  
    }
    public Auto(){
        
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setUtente_fk(int utente_fk) {
        this.utente_fk = utente_fk;
    }

    public int getId() {
        return id;
    }

    public String getModello() {
        return modello;
    }

    public String getMarca() {
        return marca;
    }

    public String getColore() {
        return colore;
    }

    public int getUtente_fk() {
        return utente_fk;
    }

}
