/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Auto;

/**
 *
 * @author leo
 */
public class AutoRqt extends Auto{
    
    public AutoRqt(String id, String modello, String marca, String colore, String utente_fk) {
        super(id, modello, marca, colore, utente_fk);
    }

    public AutoRqt() {
        super();
    }
    
}
