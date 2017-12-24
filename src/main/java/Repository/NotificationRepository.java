/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import DatabaseConstants.NotificaTable;
import DatabaseConstants.Table;
import DatabaseConstants.TableConstants.Notifica_stato;
import Model.ModelDB.Notifica;
import Model.Request.NotificaRqt;
import Model.Response.NotificaRes;
import static Utilita.DatabaseUtils.ottieniIdValido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class NotificationRepository {

    public static void checkNotificationExistAndDelete(int mittente, int destinatario, int tipologia, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT " + NotificaTable.ID + " FROM " + Table.NOTIFICA + " AS n WHERE " + NotificaTable.MITTENTE + "=? AND " + NotificaTable.DESTINATARIO + "=? AND " + NotificaTable.TIPOLOGIA + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ps.setInt(3, tipologia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("n." + NotificaTable.ID);
                query = "DELETE FROM " + Table.NOTIFICA + " WHERE " + NotificaTable.ID + "=?";
                ps = connection.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    public static Notifica getNotifica(int id, DataSource ds) throws SQLException {
        Notifica notifica = null;
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM notifica AS n WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifica = new Notifica(rs);
            }
            return notifica;
        }
    }

    public static int getNoticationNumber(int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            java.util.Date utilDate = new java.util.Date();
            java.sql.Timestamp date = new java.sql.Timestamp(utilDate.getTime());
            PreparedStatement ps = connection.prepareStatement(""
                    + "SELECT COUNT(id) AS numero "
                    + "FROM notifica "
                    + "WHERE destinatario=? "
                    + "AND (stato=0 OR stato=1) "
                    + "AND inizio_validita<=? "
                    + "AND fine_validita>=?");
            ps.setInt(1, id);
            ps.setTimestamp(2, date);
            ps.setTimestamp(3, date);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            if (rs.next()) {
                i = rs.getInt("numero");
            }
            return i;
        }
    }

    public static List<NotificaRes> getNotifiche(int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            List<NotificaRes> notifiche = new ArrayList();
            GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            java.sql.Timestamp oggi = new java.sql.Timestamp(gc.getTimeInMillis());

            String query = "SELECT * "
                    + "FROM utente AS u JOIN notifica AS n ON u.id=n.mittente "
                    + "JOIN notifica_tipologia AS nt ON n.tipologia=nt.id_tipologia "
                    + "WHERE n.fine_validita >= ? "
                    + "AND n.inizio_validita <= ? "
                    + "AND n.destinatario=? "
                    + "AND (n.stato=" + Notifica_stato.VISUALIZZATA + " OR n.stato=" + Notifica_stato.INSERITA + ") "
                    + "ORDER BY n.data DESC";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setTimestamp(1, oggi);
            ps.setTimestamp(2, oggi);
            ps.setInt(3, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifiche.add(new NotificaRes(rs));
            }
            return notifiche;
        }
    }

    public static int insertNotifica(NotificaRqt notifica, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            int id_notifica = ottieniIdValido("notifica", ds);
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
                    + "nome_destinatario"
                    + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id_notifica);
            ps.setInt(2, notifica.getMittente());
            ps.setInt(3, notifica.getDestinatario());
            ps.setInt(4, notifica.getTipologia());
            ps.setString(5, notifica.getMessaggio());
            ps.setTimestamp(6, notifica.getInizio_validita());
            ps.setTimestamp(7, notifica.getFine_validita());
            ps.setInt(8, notifica.getId_viaggio());
            ps.setString(9, notifica.getNome_viaggio());
            ps.setString(10, notifica.getNome_mittente());
            ps.setInt(11, notifica.getStato());
            ps.setInt(12, notifica.getPosti());
            ps.setInt(13, notifica.getPosti_da_prenotare());
            ps.setInt(14, notifica.getId_partenza());
            ps.setInt(15, notifica.getId_arrivo());
            ps.setString(16, notifica.getNome_destinatario());

            ps.executeUpdate();
            return id_notifica;
        }
    }

    public static int eliminaNotifica(int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE notifica SET stato=2 WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
