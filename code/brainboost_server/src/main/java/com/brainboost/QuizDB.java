package com.brainboost;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class QuizDB {
    private static final String QUIZ_DB_URL = "jdbc:sqlite:quizzes.db";

    public QuizDB() {
        // Initialize the database
        initializeDatabase();

        // Populate the quizzes table only if it's empty
        if (isDatabaseEmpty()) {
            populateQuizzes();
        }
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(QUIZ_DB_URL)) {
            Statement stmt = conn.createStatement();
            // CREATE TABLES
            stmt.execute("CREATE TABLE IF NOT EXISTS quizzes (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "subject TEXT NOT NULL," +
                         "question1 INTEGER NOT NULL," +
                         "question2 INTEGER NOT NULL," +
                         "question3 INTEGER NOT NULL," +
                         "question4 INTEGER NOT NULL," +
                         "question5 INTEGER NOT NULL," +
                         "FOREIGN KEY(question1) REFERENCES questions(id)," +
                         "FOREIGN KEY(question2) REFERENCES questions(id)," +
                         "FOREIGN KEY(question3) REFERENCES questions(id)," +
                         "FOREIGN KEY(question4) REFERENCES questions(id)," +
                         "FOREIGN KEY(question5) REFERENCES questions(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isDatabaseEmpty() {
        String query = "SELECT COUNT(*) AS count FROM quizzes";
        try (Connection conn = DriverManager.getConnection(QUIZ_DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            return rs.getInt("count") == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Assume empty if an error occurs
        }
    }

    public void addQuiz(String subject, int q1, int q2, int q3, int q4, int q5) {
        try (Connection conn = DriverManager.getConnection(QUIZ_DB_URL)) {
            // Check if the quiz already exists using the subject and the set of question IDs
            String checkQuery = "SELECT COUNT(*) FROM quizzes WHERE subject = ? " +
                                "AND question1 = ? AND question2 = ? AND question3 = ? " +
                                "AND question4 = ? AND question5 = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                stmt.setString(1, subject);
                stmt.setInt(2, q1);
                stmt.setInt(3, q2);
                stmt.setInt(4, q3);
                stmt.setInt(5, q4);
                stmt.setInt(6, q5);

                ResultSet rs = stmt.executeQuery();
                if (rs.getInt(1) == 0) { // No existing quiz with the same subject and questions
                    String insertQuery = "INSERT INTO quizzes (subject, question1, question2, question3, question4, question5) " +
                                         "VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, subject);
                        insertStmt.setInt(2, q1);
                        insertStmt.setInt(3, q2);
                        insertStmt.setInt(4, q3);
                        insertStmt.setInt(5, q4);
                        insertStmt.setInt(6, q5);
                        insertStmt.executeUpdate();
                        System.out.println("Quiz added: " + subject + "Question IDs: " + q1 + " " + q2 + " " + q3 + " " + q4 + " " + q5);
                    }
                } else {
                    System.out.println("Quiz already exists.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getQuiz(int id) {
        try (Connection conn = DriverManager.getConnection(QUIZ_DB_URL)) {
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
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void populateQuizzes() {
        // Example: Populate quizzes with predefined question IDs (ensure questions with those IDs already exist in the database)
        addQuiz("Math", 1, 2, 3, 4, 5);
        addQuiz("Math", 6, 7, 8, 9, 10);
        addQuiz("Science", 11, 12, 13, 14, 15);
        addQuiz("Science", 16, 17, 18, 19, 20);
        addQuiz("History", 21, 22, 23, 24, 25);
        addQuiz("History", 26, 27, 28, 29, 30);
        addQuiz("English", 31, 32, 33, 34, 35);
        addQuiz("English", 36, 37, 38, 39, 40);
    }

    public static void main(String[] args) {
        new QuizDB();
    }
}
