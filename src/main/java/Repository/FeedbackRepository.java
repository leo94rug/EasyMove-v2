/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import DatabaseConstants.Feedback;
import DatabaseConstants.Table;
import Model.Request.FeedbackRqt;
import Model.Response.FeedbackRes;
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
import javax.sql.DataSource;

/**
 *
 * @author leo
 */
public class FeedbackRepository {

    Connection connection = null;

    public FeedbackRepository(Connection dataSource) {
        connection = dataSource;
    }

    public FeedbackRes getFeedback(String id) throws SQLException, ParseException, ClassNotFoundException, NamingException {
        int cont = 0;
        UserRepository userRepository = new UserRepository(connection);
        List<FeedbackRes> feedbacklist = new ArrayList();

            String query = "SELECT * " 
                    + "FROM " + Table.FEEDBACK + " "
                    + " AS f WHERE f." + Feedback.UTENTE_RECENSITO + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FeedbackRes feedBackRes = new FeedbackRes(rs);
                feedBackRes.setUtente(userRepository.getUtente(feedBackRes.getUtente_recensore()));
                feedbacklist.add(feedBackRes);
                cont++;
            }
        
        FeedbackRes feedBackResList = new FeedbackRes();
        feedBackResList.setListaFeedback(feedbacklist);
        feedBackResList.calcoloMedie();
        feedBackResList.setCont(cont);
        return feedBackResList;
    }

    public int addFeedback(FeedbackRqt feedbackRqt) throws SQLException {
            String query = "INSERT INTO " + Table.FEEDBACK + "("
                    + Feedback.ID + ","
                    + Feedback.VALUTAZIONE_DISPONIBILITA + ","
                    + Feedback.VALUTAZIONE_GUIDA + ","
                    + Feedback.VALUTAZIONE_PUNTUALITA + ","
                    + Feedback.TESTO + ","
                    + Feedback.UTENTE_RECENSITO + ","
                    + Feedback.DATA + ","
                    + Feedback.UTENTE_RECENSORE + ") VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, UUID.randomUUID().toString());
            ps.setInt(2, feedbackRqt.getValutazione_disponibilita());
            ps.setInt(3, feedbackRqt.getValutazione_guida());
            ps.setInt(4, feedbackRqt.getValutazione_puntualita());
            ps.setString(5, feedbackRqt.getTesto());
            ps.setString(6, feedbackRqt.getUtente_recensito());
            ps.setString(7, DatesConversion.now());
            ps.setString(8, feedbackRqt.getUtente_recensore());
            return ps.executeUpdate();
        
    }

    public boolean existingFeedback(String mittente, String destinatario) throws SQLException {

            String query = "SELECT * FROM " + Table.FEEDBACK + " WHERE " + Feedback.UTENTE_RECENSORE + "=? AND " + Feedback.UTENTE_RECENSITO + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, mittente);
            ps.setString(2, destinatario);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    
}
