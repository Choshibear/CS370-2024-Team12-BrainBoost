package com.brainboost.frames;

import java.awt.Font;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brainboost.ServerAPI;
import com.brainboost.User;

public class QuizFrame extends JPanel {
    private String subject;
    private String[] questions;
    private int currentQuestionNumber = 1;
    private JLabel currentquestionLabel = new JLabel("Loading Question");
    private JButton[] answerButtons = new JButton[4];
    private int score = 0;
    private JLabel pageLabel = new JLabel("Page 1 of 5");
    private JButton nextButton;
    private JButton submitQuizButton;
    
    // To revalidate content pane
    private JFrame parentFrame; 
    private User user;

    public QuizFrame(JFrame previousFrame, User user) {
        this.parentFrame = previousFrame;
        this.user = user;

        // Get current quiz info
        getQuiz(user.getQuiz_id());

        // Initialize the GUI components
        initializeGUI();
        loadQuestion(0); // Load the first question
    }

    private void initializeGUI() {
        // Quiz Title Panel
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("BrainBoost " + subject + " Quiz");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        // Quiz Header Panel
        JPanel headerPanel = new JPanel();
        JLabel subjectPanel = new JLabel(subject + " Quiz");
        JLabel questionNumPanel = new JLabel("Question " + currentQuestionNumber + " of 5");
        JButton returnButton = new JButton("Return to Menu");
        returnButton.addActionListener(e -> {
            parentFrame.setContentPane(new MenuFrame(parentFrame, user));
            parentFrame.revalidate();
        });
        headerPanel.add(subjectPanel);
        headerPanel.add(questionNumPanel);
        headerPanel.add(returnButton);

        // Quiz Question Panel
        JPanel questionPanel = new JPanel();
        currentquestionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionPanel.add(currentquestionLabel);

        // Quiz Answer Buttons Panel
        JPanel answerButtonsPanel = new JPanel();
        answerButtonsPanel.setLayout(new BoxLayout(answerButtonsPanel, BoxLayout.Y_AXIS));
        answerButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton("Loading Answer");
            int finalI = i; // wont work without this cause of encapsulation issues
            answerButtons[i].addActionListener(e -> {
                if (checkAnswer(answerButtons[finalI].getText())) {
                    answerButtons[finalI].setBackground(java.awt.Color.GREEN);
                    score++;
                } else {
                    answerButtons[finalI].setBackground(java.awt.Color.RED);
                }
                disableAnswerButtons();
            });
            answerButtonsPanel.add(answerButtons[i]);
        }

        // Page Traversal Panel
        JPanel traversalPanel = new JPanel();
        nextButton = new JButton("Next");
        submitQuizButton = new JButton("Submit Quiz");
        submitQuizButton.setVisible(false);

        nextButton.addActionListener(e -> {
            if (currentQuestionNumber < 5) {
                loadQuestion(currentQuestionNumber);
                currentQuestionNumber++;
                pageLabel.setText("Page " + currentQuestionNumber + " of 5");
            }
            if (currentQuestionNumber == 5) {
                nextButton.setEnabled(false);
                submitQuizButton.setVisible(true);
            }
        });
        //immediately takes user to leaderboard after submitting
        submitQuizButton.addActionListener(e -> {
            addAttempt(score);
            parentFrame.setContentPane(new Leaderboard(parentFrame, user));
            parentFrame.revalidate();
        });

        traversalPanel.add(pageLabel);
        traversalPanel.add(nextButton);
        traversalPanel.add(submitQuizButton);

        // Main display panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(headerPanel);
        add(questionPanel);
        add(answerButtonsPanel);
        add(traversalPanel);
    }

    private void getQuiz(int id) {
        try {
            String[] data = ServerAPI.sendMessage("getQuiz," + id).split(",");
            subject = data[0];
            questions = new String[5];
            questions[0] = data[1];
            questions[1] = data[2];
            questions[2] = data[3];
            questions[3] = data[4];
            questions[4] = data[5];
            System.out.println("Questions: " + Arrays.toString(questions));
            if (questions.length < 5) {
                throw new IllegalArgumentException("Not enough questions returned for the quiz.");
            }
            System.out.println("Obtained quiz: " + id);
        } catch (Exception e) {
            System.out.println("Error fetching quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadQuestion(int index) {
        try {
            String response = ServerAPI.sendMessage("getQuestion," + questions[index]);
            System.out.println("Server response: " + response);
            String[] data = response.split(",");
            currentquestionLabel.setText(data[0]);
            for (int i = 0; i < 4; i++) {
                answerButtons[i].setText(data[i + 1]);
                answerButtons[i].setBackground(null);
                answerButtons[i].setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println("Error fetching question: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean checkAnswer(String choice) {
        try {
            String correctAnswer = ServerAPI.sendMessage("getAnswer," + questions[currentQuestionNumber - 1]);
            return choice.equals(correctAnswer);
        } catch (Exception e) {
            System.out.println("Error checking answer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void disableAnswerButtons() {
        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }
    }

    private void addAttempt(int score) {
        try {
            String result = ServerAPI.sendMessage("getAttempt," + user.getQuiz_id() + "," + user.getUsername() + "," + score);
            if ("no attempt found".equals(result)) {
                ServerAPI.sendMessage("addAttempt," + user.getQuiz_id() + "," + user.getUsername() + "," + score);
            } else {
                if (Integer.parseInt(result) < score) {
                    ServerAPI.sendMessage("updateAttempt," + user.getQuiz_id() + "," + user.getUsername() + "," + score);
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving attempt: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
