/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Model.ModelDB.Auto;
import Model.Request.AutoRqt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leo
 */
public class CarRepository {

    Connection connection;

    public CarRepository(Connection dataSource) {
        connection = dataSource;
    }

    public int deleteCar(String id) throws SQLException {
        String query = "DELETE FROM auto WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    public void addCar(AutoRqt autoRqt) throws SQLException {
        String query = "INSERT INTO auto(id, modello, marca, colore, utente_fk) VALUES (?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, autoRqt.getId());
        ps.setString(2, autoRqt.getModello());
        ps.setString(3, autoRqt.getMarca());
        ps.setString(4, autoRqt.getColore());
        ps.setString(5, autoRqt.getUtente_fk());
        int i = ps.executeUpdate();
    }

    public List<Auto> getAuto(String id) throws SQLException {
        List<Auto> autolist = new ArrayList();
        ResultSet rs;
        String query = "SELECT * FROM auto AS a WHERE a.utente_fk=?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        rs = ps.executeQuery();
        while (rs.next()) {
            autolist.add(new Auto(rs));
        }

        return autolist;
    }

}
