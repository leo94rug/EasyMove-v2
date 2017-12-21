/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class DatabaseUtils {
        public static int ottieniIdValido(String table, DataSource ds) throws SQLException {
        int id = (int) (Math.random() * 999999);
        Connection connection = ds.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            connection.close();
            id = ottieniIdValido(table, ds);
        }
        connection.close();
        return id;
    }
}
