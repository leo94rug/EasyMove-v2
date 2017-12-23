/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Utente;
import Utilita.Crypt;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class UtenteRqt extends Utente {

    private String psw;

    public UtenteRqt(JSONObject obj) throws JSONException, Exception {
        super();
        if (obj.has("email")) {
            super.setEmail(obj.getString("email"));
        }
        if (obj.has("id")) {
            super.setId(obj.getInt("id"));
        }
        if (obj.has("psw")) {
            this.psw = Crypt.encrypt(obj.getString("psw"));
        }
        if (obj.has("professione")) {
            super.setProfessione(obj.getString("professione"));
        } else {
            super.setProfessione("Non specificata");
        }
        if (obj.has("biografia")) {
            super.setBiografia(obj.getString("biografia"));
        } else {
            super.setBiografia("L'utente non ha inserito una biografia");
        }
        if (obj.has("telefono1")) {
            super.setTelefono1(obj.getString("telefono1"));
        } else {
            super.setTelefono1("");
        }
        super.setNome(obj.getString("nome"));
        super.setCognome(obj.getString("cognome"));
        super.setSesso(obj.getString("sesso"));
        long longNum = obj.getLong("anno_nascita");
        super.setAnno_nascita(new Timestamp(longNum));
    }

    public String getPassword() {
        return psw;
    }

    public void setPassword(String password) {
        this.psw = password;
    }

}
