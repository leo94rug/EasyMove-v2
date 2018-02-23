/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.NotificaTable.DESTINATARIO;
import static DatabaseConstants.NotificaTable.ID;
import static DatabaseConstants.NotificaTable.MITTENTE;
import static DatabaseConstants.NotificaTable.TIPOLOGIA;
import static DatabaseConstants.Table.NOTIFICA;
import static DatabaseConstants.Table.UTENTE;
import DatabaseConstants.TableConstants.Notifica_stato;
import static DatabaseConstants.TableConstants.Notifica_tipologia.INSERISCI_FEEDBACK;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.FEEDBACK_GIA_INSERITO;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.IMPOSSIBILE_INSERIRE_FEEDBACK;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.NON_E_ANCORA_POSSIBILE;
import static DatabaseConstants.TableConstants.Relazione_da_valutare.PUO_INSERIRE_FEEDBACK;
import Interfaces.IDate;
import Model.ModelDB.Notifica;
import Model.ModelDB.Relazione;
import Model.Request.NotificaRqt;
import Model.Response.NotificaRes;
import Utilita.DatesConversion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author leo
 */
public class NotificationRepository {

    Connection connection;

    public NotificationRepository(Connection dataSource) {
        connection = dataSource;
    }

    public void checkNotificationExistAndDelete(String mittente, String destinatario, int tipologia) throws SQLException {
        String query = "SELECT " + ID + " FROM " + NOTIFICA + " AS n WHERE " + MITTENTE + "=? AND " + DESTINATARIO + "=? AND " + TIPOLOGIA + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, mittente);
        ps.setString(2, destinatario);
        ps.setInt(3, tipologia);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String id = rs.getString("n." + ID);
            query = "DELETE FROM " + NOTIFICA + " WHERE " + ID + "=?";
            ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Notifica getNotifica(String id) throws SQLException {
        String query = "SELECT * FROM " + NOTIFICA + " AS n WHERE " + ID + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? new Notifica(rs) : null;
    }

    public int getNoticationNumber(String idUtente) throws SQLException {
        IDate dateUtility = new DatesConversion();
        String date = dateUtility.now();
        PreparedStatement ps = connection.prepareStatement(""
                + "SELECT COUNT(n.id) AS numero "
                + "FROM " + UTENTE + " AS u JOIN " + NOTIFICA + " AS n ON u.id=n.mittente "
                + "WHERE n.destinatario=? "
                + "AND (n.stato=" + Notifica_stato.VISUALIZZATA + " OR n.stato=" + Notifica_stato.INSERITA + ") "
                + "AND n.inizio_validita<=? "
                + "AND n.fine_validita>=?");
        ps.setString(1, idUtente);
        ps.setString(2, date);
        ps.setString(3, date);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("numero") : 0;

    }

    public List<NotificaRes> getNotifiche(String id) throws SQLException {
        List<NotificaRes> notifiche = new ArrayList();
        IDate dateUtility = new DatesConversion();
        String date = dateUtility.now();
        String query = "SELECT * "
                + "FROM " + UTENTE + " AS u JOIN " + NOTIFICA + " AS n ON u.id=n.mittente "
                + "JOIN notifica_tipologia AS nt ON n.tipologia=nt.id_tipologia "
                + "WHERE n.fine_validita >= ? "
                + "AND n.inizio_validita <= ? "
                + "AND n.destinatario=? "
                + "AND (n.stato=" + Notifica_stato.VISUALIZZATA + " OR n.stato=" + Notifica_stato.INSERITA + ") "
                + "ORDER BY n.data DESC";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, date);
        ps.setString(2, date);
        ps.setString(3, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            notifiche.add(new NotificaRes(rs));
        }
        return notifiche;
    }

    public int insertNotifica(Notifica notifica) throws SQLException {
        String query = "INSERT INTO notifica("
                + "id, "
                + "mittente, "
                + "destinatario, "
                + "tipologia, "
                + "messaggio, "
                + "inizio_validita,"
                + "fine_validita, "
                + "id_viaggio, "
                + "nome_viaggio, "
                + "nome_mittente,"
                + "stato,"
                + "posti,"
                + "posti_da_prenotare,"
                + "id_partenza,"
                + "id_Arrivo,"
                + "nome_destinatario,"
                + "data"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, notifica.getId());
        ps.setString(2, notifica.getMittente());
        ps.setString(3, notifica.getDestinatario());
        ps.setInt(4, notifica.getTipologia());
        ps.setString(5, notifica.getMessaggio());
        ps.setString(6, notifica.getInizio_validita());
        ps.setString(7, notifica.getFine_validita());
        ps.setString(8, notifica.getId_viaggio());
        ps.setString(9, notifica.getNome_viaggio());
        ps.setString(10, notifica.getNome_mittente());
        ps.setInt(11, notifica.getStato());
        ps.setInt(12, notifica.getPosti());
        ps.setInt(13, notifica.getPosti_da_prenotare());
        ps.setString(14, notifica.getId_partenza());
        ps.setString(15, notifica.getId_arrivo());
        ps.setString(16, notifica.getNome_destinatario());
        ps.setString(17, notifica.getData());
        int r = ps.executeUpdate();
        return r;
    }

    public int eliminaNotifica(String id) throws SQLException {
        String query = "UPDATE notifica SET stato=2 WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    public void insertFeedbackNotification(String utente_1, String utente_2, Notifica notifica) throws SQLException, ParseException, CloneNotSupportedException {
        RelazioneRepository relazioneRepository = new RelazioneRepository(connection);
        RouteRepository routeRepository = new RouteRepository(connection);
        String id_tappa = notifica.getId_partenza();
        IDate dateUtility = new DatesConversion();
        String orarioPartenza = routeRepository.getTravelDetail(id_tappa, id_tappa).getOrario_partenza();
        Relazione relazione = relazioneRepository.getRelazione(utente_1, utente_2);
        if (!dateUtility.isDateValid(orarioPartenza)) {
            return;
        }

        int daValutare = relazione.getDa_valutare();
        switch (daValutare) {
            case IMPOSSIBILE_INSERIRE_FEEDBACK: {
                relazioneRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, orarioPartenza);
                break;
            }
            case NON_E_ANCORA_POSSIBILE: {
                if (dateUtility.before(orarioPartenza, relazione.getDa_valutare_data())) {
                    relazioneRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, orarioPartenza);
                }
                break;
            }
            case PUO_INSERIRE_FEEDBACK: {
                break;
            }
            case FEEDBACK_GIA_INSERITO: {
                if (dateUtility.before(orarioPartenza, relazione.getDa_valutare_data())) {
                    relazioneRepository.updateRelazioneDaValutare(utente_1, utente_2, 1, orarioPartenza);
                    break;
                }
            }
        }
        if (daValutare != FEEDBACK_GIA_INSERITO) {
            NotificaRqt insertFeedbackNotification = notifica.clone();
            insertFeedbackNotification.setTipologia(INSERISCI_FEEDBACK);
            insertFeedbackNotification.setMessaggio("Richiesta feedback");
            insertFeedbackNotification.setMittente(utente_1);
            insertFeedbackNotification.setDestinatario(utente_2);

            checkNotificationExistAndDelete(utente_1, utente_2, INSERISCI_FEEDBACK);
            insertFeedbackNotification.setInizio_validita(orarioPartenza);
            insertFeedbackNotification.setFine_validita(dateUtility.addYears(1));
            insertFeedbackNotification.setId(UUID.randomUUID().toString());
            insertFeedbackNotification.setData(dateUtility.now());
            insertNotifica(insertFeedbackNotification);
        }

    }

}
