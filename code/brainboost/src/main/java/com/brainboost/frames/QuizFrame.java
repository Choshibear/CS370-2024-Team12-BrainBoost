package com.brainboost.frames;

import java.awt.Font;
import java.awt.GridLayout;
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
    private int score = 0; 
    private JLabel currentquestionLabel = new JLabel("Loading Question");
    private JLabel status = new JLabel("No Status");
    private JLabel scoreLabel = new JLabel("Score: " + score);
    private JButton[] answerButtons = new JButton[4];
    
    private boolean currentQuestionAnswered = false;
    
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
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        status.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(scoreLabel);
        headerPanel.add(status);
        status.setVisible(false);
        
        // Quiz Question Panel
        JPanel questionPanel = new JPanel();
        currentquestionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));
        questionPanel.add(currentquestionLabel);

        // Quiz Answer Buttons Panel
        JPanel answerButtonsPanel = new JPanel();
        answerButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));
        answerButtonsPanel.setLayout(new GridLayout(2, 2, 20, 40));

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton("Loading Answer");
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 24));

            //setpreferredSize to the text of the button
            String textsize = answerButtons[i].getText();
            int width = answerButtons[i].getPreferredSize().width;
            int height = answerButtons[i].getPreferredSize().height;
            answerButtons[i].setPreferredSize(new java.awt.Dimension(width, height));

            int finalI = i; // wont work without this cause of encapsulation issues
            answerButtons[i].addActionListener(e -> {
                //when an answer button is clicked, check if the answer is correct
                //if the answer is correct, increment the score
                if (checkAnswer(answerButtons[finalI].getText())) {
                    status.setText("Correct Answer!");
                    status.setForeground(java.awt.Color.GREEN);
                    answerButtons[finalI].setBackground(java.awt.Color.GREEN);
                    score++;
                    scoreLabel.setText("Score: " + score);
                //when the answer is incorrect
                } else {
                    status.setText("Incorrect Answer!");
                    status.setForeground(java.awt.Color.RED);
                    answerButtons[finalI].setBackground(java.awt.Color.RED);
                }
                currentQuestionAnswered = true;
                status.setVisible(true);
                disableAnswerButtons();
            });
            answerButtonsPanel.add(answerButtons[i]);
        }

        // Page Traversal Panel
        JPanel traversalPanel = new JPanel();
        traversalPanel.setLayout(new BoxLayout(traversalPanel, BoxLayout.Y_AXIS));

        JLabel questionNumPanel = new JLabel("Question " + currentQuestionNumber + " of 5");
        questionNumPanel.setFont(new Font("Arial", Font.BOLD, 18));
        questionNumPanel.setAlignmentX(CENTER_ALIGNMENT);
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 24));
        nextButton.setAlignmentX(CENTER_ALIGNMENT);
        JButton submitQuizButton = new JButton("Submit Quiz");
        submitQuizButton.setFont(new Font("Arial", Font.BOLD, 24));
        submitQuizButton.setVisible(false);
        submitQuizButton.setAlignmentX(CENTER_ALIGNMENT);

        // Traversal Button actions
        //load next question
        nextButton.addActionListener(e -> {
            //checks if current question is answered
            if (!currentQuestionAnswered) {
                status.setText("Please answer the current question before moving to the next question.");
                status.setVisible(true);
            }
            else {
                status.setVisible(false);
                //checks if current question is the last question
                if (currentQuestionNumber < 5) {
                loadQuestion(currentQuestionNumber);
                }
                //last question
                if (currentQuestionNumber == 5) {
                    nextButton.setEnabled(false);
                    submitQuizButton.setVisible(true);
                }
                questionNumPanel.setText("Question " + currentQuestionNumber + " of 5");
            }
        });
        //submit quiz button
        //immediately takes user to leaderboard after submitting
        submitQuizButton.addActionListener(e -> {
            if (!currentQuestionAnswered) {
                status.setText("Please answer the current question before submitting the quiz.");
                status.setVisible(true);
            }
            else {
                addAttempt(score);
                parentFrame.setContentPane(new Leaderboard(parentFrame, user));
                parentFrame.revalidate();
            }
            
        });

        traversalPanel.add(questionNumPanel);
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

    // Fetches the quiz from the server of given id from user choice from previous frame
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
            currentQuestionNumber = index + 1;
            currentQuestionAnswered = false;
            status.setVisible(false);

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
            System.out.println("Correct answer: " + correctAnswer);
            System.out.println("User choice: " + choice);
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
            button.setBackground(null);
        }
    }

    private void addAttempt(int score) {
        try {
            String result = ServerAPI.sendMessage("getAttempt," + user.getQuiz_id() + "," + user.getUsername() + "," + score);
            System.out.println("addAttempt result: " + result);
            if ("no attempt found".equals(result)) {
                System.out.println("Added attempt: Subject ID:" + user.getQuiz_id() + "," + user.getUsername() + ", Score:" + score);
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
