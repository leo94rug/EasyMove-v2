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
import static DatabaseConstants.Table.PRENOTAZIONE;
import static DatabaseConstants.Table.UTENTE;
import static DatabaseConstants.Utente.ID;
import Model.ModelDB.Notifica;
import Model.ModelDB.Utente;
import Model.Request.NotificaRqt;
import Model.Response.PrenotazioneRes;
import Model.Response.Tratta_autoRes;
import Model.Response.Viaggio_autoRes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author leo
 */
public class PrenotazioneRepository {

    Connection connection;

    public PrenotazioneRepository(Connection dataSource) {
        connection = dataSource;
    }

    public void setPrenotation(Notifica prenotazione) throws SQLException {

        String query = "INSERT INTO " + PRENOTAZIONE
                + "(" + AUTISTA
                + ", " + PASSEGGERO
                + ", " + ID_PARTENZA
                + ", " + ID_ARRIVO
                + ", " + POSTI
                + ") VALUES (?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, prenotazione.getDestinatario());
        ps.setInt(2, prenotazione.getMittente());
        ps.setInt(3, prenotazione.getId_partenza());
        ps.setInt(4, prenotazione.getId_arrivo());
        ps.setInt(5, prenotazione.getPosti());
        ps.executeUpdate();

    }

    public void setPrenotation(int messaggio, int id_partenza, int id_arrivo) throws SQLException {

        UserRepository userRepository = new UserRepository(connection);
        String query = "SELECT * FROM tratta_auto WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id_partenza);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int enumerazione = rs.getInt("enumerazione");
            int viaggio_fk = rs.getInt("viaggio_fk");
            int id_tratta_successiva = userRepository.getFermataSuccessiva(enumerazione + 1, viaggio_fk);
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

    public Boolean getDisponibilitaPosti(Notifica prenotazione) throws SQLException {
        RouteRepository routeRepository = new RouteRepository(connection);
        int posti = routeRepository.calcoloPosti(prenotazione.getId_partenza(), prenotazione.getId_arrivo());
        return posti >= prenotazione.getPosti();
    }

    public boolean getDisponibilitaViaggio(NotificaRqt notifica) throws SQLException {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return notifica.getData().before(timestamp);

    }

    public List<PrenotazioneRes> getPrenotazione(int idUtente) throws SQLException, ParseException {
        List<PrenotazioneRes> prenotazioni = new ArrayList<>();
        UserRepository userRepository = new UserRepository(connection);
        RouteRepository routeRepository = new RouteRepository(connection);

        String query = "SELECT * FROM " + PRENOTAZIONE + "  WHERE " + PASSEGGERO + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, idUtente);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PrenotazioneRes prenotazione = new PrenotazioneRes(rs);
            Tratta_autoRes tratta_auto = routeRepository.getTravelDetail(prenotazione.getId_partenza(), prenotazione.getId_arrivo());
            prenotazione.setTratte_auto(tratta_auto);
            Utente utente = userRepository.getUtente(prenotazione.getAutista());
            prenotazione.setUtente(utente);
            prenotazioni.add(prenotazione);
        }
        return prenotazioni;

    }

    public List<Utente> getPrenotazioneByTratta_auto(int idTratta_auto) throws SQLException, ParseException {
        List<Utente> utenti = new ArrayList<>();

        String query = "SELECT * FROM " + PRENOTAZIONE + " AS p "
                + " JOIN " + UTENTE + " AS u"
                + " ON p." + PASSEGGERO + "=u." + ID
                + " WHERE " + ID_PARTENZA + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, idTratta_auto);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Utente utente = new Utente(rs);
            utenti.add(utente);
        }
        return utenti;
    }

}
