/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Model.ModelDB.Posizione;
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
public class PosizioneRepository {
        Connection connection = null;

    public PosizioneRepository(Connection dataSource) {
        connection = dataSource;
    }

    public List<Posizione> getPosizioni() throws SQLException {
        List<Posizione> posizioni = new ArrayList();

            String query = "SELECT * " 
                    + "FROM posizione ";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Posizione posizione = new Posizione(rs);
                posizioni.add(posizione);
            }
        return posizioni;
    }
}
