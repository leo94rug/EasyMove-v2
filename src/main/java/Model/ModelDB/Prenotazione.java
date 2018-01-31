/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ModelDB;

import static DatabaseConstants.NotificaTable.ID;
import static DatabaseConstants.NotificaTable.ID_PARTENZA;
import static DatabaseConstants.Prenotazione.AUTISTA;
import static DatabaseConstants.Prenotazione.ID_ARRIVO;
import static DatabaseConstants.Prenotazione.PASSEGGERO;
import static DatabaseConstants.Prenotazione.POSTI;
import static DatabaseConstants.Prenotazione.PREZZO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author leo
 */
public class Prenotazione{

    private String id;
    private String passeggero;
    private String autista;
    private String id_partenza;
    private String id_arrivo;
    private int posti;
    private int prezzo;
//TODO: eliminare
   public Prenotazione(JSONObject obj) throws JSONException {
        if (obj.has("id")) {
            id = (obj.getString("id"));
        }
        passeggero = (obj.getString("passeggero"));
        autista = (obj.getString("autista"));
        id_arrivo = (obj.getString("id_arrivo"));
        id_partenza = (obj.getString("id_partenza"));
        if (obj.has("posti")) {
            posti = (obj.getInt("posti"));
        }
        if (obj.has("prezzo")) {
            prezzo = (obj.getInt("prezzo"));
        }
    }

    public Prenotazione(ResultSet rs) throws SQLException {
        this.id = rs.getString(ID);
        this.passeggero = rs.getString(PASSEGGERO);
        this.autista = rs.getString(AUTISTA);
        this.id_partenza = rs.getString(ID_PARTENZA);
        this.id_arrivo = rs.getString(ID_ARRIVO);
        this.posti = rs.getInt(POSTI);
        this.prezzo = rs.getInt(PREZZO);
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(String passeggero) {
        this.passeggero = passeggero;
    }

    public String getAutista() {
        return autista;
    }

    public void setAutista(String autista) {
        this.autista = autista;
    }

    public String getId_partenza() {
        return id_partenza;
    }

    public void setId_partenza(String id_partenza) {
        this.id_partenza = id_partenza;
    }

    public String getId_arrivo() {
        return id_arrivo;
    }

    public void setId_arrivo(String id_arrivo) {
        this.id_arrivo = id_arrivo;
    }

    public int getPosti() {
        return posti;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

}
