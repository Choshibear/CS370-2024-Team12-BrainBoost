package com.brainboost;
import java.sql.*;

public class AttemptDB {
    private static final String DB_URL = "jdbc:sqlite:attempts.db";
    
    public AttemptDB() {
        // First, initialize the database
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            
            //CREATE TABLES
            stmt.execute("CREATE TABLE IF NOT EXISTS attempts (" +
                        "quiz_id INTEGER PRIMARY KEY REFERENCES quizzes(id)," +
                        "userID INTEGER NOT NULL REFERENCES users(id)," +
                        "score INTEGER NOT NULL,");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //adds an attempt
    public String addAttempt(int quiz_id, int userID, int score){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            String sql = String.format(
                "INSERT INTO attempts (quiz_id, userID, score) " +
                "VALUES ('%d', '%d', '%d')",
                quiz_id, userID, score
            );
            stmt.executeUpdate(sql);
            return "Added attempt";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to add attempt";
        }
    }
    //updates the score for the attempt found given the quiz id and user id
    public String updateAttempt(int quiz_id, int userID, int score){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            String sql = String.format(
                "UPDATE attempts SET score = '%d' WHERE quiz_id = '%d' AND userID = '%d'",
                score, quiz_id, userID
            );
            stmt.executeUpdate(sql);
            return "Updated attempt";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update attempt";
        }
    }
    //returns the score for the attempt found given the quiz id and user id as a string
    public String getAttempt(int quiz_id, int userID){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT score " +
                "FROM attempts WHERE userID = " + userID + " AND quiz_id = " + quiz_id
            );
            if (rs.next()) {
                return String.format("%d", rs.getInt("score"));
            }
            return "no attempt found";
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //returns the all scores for the attempts found given the quiz id (paramter might change depending on subject of quiz)
    public String printLeaderboard(int quiz_id)
    {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT * " +
                "FROM attempts WHERE quiz_id = " + quiz_id
            );
            //if no attempts found
            if (!rs.next()) {
                return "Could not find attempts";
            }
            //if attempts found
            else{
                String leaderboard = "";
                while (rs.next()) {
                    leaderboard += String.format("%d, %d\n", rs.getInt("userID"), rs.getInt("score"));
                }
                return leaderboard;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
