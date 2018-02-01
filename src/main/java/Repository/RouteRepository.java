/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import static DatabaseConstants.Table.TRATTA_AUTO;
import static DatabaseConstants.Table.VIAGGIO_AUTO;
import static DatabaseConstants.Tratta_auto.ENUMERAZIONE;
import static DatabaseConstants.Tratta_auto.ID;
import static DatabaseConstants.Tratta_auto.POSTI;
import static DatabaseConstants.Tratta_auto.VIAGGIO_FK;
import Model.Coordinate;
import Model.ModelDB.Notifica;
import Model.ModelDB.Ricerca;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.ModelDB.Viaggio_auto;
import Model.Response.Viaggio_autoRes;
import Model.Response.Tratta_autoRes;
import Utilita.DatesConversion;
import java.sql.Connection;
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
public class RouteRepository {

    Connection connection;   

    public RouteRepository(Connection dataSource) {
        connection = dataSource;
    }

    public int calcoloPosti(String tratta1, String tratta2) throws SQLException {
        int min_posti=10;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE id=?");
        ps.setString(1, tratta1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int num = rs.getInt(ENUMERAZIONE);
            String viaggio_fk = rs.getString(VIAGGIO_FK);
            int posti = rs.getInt(POSTI);
            String id_tratta = rs.getString(ID);
            if (posti < min_posti) {
                min_posti = posti;
            }
            if (!tratta2.equals(id_tratta)) {
                ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE enumerazione=? AND viaggio_fk=?");
                ps.setInt(1, num + 1);
                ps.setString(2, viaggio_fk);
                rs = ps.executeQuery();
            }
        }
        return min_posti;
    }

    public void insertTratta_auto(Tratta_auto tratta_auto) throws SQLException {
        String query = "INSERT INTO tratta_auto("
                + "orario_partenza, "
                + "enumerazione, "
                + "viaggio_fk, "
                + "prezzo, "
                + "distanza, "
                + "posti,"
                + "lat_partenza,"
                + "lng_partenza,"
                + "lat_arrivo,"
                + "lng_arrivo,"
                + "denominazione_partenza,"
                + "data,"
                + "id,"
                + "denominazione_arrivo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, tratta_auto.getOrario_partenza());
        ps.setInt(2, tratta_auto.getEnumerazione());
        ps.setString(3, tratta_auto.getViaggio_fk());
        ps.setInt(4, tratta_auto.getPrezzo());
        ps.setInt(5, tratta_auto.getDistanza());
        ps.setInt(6, tratta_auto.getPosti());
        ps.setDouble(7, tratta_auto.getLat_partenza());
        ps.setDouble(8, tratta_auto.getLng_partenza());
        ps.setDouble(9, tratta_auto.getLat_arrivo());
        ps.setDouble(10, tratta_auto.getLng_arrivo());
        ps.setString(11, tratta_auto.getDenominazione_partenza());
        ps.setString(12, tratta_auto.getData());
        ps.setString(13, tratta_auto.getId());
        ps.setString(14, tratta_auto.getDenominazione_arrivo());
        int i = ps.executeUpdate();
    }

    public int insertViaggio_auto(Viaggio_auto viaggio_auto) throws SQLException {
        String query = "INSERT INTO viaggio_auto("
                + "id, "
                + "auto, "
                + "ritardo_max, "
                + "bagaglio_max, "
                + "disponibilita_deviazioni, "
                + "utente_fk,"
                + "data,"
                + "tipologia) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, viaggio_auto.getId());
        ps.setString(2, viaggio_auto.getAuto());
        ps.setString(3, viaggio_auto.getRitardo_max());
        ps.setString(4, viaggio_auto.getBagaglio_max());
        ps.setString(5, viaggio_auto.getDisponibilita_deviazioni());
        ps.setString(6, viaggio_auto.getUtente_fk());
        ps.setString(7, viaggio_auto.getData());
        ps.setInt(8, viaggio_auto.getTipologia());
        return ps.executeUpdate();
    }

