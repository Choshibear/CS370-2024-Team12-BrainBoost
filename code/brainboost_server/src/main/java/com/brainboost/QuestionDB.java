package com.brainboost;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class QuestionDB {
    private static final String DB_URL = "jdbc:sqlite:questions.db";

    public QuestionDB() {
        // Initialize the database
        initializeDatabase();

        // Populate the database only if it's empty
        if (isDatabaseEmpty()) {
            populateQuestionsDB();
        }
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();

            // CREATE TABLES
            stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "subject TEXT NOT NULL," +
                         "question TEXT NOT NULL UNIQUE," + // using unique to avoid duplicate questions
                         "choice1 TEXT NOT NULL," +
                         "choice2 TEXT NOT NULL," +
                         "choice3 TEXT NOT NULL," +
                         "choice4 TEXT NOT NULL," +
                         "answer TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addQuestion(String subject, String question, String choice1, String choice2, String choice3, String choice4, String answer) {
        String sql = "INSERT INTO questions (subject, question, choice1, choice2, choice3, choice4, answer) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject);
            stmt.setString(2, question);
            stmt.setString(3, choice1);
            stmt.setString(4, choice2);
            stmt.setString(5, choice3);
            stmt.setString(6, choice4);
            stmt.setString(7, answer);

            stmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore duplicate question errors
            if (!e.getMessage().contains("UNIQUE")) {
                e.printStackTrace();
            }
        }
    }

    public String getQuestion(int id) {
        String query = "SELECT * FROM questions WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("question") + "," +
                       rs.getString("choice1") + "," +
                       rs.getString("choice2") + "," +
                       rs.getString("choice3") + "," +
                       rs.getString("choice4");
            } else {
                System.out.println("No question found for id: " + id);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAnswer(int id) {
        String query = "SELECT answer FROM questions WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("answer");
            }

            return null; 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isDatabaseEmpty() {
        String query = "SELECT COUNT(*) AS count FROM questions";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // Assume empty if a problem occurs
    }

    // Populates the database using the questions.txt file
    public void populateQuestionsDB() {
        String filePath = "code/brainboost_server/questions.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> questions = new ArrayList<>();
            while ((line = br.readLine()) != null) { // while there is a line
                if (!line.trim().isEmpty()) { // Ignore empty lines
                    questions.add(line.trim()); 
                }
            }

            for (int i = 0; i < questions.size(); i += 4) { // process lines in groups of 4
                String subject = questions.get(i);
                String question = questions.get(i + 1);
                String[] choices = questions.get(i + 2).split(","); // split choices into an array
                String answer = questions.get(i + 3);

                addQuestion(subject, question, choices[0], choices[1], choices[2], choices[3], answer);
            }

            System.out.println("Questions database populated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
