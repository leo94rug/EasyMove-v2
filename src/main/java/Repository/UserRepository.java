/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.Table.UTENTE;
import static DatabaseConstants.TableConstants.Utente_tipologia.NON_CONFERMATO;
import static DatabaseConstants.TableConstants.Utente_tipologia.OSPITE;
import static DatabaseConstants.TableConstants.Utente_tipologia.REGISTRATO;
import static DatabaseConstants.Utente.ANNO_NASCITA;
import static DatabaseConstants.Utente.COGNOME;
import static DatabaseConstants.Utente.EMAIL;
import static DatabaseConstants.Utente.ID;
import static DatabaseConstants.Utente.NOME;
import static DatabaseConstants.Utente.PASSWORD;
import static DatabaseConstants.Utente.PROFESSIONE;
import static DatabaseConstants.Utente.SESSO;
import static DatabaseConstants.Utente.TIPO;
import Interfaces.ICrypt;
import Model.ModelDB.Relazione;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Request.UtenteRqt;
import Model.Response.UtenteRes;
import Model.Response.Viaggio_autoRes;
import Utilita.Crypt.Encryptor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class UserRepository {

    DataSource ds;

    public UserRepository(DataSource dataSource) {
        ds = dataSource;

    }

    public int deleteUser(int id) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM utente WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public boolean existingUser(String email) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT email FROM utente WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public String getNomeCognome(int id) throws SQLException {
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

    public int updateUser(Utente anagrafica, int id) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE " + UTENTE + " SET "
                    + "cognome = ?, "
                    + "anno_nascita = ?, "
                    + "biografia = ?, "
                    + "sesso = ?, "
                    + "telefono1 = ?, "
                    + "nome = ? "
                    + "WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, anagrafica.getCognome());
            ps.setString(2, anagrafica.getAnno_nascita());
            ps.setString(3, anagrafica.getBiografia());
            ps.setString(4, anagrafica.getSesso());
            ps.setString(5, anagrafica.getTelefono1());
            ps.setString(6, anagrafica.getNome());
            ps.setInt(7, id);
            return ps.executeUpdate();
        }
    }

    public List<Viaggio_autoRes> getViaggi(int utente_fk) throws SQLException {
        List<Viaggio_autoRes> viaggiolist = new ArrayList();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Timestamp oggi = new java.sql.Timestamp(utilDate.getTime());
        try (Connection connection = ds.getConnection()) {
            RouteRepository routeRepository = new RouteRepository(ds);
            String query = "SELECT * FROM viaggio_auto AS vi WHERE vi.utente_fk=? AND vi.visibile=1";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, utente_fk);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Viaggio_autoRes viaggio_autoRes = new Viaggio_autoRes(rs);
                Viaggio_autoRes tratte_auto = routeRepository.getTravelDetailByEnum(viaggio_autoRes.getId());
                viaggio_autoRes.setTratta_auto(tratte_auto.getTratta_auto());
                viaggio_autoRes.setId_partenza(tratte_auto.getId_partenza());
                viaggio_autoRes.setId_arrivo(tratte_auto.getId_arrivo());
                viaggiolist.add(viaggio_autoRes);
            }
        }
        return viaggiolist;
    }

    public UtenteRes getUtente(int id) throws SQLException, ParseException {
        UtenteRes utente = null;
        String query = "SELECT * FROM " + UTENTE + " AS u WHERE u.id=?";
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

    public void setFriendship(int mittente, int destinatario) throws SQLException {
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

    public int getFermataSuccessiva(int i, int viaggio_fk) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT id FROM tratta_auto WHERE viaggio_fk=? AND enumerazione=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, viaggio_fk);
            ps.setInt(2, i);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return getFermataSuccessiva(i - 1, viaggio_fk);
            }
        }
    }

    public void updateUserEmailStatus(String id) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE " + UTENTE + " SET " + TIPO + "=1 WHERE " + ID + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public int checkFriend(int user1, int user2) throws SQLException {
        if (user1 == user2) {
            return 2;
        }
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM relazione WHERE utente_1=? AND utente_2=? AND approvato=1";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public UtenteRes getUtente(String email) throws SQLException, ClassNotFoundException, NamingException, ParseException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM " + UTENTE + " AS u WHERE " + EMAIL + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? new UtenteRes(rs) : null;
        }
    }

    public Utente getUtenteByEmail(String email) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * "
                    + "FROM " + UTENTE + " AS u "
                    + "WHERE " + EMAIL + "=? "
                    + "AND " + TIPO + "!=? "
                    + "AND " + TIPO + "!=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setInt(2, OSPITE);
            ps.setInt(3, NON_CONFERMATO);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utente(rs);
            }
        }
        return null;
    }

    public boolean userConfirm(String email) throws SQLException {
        try (Connection connection = ds.getConnection()) {

            String query = "UPDATE " + UTENTE + " SET " + TIPO + " = ? WHERE " + EMAIL + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, REGISTRATO);
            ps.setString(2, email);
            return ps.executeUpdate() != 0;
        }
    }

    public int insertUser(Utente utenteRqt) throws SQLException, Exception {
        try (Connection connection = ds.getConnection()) {
            ICrypt crypt = new Encryptor();
            utenteRqt.setPassword(crypt.encrypt(utenteRqt.getPassword()));
            PreparedStatement ps = connection.prepareStatement("INSERT INTO " + UTENTE + " (" + EMAIL + ", " + NOME + ", " + COGNOME + ", " + PASSWORD + "," + PROFESSIONE + ", " + ANNO_NASCITA + ", " + SESSO + ", " + TIPO + ") VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, utenteRqt.getEmail());
            ps.setString(2, utenteRqt.getNome());
            ps.setString(3, utenteRqt.getCognome());
            ps.setString(4, utenteRqt.getPassword());
            ps.setString(5, utenteRqt.getProfessione());
            ps.setString(6, utenteRqt.getAnno_nascita());
            ps.setString(7, utenteRqt.getSesso());
            ps.setInt(8, NON_CONFERMATO);
            return ps.executeUpdate();
        }
    }

    public int setPassword(int id, String psw) throws SQLException, Exception {
        try (Connection connection = ds.getConnection()) {
            ICrypt crypt = new Encryptor();
            String new_psw_crypt = crypt.encrypt(psw);
            PreparedStatement ps = connection.prepareStatement("UPDATE " + UTENTE + " SET psw=? WHERE id=?");
            ps.setString(1, new_psw_crypt);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    public int updateUtenteImage(int id, String immagine) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "UPDATE " + UTENTE + " SET foto_utente = ? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, immagine);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    public int getTravelNumber(int id, Date date) throws SQLException {
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

    public Relazione getRelazione(int mittente, int destinatario) throws SQLException {
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

    public int updateRelazioneDaValutare(int utente_1, int utente_2, int index, Timestamp orario_partenza) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE relazione SET da_valutare=?,da_valutare_data=? WHERE utente_1=? AND utente_2=?");
            ps.setInt(1, index);
            ps.setTimestamp(2, orario_partenza);
            ps.setInt(3, utente_1);
            ps.setInt(4, utente_2);
            return ps.executeUpdate();
        }
    }

    public int updateRelazioneDaValutare(int utente_1, int utente_2, int index) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE relazione SET da_valutare=? WHERE utente_1=? AND utente_2=?");
            ps.setInt(1, index);
            ps.setInt(2, utente_1);
            ps.setInt(3, utente_2);
            return ps.executeUpdate();
        }
    }
}
