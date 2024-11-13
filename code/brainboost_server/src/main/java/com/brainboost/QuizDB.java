package com.brainboost;
import java.sql.*;

public class QuizDB {
    private static final String DB_URL = "jdbc:sqlite:quizzes.db";
    
    public QuizDB() {
        // First, initialize the database
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            
            //CREATE TABLES
            stmt.execute("CREATE TABLE IF NOT EXISTS quizzes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "subject TEXT NOT NULL," + 
                        "question1 INTEGER NOT NULL REFERENCES questions(id)," +
                        "question2 INTEGER NOT NULL REFERENCES questions(id)," +
                        "question3 INTEGER NOT NULL REFERENCES questions(id)," +
                        "question4 INTEGER NOT NULL REFERENCES questions(id)," +
                        "question5 INTEGER NOT NULL REFERENCES questions(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addQuiz(String subject, int q1, int q2, int q3, int q4, int q5) {
      try (Connection conn = DriverManager.getConnection(DB_URL)) {
          Statement stmt = conn.createStatement();
          String sql = String.format(
              "INSERT INTO quizzes (subject, question1, question2, question3, question4, question5) " +
              "VALUES ('%s', '%d', '%d', '%d', '%d', '%d')",
              subject, q1, q2, q3, q4, q5
          );
          stmt.executeUpdate(sql);
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  public String getQuiz(int id) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT subject, question1, question2, question3, question4, question5 " +
            "FROM quizzes WHERE id = " + id
        );
        
        if (rs.next()) {
            return String.format("%s,%d,%d,%d,%d,%d",
                rs.getString("subject"),
                rs.getInt("question1"),
                rs.getInt("question2"),
                rs.getInt("question3"),
                rs.getInt("question4"),
                rs.getInt("question5")
            );
        }
        
        return null; // or return "" if you prefer empty string for not found
        
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
 }
}