    public List<Viaggio_autoRes> cercaAuto(Ricerca r) throws SQLException, ParseException, ClassNotFoundException, NamingException {
        Coordinate coordinate = new Coordinate(r.getLat_p(), r.getLng_p(), r.getLat_a(), r.getLng_a(), r.getDistanza_tra(), r.getDistanza());
        List<Tratta_auto> listaPartenze = new ArrayList();
        listaPartenze = listaPartenzeAuto(coordinate, r);
        return controlloTappeAuto(coordinate, listaPartenze);
    }

    private List<Tratta_auto> listaPartenzeAuto(Coordinate c, Ricerca r) throws SQLException {
        ResultSet rs;
        List<Tratta_auto> listaPartenze = new ArrayList();
        String query = ("SELECT * FROM tratta_auto AS t "
                + "JOIN viaggio_auto as v ON t.viaggio_fk=v.id "
                + "WHERE t.lat_partenza < ? AND t.lat_partenza > ? AND t.lng_partenza < ? AND t.lng_partenza > ? AND t.orario_partenza >= ? AND v.visibile=1");

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, c.getMaxLatP());
        ps.setDouble(2, c.getMinLatP());
        ps.setDouble(3, c.getMaxLonP());
        ps.setDouble(4, c.getMinLonP());
        ps.setString(5, r.getDate());
        rs = ps.executeQuery();
        while (rs.next()) {
            Tratta_auto tratta = new Tratta_auto(rs);
            listaPartenze.add(tratta);
        }

