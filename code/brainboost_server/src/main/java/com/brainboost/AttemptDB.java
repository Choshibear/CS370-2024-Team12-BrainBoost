package com.brainboost;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                        "quiz_id INTEGER NOT NULL REFERENCES quizzes(id)," +
                        "user TEXT NOT NULL REFERENCES users(username)," +
                        "score INTEGER NOT NULL," +
                        "PRIMARY KEY (quiz_id, user))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //adds an attempt
    public String addAttempt(int quiz_id, String user, int score){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            //added prepared statement for security
            String sql = "INSERT INTO attempts (quiz_id, user, score) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, quiz_id);
                pstmt.setString(2, user); // Change user to String
                pstmt.setInt(3, score);
                pstmt.executeUpdate();
                return "Added attempt";
}
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to add attempt";
        }
    }
    //updates the score for the attempt found given the quiz id and user id
    public String updateAttempt(int quiz_id, String user, int score){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            String sql = String.format(
                "UPDATE attempts SET score = '%d' WHERE quiz_id = '%d' AND user = '%s'",
                score, quiz_id, user
            );
            stmt.executeUpdate(sql);
            return "Updated attempt";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update attempt";
        }
    }
    //returns the score for the attempt found given the quiz id and user, is used to determine if the user has already attempted the quiz and if score is higher,to update their score
    public String getAttempt(int quiz_id, String user){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT score FROM attempts WHERE user = ? AND quiz_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user);  // Set the user parameter
                pstmt.setInt(2, quiz_id);  // Set the quiz_id parameter
   
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return String.format("%d", rs.getInt("score"));
                }
                return "no attempt found";
            }
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
                "FROM attempts WHERE quiz_id = '" + quiz_id + "' ORDER BY score DESC"
            );
            String leaderboard = "";
            System.out.println("server query result for printLeaderboard: " + rs);
            while (rs.next()) {
                leaderboard += rs.getString("user") + "," + rs.getString("score") + "/";
            }
            System.out.println("current leaderboard to client: " + leaderboard);
            //if empoty, return "no attempts found" else return leaderboard as a string
            if (leaderboard.equals("")) {
                return "no attempts found";
            } else {
                return leaderboard;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getAchievements(String user) {
        String res = ""; //Flags in order --> Math, Science, History, English, All
        int correct = 0;

        // CHECK MATH
        if(isPerfectScore(user, 1) && isPerfectScore(user, 2)) {
            res += "1,";
            correct += 2;
        } else {
            res += "0,";
        }

        // CHECK SCIENCE
        if(isPerfectScore(user, 3) && isPerfectScore(user, 4)) {
            res += "1,";
            correct += 2;
        } else {
            res += "0,";
        }

        // CHECK HISTORY
        if(isPerfectScore(user, 5) && isPerfectScore(user, 6)) {
            res += "1,";
            correct += 2;
        } else {
            res += "0,";
        }

        // CHECK ENGLISH
        if(isPerfectScore(user, 7) && isPerfectScore(user, 8)) {
            res += "1,";
            correct += 2;
        } else {
            res += "0,";
        }

        // CHECK ALL
        if(correct == 8) {
            res += "1";
        } else {
            res += "0";
        }

        return res;
        
    }

    public boolean isPerfectScore(String user, int quiz_id) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT score FROM attempts WHERE user = '" + user + "' AND quiz_id = " + quiz_id
            );
            
            if (rs.next()) {
                return rs.getInt("score") == 5;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
