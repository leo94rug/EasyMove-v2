/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.Relazione.APPROVATO;
import static DatabaseConstants.Relazione.UTENTE_1;
import static DatabaseConstants.Relazione.UTENTE_2;
import static DatabaseConstants.Table.RELAZIONE;
import DatabaseConstants.TableConstants.Relazione_approvato;
import static DatabaseConstants.TableConstants.Relazione_approvato.NON_APPROVATO;
import static DatabaseConstants.TableConstants.Relazione_approvato.STESSO_UTENTE;
import Model.ModelDB.Relazione;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class RelazioneRepository {

    DataSource ds;

    public RelazioneRepository(DataSource dataSource) {
        ds = dataSource;
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

    public void setRelazioneApprovato(int mittente, int destinatario) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO "+RELAZIONE+"(" + UTENTE_1 + ", " + UTENTE_2 + ", "+APPROVATO+") VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ps.setInt(3, Relazione_approvato.APPROVATO);
            ps.executeUpdate();
            ps = connection.prepareStatement(query);
            ps.setInt(1, destinatario);
            ps.setInt(2, mittente);
            ps.setInt(3, Relazione_approvato.APPROVATO);
            ps.executeUpdate();
        }
    }

    public int getRelazioneApprovato(int user1, int user2) throws SQLException {
        if (user1 == user2) {
            return STESSO_UTENTE;
        }
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * FROM " + RELAZIONE + " WHERE " + UTENTE_1 + "=? AND " + UTENTE_2 + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt((APPROVATO)) : NON_APPROVATO;
        }
    }
}
