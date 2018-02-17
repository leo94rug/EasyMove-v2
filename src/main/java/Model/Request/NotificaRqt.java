/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Request;

import Model.ModelDB.Notifica;
import Repository.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class NotificaRqt extends Notifica implements Cloneable {

    public NotificaRqt(JSONObject obj, Connection ds) throws JSONException, SQLException {
        super();
        UserRepository userRepository = new UserRepository(ds);
        if (obj.has("id")) {
            super.setId(obj.getString("id"));
        }
        super.setMessaggio(obj.getString("messaggio"));
        super.setMittente(obj.getString("mittente"));
        super.setDestinatario(obj.getString("destinatario"));
        super.setTipologia(obj.getInt("tipologia"));
        if (obj.has("inizio_validita")) {
            super.setInizio_validita(obj.getString("inizio_validita"));
        }
        if (obj.has("data")) {
            super.setData(obj.getString("data"));
        }
        if (obj.has("fine_validita")) {
            super.setFine_validita(obj.getString("fine_validita"));
        }
        super.setNome_viaggio(obj.getString("nome_viaggio"));
        if (obj.has("nome_mittente")) {
            super.setNome_mittente(obj.getString("nome_mittente"));
        } else {
            super.setNome_mittente(userRepository.getNomeCognome(super.getMittente()));
        }
        if (obj.has("nome_destinatario")) {
            super.setNome_destinatario(obj.getString("nome_destinatario"));
        } else {
            String nomeCognome = userRepository.getNomeCognome(super.getDestinatario());
            if (!nomeCognome.equals("")) {
                super.setNome_destinatario(nomeCognome);
            }
        }
        super.setId_viaggio(obj.getString("id_viaggio"));
        super.setId_partenza(obj.getString("id_partenza"));
        super.setId_arrivo(obj.getString("id_arrivo"));
        if (obj.has("posti")) {
            super.setPosti(obj.getInt("posti"));
        }
        if (obj.has("posti_da_prenotare")) {
            super.setPosti_da_prenotare(obj.getInt("posti_da_prenotare"));
        }
        super.setStato(0);

    }

    @Override
    public NotificaRqt clone() throws CloneNotSupportedException {
        return (NotificaRqt) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
