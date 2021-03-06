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

    Connection connection;

    public RelazioneRepository(Connection dataSource) {
        connection = dataSource;
    }

    public Relazione getRelazione(String mittente, String destinatario) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM relazione WHERE utente_1=? AND utente_2=?");
        ps.setString(1, mittente);
        ps.setString(2, destinatario);
        ResultSet rs = ps.executeQuery();
        Relazione relazione = null;
        if (rs.next()) {
            relazione = new Relazione(rs);
        }
        return relazione;

    }

    public int updateRelazioneDaValutare(String utente_1, String utente_2, int index, String orario_partenza) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE relazione SET da_valutare=?,da_valutare_data=? WHERE utente_1=? AND utente_2=?");
        ps.setInt(1, index);
        ps.setString(2, orario_partenza);
        ps.setString(3, utente_1);
        ps.setString(4, utente_2);
        return ps.executeUpdate();

    }

    public int updateRelazioneDaValutare(String utente_1, String utente_2, int index) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("UPDATE relazione SET da_valutare=? WHERE utente_1=? AND utente_2=?");
        ps.setInt(1, index);
        ps.setString(2, utente_1);
        ps.setString(3, utente_2);
        return ps.executeUpdate();

    }

    public void setRelazioneApprovato(String mittente, String destinatario, int relazione) throws SQLException {

        String query = "SELECT * FROM " + RELAZIONE
                + " WHERE " + UTENTE_1 + "=? AND " + UTENTE_2 + "=? ";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, mittente);
        ps.setString(2, destinatario);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            query = "UPDATE " + RELAZIONE + " SET " + APPROVATO + "=? WHERE " + UTENTE_1 + "=? AND " + UTENTE_2 + "=? ";
            ps = connection.prepareStatement(query);
            ps.setInt(1, relazione);
            ps.setString(2, mittente);
            ps.setString(3, destinatario);
            ps.executeUpdate();
        } else {
            query = "INSERT INTO " + RELAZIONE + "(" + UTENTE_1 + ", " + UTENTE_2 + ", " + APPROVATO + ") VALUES (?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, mittente);
            ps.setString(2, destinatario);
            ps.setInt(3, relazione);
            ps.executeUpdate();
        }

    }

    public int getRelazioneApprovato(String user1, String user2) throws SQLException {
        if (user1.equals(user2)) {
            return STESSO_UTENTE;
        }
        String query = "SELECT * FROM " + RELAZIONE + " WHERE " + UTENTE_1 + "=? AND " + UTENTE_2 + "=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user1);
        ps.setString(2, user2);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt((APPROVATO)) : NON_APPROVATO;
    }
}
