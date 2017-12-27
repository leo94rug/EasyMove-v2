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
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class CarRepository {

    DataSource ds;
    public CarRepository(DataSource dataSource) {
        ds = dataSource;
    }
    public int deleteCar(int id) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "DELETE FROM auto WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public void addCar(AutoRqt autoRqt) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO auto(modello, marca, colore, utente_fk) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, autoRqt.getModello());
            ps.setString(2, autoRqt.getMarca());
            ps.setString(3, autoRqt.getColore());
            ps.setInt(4, autoRqt.getUtente_fk());
            int i = ps.executeUpdate();
        }
    }

    public List<Auto> getAuto(int id) throws SQLException {
        List<Auto> autolist = new ArrayList();
        ResultSet rs;
        String query = "SELECT * FROM auto AS a WHERE a.utente_fk=?";
        try (Connection connection = ds.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                autolist.add(new Auto(rs));
            }
        }
        return autolist;
    }


}