        return listaPartenze;
    }

    private List<Viaggio_autoRes> controlloTappeAuto(Coordinate c, List<Tratta_auto> listaPartenze) throws SQLException, ParseException, ClassNotFoundException, NamingException {
        List<Viaggio_autoRes> successi = new ArrayList();
        UserRepository userRepository = new UserRepository(connection);

        for (Tratta_auto tratta_auto : listaPartenze) {
            ResultSet rs;
            String query = "SELECT * FROM tratta_auto AS t WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tratta_auto.getId());
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
                    Viaggio_autoRes viaggio_autoRes = (Viaggio_autoRes) getViaggioAuto(tratta_auto.getViaggio_fk());
                    Utente utente = userRepository.getUtente(viaggio_autoRes.getUtente_fk());
                    viaggio_autoRes.setUtente(utente);
                    viaggio_autoRes.setTratta_auto(tratta_auto);
                    viaggio_autoRes.setId_partenza(tratta_auto.getId());
                    viaggio_autoRes.setId_arrivo(tratta_auto_rs.getId());
                    successi.add(viaggio_autoRes);
                    rs.afterLast();
                } else {
                    query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
                    ps = connection.prepareStatement(query);
                    ps.setString(1, tratta_auto_rs.getViaggio_fk());
                    ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
                    rs = ps.executeQuery();
                }
            }
        }

        return successi;
    }

    public int updatePosti(String id_tratta, int new_posti) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("UPDATE " + TRATTA_AUTO
                + " SET " + POSTI + "=? "
                + "WHERE " + ID + "=?");
        ps.setInt(1, new_posti);
        ps.setString(2, id_tratta);
        return ps.executeUpdate();

    }

    public boolean decreasePosti(Notifica prenotazione) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE id=?");
        ps.setString(1, prenotazione.getId_partenza());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int num = rs.getInt("enumerazione");
            int posti = rs.getInt("posti");
            String viaggio_fk = rs.getString("viaggio_fk");
            String id_tratta = rs.getString("id");
            updatePosti(id_tratta, posti - prenotazione.getPosti());
            if (!prenotazione.getId_arrivo().equalsIgnoreCase(id_tratta)) {
                ps = connection.prepareStatement("SELECT * FROM tratta_auto WHERE enumerazione=? AND viaggio_fk=?");
                ps.setInt(1, num + 1);
                ps.setString(2, viaggio_fk);
                rs = ps.executeQuery();
            }
        }

        return true;
    }

    public List<Tratta_auto> getListTratteAuto(String viaggio_fk) throws SQLException {

        List<Tratta_auto> tratteList = new ArrayList();
        ResultSet rs;
        String query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? ORDER BY enumerazione";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, viaggio_fk);
        rs = ps.executeQuery();
        while (rs.next()) {
            Tratta_auto tratta_auto = new Tratta_auto(rs);
            tratteList.add(tratta_auto);
        }
        return tratteList;

    }

    public Viaggio_autoRes getAllTratta_auto(String viaggio_fk) throws SQLException {
        Viaggio_autoRes viaggio_autoRes = new Viaggio_autoRes();
        ResultSet rs;
        String query = "SELECT * FROM tratta_auto AS t WHERE enumerazione=? AND viaggio_fk=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, 1);
        ps.setString(2, viaggio_fk);
        rs = ps.executeQuery();
        Tratta_auto tratta_auto = null;
        if (rs.next()) {
            tratta_auto = new Tratta_auto(rs);
            tratta_auto.setDistanza(0);
            tratta_auto.setPrezzo(0);
            viaggio_autoRes.setId_partenza(tratta_auto.getId());
        }
        rs.beforeFirst();
        while (rs.next()) {
            Tratta_auto tratta_auto_rs = new Tratta_auto(rs);
            tratta_auto.addDistanza(tratta_auto_rs.getDistanza());
            tratta_auto.addPosti(tratta_auto_rs.getPosti());
            tratta_auto.addPrezzo(tratta_auto_rs.getPrezzo());
            tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
            viaggio_autoRes.setId_arrivo(tratta_auto_rs.getId());
            query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
            ps = connection.prepareStatement(query);
            ps.setString(1, tratta_auto_rs.getViaggio_fk());
            ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
            rs = ps.executeQuery();
        }
        viaggio_autoRes.setTratta_auto(tratta_auto);
        return viaggio_autoRes;
    }

    public Viaggio_autoRes getViaggioAuto(String id) throws SQLException {

        Viaggio_autoRes viaggio_auto = null;
        String query = "SELECT * FROM viaggio_auto AS vi WHERE vi.id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            viaggio_auto = new Viaggio_autoRes(rs);
        }
        return viaggio_auto;

    }

    public Tratta_autoRes getTravelDetail(String tratta1, String tratta2) throws SQLException, ParseException {

        PrenotazioneRepository prenotazioneRepository = new PrenotazioneRepository(connection);
        ArrayList<Utente> utenti = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT * FROM tratta_auto AS t WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, tratta1);
        rs = ps.executeQuery();
        Tratta_autoRes tratta_auto = null;
        if (rs.next()) {
            tratta_auto = new Tratta_autoRes(rs);
        }
        tratta_auto.setDistanza(0);
        tratta_auto.setPrezzo(0);
        rs.beforeFirst();
        while (rs.next()) {
            Tratta_auto tratta_auto_rs = new Tratta_auto(rs);
            //id tratta_auto_rs per cercare in tabella prenotazioni
            String idPartenza = tratta_auto_rs.getId();
            utenti.addAll(prenotazioneRepository.getPrenotazioneByTratta_auto(idPartenza));
            tratta_auto.setPasseggeri(utenti);
            tratta_auto.addDistanza(tratta_auto_rs.getDistanza());
            tratta_auto.addPosti(tratta_auto_rs.getPosti());
            tratta_auto.addPrezzo(tratta_auto_rs.getPrezzo());
            tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
            if (tratta_auto_rs.getId().equals(tratta2)) {
                rs.afterLast();
            } else {
                query = "SELECT * FROM tratta_auto AS t WHERE viaggio_fk=? AND enumerazione=?";
                ps = connection.prepareStatement(query);
                ps.setString(1, tratta_auto_rs.getViaggio_fk());
                ps.setInt(2, tratta_auto_rs.getEnumerazione() + 1);
                rs = ps.executeQuery();
            }
        }
        return tratta_auto;

    }

    public int deleteTravel(String id) throws SQLException {

        String query = "UPDATE viaggio_auto SET visibile=0 WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();

    }

}
