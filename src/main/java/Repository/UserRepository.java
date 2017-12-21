/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import DatabaseConstants.Feedback;
import DatabaseConstants.NotificaTable;
import DatabaseConstants.Table;
import DatabaseConstants.TableConstants.Notifica_stato;
import Model.*;
import Model.ModelDB.Notifica;
import Model.ModelDB.Relazione;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Request.FeedbackRqt;
import Model.Request.NotificaRqt;
import Model.Request.UtenteRqt;
import Model.Response.FeedbackRes;
import Model.Response.NotificaRes;
import Model.Response.UtenteRes;
import Model.Response.Viaggio_autoRes;
import static Utilita.DatabaseUtils.ottieniIdValido;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class UserRepository {

    public static int deleteUser(int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM utente WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public static boolean existingUser(String email, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT email FROM utente WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static boolean existingFeedback(int mittente, int destinatario, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT da_valutare "
                    + "FROM relazione "
                    + "WHERE utente_1=? AND utente_2=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static String getNomeCognome(int id, DataSource ds) throws SQLException {
        String nome = "";
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT nome,cognome FROM utente WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nome = rs.getString("nome") + " " + rs.getString("cognome");
            }
            return nome;
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

    public static int updateUser(Utente anagrafica, int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE utente SET "
                    + "cognome = ?, "
                    + "anno_nascita = ?, "
                    + "biografia = ?, "
                    + "sesso = ?, "
                    + "telefono1 = ?, "
                    + "nome = ? "
                    + "WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, anagrafica.getCognome());
            ps.setTimestamp(2, anagrafica.getAnno_nascita());
            ps.setString(3, anagrafica.getBiografia());
            ps.setString(4, anagrafica.getSesso());
            ps.setString(5, anagrafica.getTelefono1());
            ps.setString(6, anagrafica.getNome());
            ps.setInt(7, id);
            return ps.executeUpdate();
        }
    }

    public static List<Viaggio_autoRes> getViaggi(int utente_fk, DataSource ds) throws SQLException {
        List<Viaggio_autoRes> viaggiolist = new ArrayList();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Timestamp oggi = new java.sql.Timestamp(utilDate.getTime());
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM viaggio_auto AS vi WHERE vi.utente_fk=? AND vi.visibile=1";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, utente_fk);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Viaggio_autoRes viaggio_autoRes = new Viaggio_autoRes(rs);
                Tratta_auto tratte_auto = RouteRepository.getTravelDetailByEnum(viaggio_autoRes.getId(), ds);
                viaggio_autoRes.setTratta_auto(tratte_auto);
                viaggiolist.add(viaggio_autoRes);
            }
        }
        return viaggiolist;
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

    public static FeedbackRes getFeedback(int id, DataSource ds) throws SQLException {
        int cont = 0;
        List<FeedbackRes> feedbacklist = new ArrayList();
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM feedback AS f WHERE f.utente_recensito=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FeedbackRes feedBackRes = new FeedbackRes(rs);
                feedBackRes.setUtente(getUtente(feedBackRes.getUtente_recensore(), ds));
                feedbacklist.add(feedBackRes);
                cont++;
            }
        }
        FeedbackRes feedBackResList = new FeedbackRes();
        feedBackResList.setListaFeedback(feedbacklist);
        feedBackResList.calcoloMedie();
        feedBackResList.setCont(cont);
        return feedBackResList;
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
                    + "AND (n.stato="+Notifica_stato.VISUALIZZATA+" OR n.stato="+Notifica_stato.INSERITA+") "
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

    public static UtenteRes getUtente(int id, DataSource ds) throws SQLException {
        UtenteRes utente = null;
        String query = "SELECT * FROM utente AS u WHERE u.id=?";
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                utente = new UtenteRes(rs);
            }
        }
        return utente;
    }

    public static void setFriendship(int mittente, int destinatario, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO relazione(utente_1, utente_2, approvato) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ps.setInt(3, 1);
            ps.executeUpdate();
            ps = connection.prepareStatement(query);
            ps.setInt(1, destinatario);
            ps.setInt(2, mittente);
            ps.setInt(3, 1);
            ps.executeUpdate();
        }
    }

    public static int getFermataSuccessiva(int i, int viaggio_fk, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT id FROM tratta_auto WHERE viaggio_fk=? AND enumerazione=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, viaggio_fk);
            ps.setInt(2, i);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return getFermataSuccessiva(i - 1, viaggio_fk, ds);
            }
        }
    }

    public static void updateUserEmailStatus(String user1, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE utente SET tipo=1 WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user1);
            ps.executeUpdate();
        }
    }

    public static boolean checkFriend(int user1, int user2, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM relazione WHERE utente_1=? AND utente_2=? AND approvato=1";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static UtenteRes getUtente(String email, String password, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            //Class.forName("com.mysql.jdbc.Driver");
            //Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easytravel2", "root", "");
            String query = "SELECT * FROM utente AS u WHERE email=? AND psw=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UtenteRes(rs);
            } else {
                return null;
            }
        }
    }

    public static Utente getUtenteByEmail(String email, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM utente AS u WHERE email=? AND tipo!=0");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utente(rs);
            }
        }
        return null;
    }

    public static boolean userConfirm(String email, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {

            String query = "UPDATE utente SET tipo = 1 WHERE email=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            return ps.executeUpdate() != 0;
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

    public static int insertUser(UtenteRqt utenteRqt, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO utente(email, nome, cognome, psw,professione, anno_nascita, sesso, tipo) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, utenteRqt.getEmail());
            ps.setString(2, utenteRqt.getNome());
            ps.setString(3, utenteRqt.getCognome());
            ps.setString(4, utenteRqt.getPassword());
            ps.setString(5, utenteRqt.getProfessione());
            ps.setTimestamp(6, utenteRqt.getAnno_nascita());
            ps.setString(7, utenteRqt.getSesso());
            ps.setInt(8, 3);
            return ps.executeUpdate();
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

    public static void checkNotificationExistAndDelete(int mittente, int destinatario, int tipologia, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT "+NotificaTable.ID+" FROM "+Table.NOTIFICA+" AS n WHERE "+NotificaTable.MITTENTE+"=? AND "+NotificaTable.DESTINATARIO+"=? AND "+NotificaTable.TIPOLOGIA+"=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ps.setInt(3, tipologia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("n."+NotificaTable.ID);
                query = "DELETE FROM "+Table.NOTIFICA+" WHERE "+NotificaTable.ID+"=?";
                ps = connection.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    public static int addFeedback(FeedbackRqt feedbackRqt, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO "+Table.FEEDBACK + "(" +
                    Feedback.VALUTAZIONE_DISPONIBILITA + "," +
                    Feedback.VALUTAZIONE_GUIDA + "," +
                    Feedback.VALUTAZIONE_PUNTUALITA + "," +
                    Feedback.TESTO + "," +
                    Feedback.UTENTE_RECENSITO + "," +
                    Feedback.UTENTE_RECENSORE + ") VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, feedbackRqt.getValutazione_disponibilita());
            ps.setInt(2, feedbackRqt.getValutazione_guida());
            ps.setInt(3, feedbackRqt.getValutazione_puntualita());
            ps.setString(4, feedbackRqt.getTesto());
            ps.setInt(5, feedbackRqt.getUtente_recensito());
            ps.setInt(6, feedbackRqt.getUtente_recensore());

            return ps.executeUpdate();
        }
    }

    public static int setPassword(int id, String new_psw_crypt, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE utente SET psw=? WHERE id=?");
            ps.setString(1, new_psw_crypt);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }
    public static int updateUtenteImage(int id, String immagine, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE utente SET foto_utente = ? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, immagine);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }
    public static int getTravelNumber(int id, Date date, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT COUNT(viaggio_fk) AS numero FROM tratta_auto AS t "
                    + "JOIN viaggio_auto AS v ON t.viaggio_fk=v.id "
                    + "WHERE v.utente_fk=?  AND t.orario_partenza<=? "
                    + "GROUP BY t.viaggio_fk";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.setDate(2, date);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            if (rs.next()) {
                return rs.getInt("numero");
            } else {
                return 0;
            }
        }
    }

    public static Relazione getRelazione(int mittente, int destinatario, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM relazione WHERE utente_1=? AND utente_2=?");
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ResultSet rs = ps.executeQuery();
            Relazione relazione = null;
            if (rs.next()) {
                relazione = new Relazione(rs);
            }
            return relazione;
        }
    }

    public static int updateRelazioneDaValutare(int utente_1, int utente_2, int index, Timestamp orario_partenza, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {                
            PreparedStatement ps = connection.prepareStatement("UPDATE relazione SET da_valutare=?,da_valutare_data=? WHERE utente_1=? AND utente_2=?");
            ps.setInt(1, index);
            ps.setTimestamp(2, orario_partenza);
            ps.setInt(3, utente_1);
            ps.setInt(4, utente_2);
            return ps.executeUpdate();    
        }
    }
}
