/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Model.Coordinate;
import Model.ModelDB.Ricerca;
import Model.ModelDB.Tratta_auto;
import Model.ModelDB.Utente;
import Model.Request.NotificaRqt;
import Model.Response.Viaggio_autoRes;
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
public class SearchRepository {

    Connection connection;

    public SearchRepository(Connection dataSource) {
        connection = dataSource;
    }
    public int insert(Ricerca search) throws SQLException {
        String query = "INSERT INTO ricerca("
                + "id, "
                + "lat_p, "
                + "lat_a, "
                + "lng_p, "
                + "lng_a,"
                + "cambio, "
                + "date_search, "
                + "distanza, "
                + "distanza_tra,"
                + "tipo,"
                + "utente_fk,"
                + "date"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, search.getId());
        ps.setDouble(2, search.getLat_p());
        ps.setDouble(3, search.getLat_a());
        ps.setDouble(4, search.getLng_p());
        ps.setDouble(5, search.getLng_a());
        ps.setInt(6, search.getCambio());
        ps.setString(7, search.getDate_search());
        ps.setInt(8, search.getDistanza());
        ps.setInt(9, search.getDistanza_tra());
        ps.setInt(10, search.getTipo());
        ps.setString(11, search.getUtente_fk());
        ps.setString(12, search.getDate());
        int r = ps.executeUpdate();
        return r;
    }
    public List<Viaggio_autoRes> cercaAuto(Ricerca r) throws SQLException, ParseException, ClassNotFoundException, NamingException {
        Coordinate coordinate = new Coordinate(r.getLat_p(), r.getLng_p(), r.getLat_a(), r.getLng_a(), r.getDistanza_tra(), r.getDistanza());
        List<Tratta_auto> listaPartenze = new ArrayList();
        listaPartenze = listaPartenzeAuto(coordinate, r);
        return controlloTappeAuto(coordinate, listaPartenze);
    }

    private List<Viaggio_autoRes> controlloTappeAuto(Coordinate c, List<Tratta_auto> listaPartenze) throws SQLException, ParseException, ClassNotFoundException, NamingException {
        List<Viaggio_autoRes> successi = new ArrayList();
        UserRepository userRepository = new UserRepository(connection);
        RouteRepository routeRepository = new RouteRepository(connection);

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
                int distanza =tratta_auto_rs.getDistanza();
                int posti = tratta_auto_rs.getPosti();
                int prezzo = tratta_auto_rs.getPrezzo();
                tratta_auto.addDistanza(distanza);
                tratta_auto.addPosti(posti);
                tratta_auto.addPrezzo(prezzo);
                tratta_auto.setDenominazione_arrivo(tratta_auto_rs.getDenominazione_arrivo());
                if (c.CalcolaDistanzaDaArrivo(tratta_auto_rs.getLat_arrivo(), tratta_auto_rs.getLng_arrivo())) {
                    Viaggio_autoRes viaggio_autoRes = (Viaggio_autoRes) routeRepository.getViaggioAuto(tratta_auto.getViaggio_fk());
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
}
