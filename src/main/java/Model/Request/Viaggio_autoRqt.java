/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Viaggio_auto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leo
 */
public class Viaggio_autoRqt extends Viaggio_auto {

    public List<Tratta_auto> andata;
//    public List<Tratta_auto> ritorno;
//
    public List<Tratta_auto> getAndata() {
        return andata;
    }

    public void setAndata(List<Tratta_auto> andata) {
        this.andata = andata;
    }

//    public List<Tratta_auto> getRitorno() {
//        return ritorno;
//    }
//
//    public void setRitorno(List<Tratta_auto> ritorno) {
//        this.ritorno = ritorno;
//    }

}
