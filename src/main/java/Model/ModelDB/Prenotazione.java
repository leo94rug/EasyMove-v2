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

    private int id;
    private int passeggero;
    private int autista;
    private int id_partenza;
    private int id_arrivo;
    private int posti;
    private int prezzo;

    public Prenotazione(JSONObject obj) throws JSONException {
        if (obj.has("id")) {
            id = (obj.getInt("id"));
        }
        passeggero = (obj.getInt("passeggero"));
        autista = (obj.getInt("autista"));
        id_arrivo = (obj.getInt("id_arrivo"));
        id_partenza = (obj.getInt("id_partenza"));
        if (obj.has("posti")) {
            posti = (obj.getInt("posti"));
        }
        if (obj.has("prezzo")) {
            prezzo = (obj.getInt("prezzo"));
        }
    }

    public Prenotazione(ResultSet rs) throws SQLException {
        this.id = rs.getInt(ID);
        this.passeggero = rs.getInt(PASSEGGERO);
        this.autista = rs.getInt(AUTISTA);
        this.id_partenza = rs.getInt(ID_PARTENZA);
        this.id_arrivo = rs.getInt(ID_ARRIVO);
        this.posti = rs.getInt(POSTI);
        this.prezzo = rs.getInt(PREZZO);
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(int passeggero) {
        this.passeggero = passeggero;
    }

    public int getAutista() {
        return autista;
    }

    public void setAutista(int autista) {
        this.autista = autista;
    }

    public int getId_partenza() {
        return id_partenza;
    }

    public void setId_partenza(int id_partenza) {
        this.id_partenza = id_partenza;
    }

    public int getId_arrivo() {
        return id_arrivo;
    }

    public void setId_arrivo(int id_arrivo) {
        this.id_arrivo = id_arrivo;
    }

    public int getPosti() {
        return posti;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

}
