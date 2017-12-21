/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.NotificaTable.ID_PARTENZA;
import static DatabaseConstants.Prenotazione.AUTISTA;
import static DatabaseConstants.Prenotazione.ID_ARRIVO;
import static DatabaseConstants.Prenotazione.PASSEGGERO;
import static DatabaseConstants.Prenotazione.POSTI;
import static DatabaseConstants.Prenotazione.PREZZO;
import static DatabaseConstants.Table.PRENOTAZIONE;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Request.PrenotazioneRqt;
import Model.Response.PrenotazioneRes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class PrenotazioneRepository {

    public static void setPrenotation(PrenotazioneRqt prenotazione, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO " + PRENOTAZIONE + 
                    "(" + AUTISTA
                    +", "+ PASSEGGERO 
                    +", "+ ID_PARTENZA
                    +", "+ ID_ARRIVO
                    +", "+ POSTI
                    +", "+ PREZZO
                    +") VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, prenotazione.getAutista());
            ps.setInt(2, prenotazione.getPasseggero());
            ps.setInt(3, prenotazione.getId_partenza());
            ps.setInt(4, prenotazione.getId_arrivo());
            ps.setInt(5, prenotazione.getPosti());
            ps.setInt(6, prenotazione.getPrezzo());
            ps.executeUpdate();
        }
    }
        public static void setPrenotation(int messaggio, int id_partenza, int id_arrivo, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM tratta_auto WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id_partenza);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enumerazione = rs.getInt("enumerazione");
                int viaggio_fk = rs.getInt("viaggio_fk");
                int id_tratta_successiva = UserRepository.getFermataSuccessiva(enumerazione + 1, viaggio_fk, ds);
                int posti = rs.getInt("posti");
                int posti_aggiornato = posti - messaggio;
                if (posti_aggiornato < 0) {
                    posti_aggiornato = 0;
                }
                query = "UPDATE tratta_auto SET posti=? WHERE id=?";
                ps = connection.prepareStatement(query);
                ps.setInt(1, posti_aggiornato);
                ps.setInt(2, id_partenza);
                int i = ps.executeUpdate();
                if (id_tratta_successiva != id_arrivo) {
                    ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE id=?");
                    ps.setInt(1, id_tratta_successiva);
                    rs = ps.executeQuery();
                }
            }
        }
    }
    public static int getDisponibilitaPrenotazione(PrenotazioneRqt prenotazione,DataSource ds) throws SQLException {
        int posti=RouteRepository.calcoloPosti(prenotazione.getId_partenza(),prenotazione.getId_arrivo(),ds);
        if(posti==-1){
            return -1;
        } else if(posti<prenotazione.getPosti()){
            return 0;
        }
        else{
            return 1;
        }
    }

    public static List<PrenotazioneRes> getPrenotazione(int id, DataSource ds) throws SQLException {
         List<PrenotazioneRes> prenotazioni = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM " + PRENOTAZIONE + "  WHERE " + PASSEGGERO + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PrenotazioneRes prenotazione = new PrenotazioneRes(rs);
                Tratta_auto tratta_auto =RouteRepository.getTravelDetail(prenotazione.getId_partenza(),prenotazione.getId_arrivo(),ds);
                prenotazione.setTratte_auto(tratta_auto);
                Utente utente = UserRepository.getUtente(prenotazione.getAutista(), ds);
                prenotazione.setUtente(utente);
                prenotazioni.add(prenotazione);
            }
            return prenotazioni;
        }    
    }
}
