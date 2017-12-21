/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Response;

import Model.ModelDB.Utente;
import Utilita.DatesConversion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class UtenteRes extends Utente{
    //public String token;
    public int eta;
    public String anno_nascita_string;
    
    public UtenteRes (ResultSet rs/*,String token*/) throws SQLException{
        super(rs);
        //this.token=token;
        this.eta=DatesConversion.calcoloEta(super.getAnno_nascita());
        this.anno_nascita_string=super.getAnno_nascita().toString();
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
    public void calcolaEta(){
        this.eta=DatesConversion.calcoloEta(super.getAnno_nascita());
    }
    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }


    
}
