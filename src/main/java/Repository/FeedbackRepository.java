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
import static Repository.UserRepository.getUtente;
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
public class FeedbackRepository {

    public static FeedbackRes getFeedback(int id, DataSource ds) throws SQLException {
        int cont = 0;
        List<FeedbackRes> feedbacklist = new ArrayList();
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT * " 
                    + "FROM " + Table.FEEDBACK + " "
                    + " AS f WHERE f." + Feedback.UTENTE_RECENSITO + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FeedbackRes feedBackRes = new FeedbackRes(rs);
                feedBackRes.setUtente(getUtente(feedBackRes.getUtente_recensore(), ds));
                feedbacklist.add(feedBackRes);
                cont++;
            }
        }
        FeedbackRes feedBackResList = new FeedbackRes();
        feedBackResList.setListaFeedback(feedbacklist);
        feedBackResList.calcoloMedie();
        feedBackResList.setCont(cont);
        return feedBackResList;
    }

    public static int addFeedback(FeedbackRqt feedbackRqt, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "INSERT INTO " + Table.FEEDBACK + "("
                    + Feedback.VALUTAZIONE_DISPONIBILITA + ","
                    + Feedback.VALUTAZIONE_GUIDA + ","
                    + Feedback.VALUTAZIONE_PUNTUALITA + ","
                    + Feedback.TESTO + ","
                    + Feedback.UTENTE_RECENSITO + ","
                    + Feedback.UTENTE_RECENSORE + ") VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, feedbackRqt.getValutazione_disponibilita());
            ps.setInt(2, feedbackRqt.getValutazione_guida());
            ps.setInt(3, feedbackRqt.getValutazione_puntualita());
            ps.setString(4, feedbackRqt.getTesto());
            ps.setInt(5, feedbackRqt.getUtente_recensito());
            ps.setInt(6, feedbackRqt.getUtente_recensore());

            return ps.executeUpdate();
        }
    }

    public static boolean existingFeedback(int mittente, int destinatario, DataSource ds) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT " + Feedback.ID
                    + "FROM " + Table.FEEDBACK
                    + "WHERE " + Feedback.UTENTE_RECENSORE + "=? "
                    + "AND " + Feedback.UTENTE_RECENSITO + "=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mittente);
            ps.setInt(2, destinatario);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
