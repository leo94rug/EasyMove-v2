/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.Table.UTENTE;
import static DatabaseConstants.TableConstants.Utente_tipologia.NON_CONFERMATO;
import static DatabaseConstants.TableConstants.Utente_tipologia.REGISTRATO;
import static DatabaseConstants.Utente.ANNO_NASCITA;
import static DatabaseConstants.Utente.COGNOME;
import static DatabaseConstants.Utente.DATA;
import static DatabaseConstants.Utente.EMAIL;
import static DatabaseConstants.Utente.FOTO_UTENTE;
import static DatabaseConstants.Utente.ID;
import static DatabaseConstants.Utente.IMAGE_PATH;
import static DatabaseConstants.Utente.NOME;
import static DatabaseConstants.Utente.PASSWORD;
import static DatabaseConstants.Utente.PROFESSIONE;
import static DatabaseConstants.Utente.SESSO;
import static DatabaseConstants.Utente.TIPO;
import Interfaces.ICrypt;
import Interfaces.IDate;
import Model.ModelDB.Utente;
import Model.Response.UtenteRes;
import Model.Response.Viaggio_autoRes;
import static Utilita.Constants.defaultImagePath;
import static Utilita.Constants.defaultProfileImage;
import Utilita.Crypt.Encryptor;
import Utilita.DatesConversion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.naming.NamingException;

/**
 *
 * @author leo
 */
public class UserRepository {

    Connection connection;

    public UserRepository(Connection dataSource) {
        connection = dataSource;
    }

    public int deleteUser(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM utente WHERE id=?");
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    public boolean existingUser(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT email FROM utente WHERE email=?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public String getNomeCognome(String id) throws SQLException {
        String nome = "";
        String query = "SELECT nome,cognome FROM utente WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            nome = rs.getString("nome") + " " + rs.getString("cognome");
        }
        return nome;
    }

    public int updateUser(Utente anagrafica, String id) throws SQLException {
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
        ps.setString(7, id);
        return ps.executeUpdate();

    }

    public List<Viaggio_autoRes> getViaggi(String utente_fk) throws SQLException {
        List<Viaggio_autoRes> viaggiolist = new ArrayList();
        java.util.Date utilDate = new java.util.Date();
        RouteRepository routeRepository = new RouteRepository(connection);
        String query = "SELECT * FROM viaggio_auto AS vi WHERE vi.utente_fk=? AND vi.visibile=1";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, utente_fk);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Viaggio_autoRes viaggio_autoRes = new Viaggio_autoRes(rs);//TODO: gestire data in rs
            Viaggio_autoRes tratta_auto = routeRepository.getAllTratta_auto(viaggio_autoRes.getId());
            viaggio_autoRes.setTratta_auto(tratta_auto.getTratta_auto());
            viaggio_autoRes.setId_partenza(tratta_auto.getId_partenza());
            viaggio_autoRes.setId_arrivo(tratta_auto.getId_arrivo());
            viaggiolist.add(viaggio_autoRes);
        }

        return viaggiolist;
    }

    public UtenteRes getUtente(String id) throws SQLException, ParseException {
        UtenteRes utente = null;
        String query = "SELECT * FROM " + UTENTE + " AS u WHERE u.id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            utente = new UtenteRes(rs);
        }

        return utente;
    }

    public String getFermataSuccessiva(int i, String viaggio_fk) throws SQLException {

        String query = "SELECT id FROM tratta_auto WHERE viaggio_fk=? AND enumerazione=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, viaggio_fk);
        ps.setInt(2, i);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("id");
        } else {
            return getFermataSuccessiva(i - 1, viaggio_fk);
        }

    }

    public void updateUserEmailStatus(String id) throws SQLException {
        String query = "UPDATE " + UTENTE + " SET " + TIPO + "=1 WHERE " + ID + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ps.executeUpdate();

    }

    public UtenteRes getUtenteByEmail(String email) throws SQLException, ClassNotFoundException, NamingException, ParseException {
        String query = "SELECT * FROM " + UTENTE + " AS u WHERE " + EMAIL + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? new UtenteRes(rs) : null;
    }

    public boolean userConfirm(String email) throws SQLException {
        String query = "UPDATE " + UTENTE + " SET " + TIPO + " = ? WHERE " + EMAIL + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, REGISTRATO);
        ps.setString(2, email);
        return ps.executeUpdate() != 0;
    }

    public Utente formalizer(Utente utente) throws CloneNotSupportedException, Exception {
        Utente newUtente = utente.clone();
        ICrypt crypt = new Encryptor();
        IDate dateUtility = new DatesConversion();
        newUtente.setId(UUID.randomUUID().toString());
        newUtente.setFoto_utente(defaultProfileImage);
        newUtente.setImage_path(defaultImagePath);
        newUtente.setPassword(crypt.encrypt(utente.getPassword()));
        newUtente.setData(dateUtility.now());
        return newUtente;
    }

    public int insertUser(Utente utenteRqt) throws SQLException, Exception {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + UTENTE + " ("
                + ID + ", "
                + EMAIL + ", "
                + NOME + ", "
                + COGNOME + ", "
                + PASSWORD + ","
                + PROFESSIONE + ", "
                + ANNO_NASCITA + ", "
                + SESSO + ", "
                + DATA + ", "
                + IMAGE_PATH + ", "
                + FOTO_UTENTE + ", "
                + TIPO + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, utenteRqt.getId());
        ps.setString(2, utenteRqt.getEmail());
        ps.setString(3, utenteRqt.getNome());
        ps.setString(4, utenteRqt.getCognome());
        ps.setString(5, utenteRqt.getPassword());
        ps.setString(6, utenteRqt.getProfessione());
        ps.setString(7, utenteRqt.getAnno_nascita());
        ps.setString(8, utenteRqt.getSesso());
        ps.setString(9, utenteRqt.getData());
        ps.setString(10, utenteRqt.getImage_path());
        ps.setString(11, utenteRqt.getFoto_utente());
        ps.setInt(12, NON_CONFERMATO);
        return ps.executeUpdate();
    }

    public int setPassword(String id, String psw) throws SQLException, Exception {

        ICrypt crypt = new Encryptor();
        String new_psw_crypt = crypt.encrypt(psw);
        PreparedStatement ps = connection.prepareStatement("UPDATE " + UTENTE + " SET " + PASSWORD + "=? WHERE " + ID + "=?");
        ps.setString(1, new_psw_crypt);
        ps.setString(2, id);
        return ps.executeUpdate();
    }

    public int updateUtenteImage(String id, String immagine, String path) throws SQLException {
        String query = "UPDATE " + UTENTE + " SET " + FOTO_UTENTE + " = ?, " + IMAGE_PATH + " = ?  WHERE " + ID + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, immagine);
        ps.setString(2, path);
        ps.setString(3, id);
        return ps.executeUpdate();

    }

    public int getTravelNumber(String id, Date date) throws SQLException {
        String query = "SELECT COUNT(viaggio_fk) AS numero FROM tratta_auto AS t "
                + "JOIN viaggio_auto AS v ON t.viaggio_fk=v.id "
                + "WHERE v.utente_fk=?  AND t.orario_partenza<=? "
                + "GROUP BY t.viaggio_fk";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ps.setDate(2, date);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("numero");
        } else {
            return 0;
        }
    }
}
