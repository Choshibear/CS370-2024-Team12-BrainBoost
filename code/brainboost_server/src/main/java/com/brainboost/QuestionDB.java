package com.brainboost;
import java.sql.*;

public class QuestionDB {
  private static final String DB_URL = "jdbc:sqlite:questions.db";
    
  public QuestionDB() {
      // First, initialize the database
      initializeDatabase();
  }
  
  private void initializeDatabase() {
      try (Connection conn = DriverManager.getConnection(DB_URL)) {
          Statement stmt = conn.createStatement();
          
          //CREATE TABLES
          stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "question TEXT NOT NULL," +
                      "choice1 TEXT NOT NULL," +
                      "choice2 TEXT NOT NULL," +
                      "choice3 TEXT NOT NULL," +
                      "choice4 TEXT NOT NULL)");

      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  public void addQuestion(String question, String choice1, String choice2, String choice3, String choice4) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        Statement stmt = conn.createStatement();
        String sql = String.format(
            "INSERT INTO questions (question, choice1, choice2, choice3, choice4) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s')",
            question, choice1, choice2, choice3, choice4
        );
        stmt.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }

  public String getQuestion(int id) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT question, choice1, choice2, choice3, choice4 " +
            "FROM questions WHERE id = " + id
        );
        
        if (rs.next()) {
            return String.format("%s,%s,%s,%s,%s",
                rs.getString("question"),
                rs.getString("choice1"),
                rs.getString("choice2"),
                rs.getString("choice3"),
                rs.getString("choice4")
            );
        }
        
        return null; // or return "" if you prefer empty string for not found
        
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
  }
}
