/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.Table.TRATTA_AUTO;
import static DatabaseConstants.Tratta_auto.ID;
import static DatabaseConstants.Tratta_auto.POSTI;
import Model.Coordinate;
import Model.ModelDB.Ricerca;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Response.Viaggio_autoRes;
import Model.Request.PrenotazioneRqt;
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
public class RouteRepository {

    public static int calcoloPosti(int tratta1, int tratta2, DataSource ds) throws SQLException {
        int min_posti;
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE id=?");
            ps.setInt(1, tratta1);
            ResultSet rs = ps.executeQuery();
            min_posti = 10;
            int cont = 0;
            while (rs.next()) {
                cont++;
                int num = rs.getInt("enumerazione");
                int viaggio_fk = rs.getInt("viaggio_fk");
                int posti = rs.getInt("posti");
                int id_tratta = rs.getInt("id");
                if (posti < min_posti) {
                    min_posti = posti;
                }
                if (tratta2 != id_tratta) {
                    ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE enumerazione=? AND viaggio_fk=?");
                    ps.setInt(1, num + 1);
                    ps.setInt(2, viaggio_fk);
                    rs = ps.executeQuery();
                }
            }
            if (cont == 0) {
                min_posti = -1;
            }
        }
        return min_posti;
    }
    public static List<Viaggio_autoRes> cercaAuto(Ricerca r, DataSource ds) throws SQLException {
        Coordinate coordinate = new Coordinate(r.getLat_p(), r.getLng_p(), r.getLat_a(), r.getLng_a(), r.getDistanza_tra(), r.getDistanza());
        List<Tratta_auto> listaPartenze = new ArrayList();
        listaPartenze = listaPartenzeAuto(coordinate, r, ds);
        return RouteRepository.controlloTappeAuto(coordinate, listaPartenze, ds);
    }

    private static List<Tratta_auto> listaPartenzeAuto(Coordinate c, Ricerca r, DataSource ds) throws SQLException {
        ResultSet rs;
        List<Tratta_auto> listaPartenze = new ArrayList();
        String query = ("SELECT * FROM tratta_auto AS t "
                + "JOIN viaggio_auto as v ON t.viaggio_fk=v.id "
                + "WHERE t.lat_partenza < ? AND t.lat_partenza > ? AND t.lng_partenza < ? AND t.lng_partenza > ? AND t.orario_partenza >= ? AND v.visibile=1");
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1, c.getMaxLatP());
            ps.setDouble(2, c.getMinLatP());
            ps.setDouble(3, c.getMaxLonP());
            ps.setDouble(4, c.getMinLonP());
            ps.setDate(5, r.getDate());
            rs = ps.executeQuery();
            while (rs.next()) {
                Tratta_auto tratta = new Tratta_auto(rs);
                listaPartenze.add(tratta);
            }
        }
        return listaPartenze;
    }

    private static List<Viaggio_autoRes> controlloTappeAuto(Coordinate c, List<Tratta_auto> listaPartenze, DataSource ds) throws SQLException {
        List<Viaggio_autoRes> successi = new ArrayList();
        try (Connection connection = ds.getConnection()) {
            for (Tratta_auto tratta_auto : listaPartenze) {
                ResultSet rs;
                String query = "SELECT * FROM tratta_auto AS t WHERE id=?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, tratta_auto.getId());
                rs = ps.executeQuery();
                tratta_auto.setDistanza(0);
                tratta_auto.setPrezzo(0);
                while (rs.next()) {
                    Tratta_auto tratta_auto_rs = new Tratta_auto(rs);
                    tratta_auto.addDistanza(tratta_auto_rs.getDistanza());
                    tratta_auto.addPosti(tratta_auto_rs.getPosti());
                    tratta_auto.addPrezzo(tratta_auto_rs.getPrezzo());
                    tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
                    if (c.CalcolaDistanzaDaArrivo(tratta_auto_rs.getLat_arrivo(), tratta_auto_rs.getLng_arrivo())) {
                        Viaggio_autoRes viaggio_autoRes = (Viaggio_autoRes)RouteRepository.getViaggioAuto(tratta_auto.getViaggio_fk(), ds);
                        Utente utente = UserRepository.getUtente(viaggio_autoRes.getUtente_fk(), ds);
                        viaggio_autoRes.setUtente(utente);
                        viaggio_autoRes.setTratta_auto(tratta_auto);
                        viaggio_autoRes.setId_partenza(tratta_auto.getId());
                        viaggio_autoRes.setId_arrivo(tratta_auto_rs.getId());
                        successi.add(viaggio_autoRes);
                        rs.afterLast();
                    }  else {
                        query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
                        ps = connection.prepareStatement(query);
                        ps.setInt(1, tratta_auto_rs.getViaggio_fk());
                        ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
                        rs = ps.executeQuery();
                    }
                }
            }
        }
        return successi;
    }
    public static int updatePosti(int id_tratta, int new_posti, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE " + TRATTA_AUTO
                    + " SET " + POSTI + "=? "
                    + "WHERE " + ID + "=?");
            ps.setInt(1, new_posti);
            ps.setInt(2, id_tratta);
            return ps.executeUpdate();
        }
    }

    public static boolean decreasePosti(PrenotazioneRqt prenotazione, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE id=?");
            ps.setInt(1, prenotazione.getId_partenza());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int num = rs.getInt("enumerazione");
                int posti = rs.getInt("posti");
                int viaggio_fk = rs.getInt("viaggio_fk");
                int id_tratta = rs.getInt("id");
                updatePosti(id_tratta,posti - prenotazione.getPosti(),ds);
                if (prenotazione.getId_arrivo() != id_tratta) {
                    ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE enumerazione=? AND viaggio_fk=?");
                    ps.setInt(1, num + 1);
                    ps.setInt(2, viaggio_fk);
                    rs = ps.executeQuery();
                }
            }
        }
        return true;
    }
    public static List<Tratta_auto> getListTratteAuto(int viaggio_fk, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            List<Tratta_auto> tratteList = new ArrayList();
            ResultSet rs;
            String query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? ORDER BY enumerazione";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, viaggio_fk);
            rs = ps.executeQuery();
            while (rs.next()) {
                Tratta_auto tratta_auto = new Tratta_auto(rs);
                tratteList.add(tratta_auto);
            }
            return tratteList;
        }
    }
    public static Tratta_auto getTravelDetailByEnum(int viaggio_fk, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            ResultSet rs;
            String query = "SELECT * FROM tratta_auto AS t WHERE enumerazione=? AND viaggio_fk=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, 1);
            ps.setInt(2, viaggio_fk);
            rs = ps.executeQuery();
            Tratta_auto tratta_auto = null;
            if (rs.next()) {
                tratta_auto = new Tratta_auto(rs);
                tratta_auto.setDistanza(0);
                tratta_auto.setPrezzo(0);
            }
            rs.beforeFirst();
            while (rs.next()) {
                Tratta_auto tratta_auto_rs = new Tratta_auto(rs);
                tratta_auto.addDistanza(tratta_auto_rs.getDistanza());
                tratta_auto.addPosti(tratta_auto_rs.getPosti());
                tratta_auto.addPrezzo(tratta_auto_rs.getPrezzo());
                tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
                query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
                ps = connection.prepareStatement(query);
                ps.setInt(1, tratta_auto_rs.getViaggio_fk());
                ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
                rs = ps.executeQuery();
            }
            return tratta_auto;
        }
    }
    public static Viaggio_autoRes getViaggioAuto(int id, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            Viaggio_autoRes viaggio_auto = null;
            String query = "SELECT * FROM viaggio_auto AS vi WHERE vi.id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                viaggio_auto = new Viaggio_autoRes(rs);
            }
            return viaggio_auto;
        }
    }
    public static Tratta_auto getTravelDetail(int tratta1, int tratta2, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            ResultSet rs;
            String query = "SELECT * FROM tratta_auto AS t WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, tratta1);
            rs = ps.executeQuery();
            Tratta_auto tratta_auto = null;
            if (rs.next()) {
                tratta_auto = new Tratta_auto(rs);
            }
            tratta_auto.setDistanza(0);
            tratta_auto.setPrezzo(0);
            rs.beforeFirst();
            while (rs.next()) {
                Tratta_auto tratta_auto_rs = new Tratta_auto(rs);
                tratta_auto.addDistanza(tratta_auto_rs.getDistanza());
                tratta_auto.addPosti(tratta_auto_rs.getPosti());
                tratta_auto.addPrezzo(tratta_auto_rs.getPrezzo());
                tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
                if (tratta_auto_rs.getId() == tratta2) {
                    rs.afterLast();
                } else {
                    query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
                    ps = connection.prepareStatement(query);
                    ps.setInt(1, tratta_auto_rs.getViaggio_fk());
                    ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
                    rs = ps.executeQuery();
                }
            }
            return tratta_auto;
        }
    }
}
