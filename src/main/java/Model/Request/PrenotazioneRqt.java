/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Prenotazione;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class PrenotazioneRqt extends Prenotazione{

    public PrenotazioneRqt(JSONObject obj) throws JSONException {
        super(obj);

    }

}
