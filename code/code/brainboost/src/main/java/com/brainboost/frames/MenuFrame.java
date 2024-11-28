package com.brainboost.frames;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brainboost.User;

public class MenuFrame extends JPanel{
    //array of quizzes details
    private String[] quizzes = {
            "Math 1", "Math 2", 
            "Science 1", "Science 2", 
            "History 1", "History 2", 
            "English 1", "English 2"
        };

    public MenuFrame(JFrame previousFrame, User user) {
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel title = new JLabel("Welcome to BrainBoost " + user.getUsername() + "!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(4, 2, 20, 20)); // 4 rows, 2 columns, spacing between

        // Create buttons for each quiz
        for (int i = 0; i < quizzes.length; i++) {
            int quizID = i + 1;
            JButton quizButton = new JButton(quizzes[i]);
            quizButton.setFont(new Font("Arial", Font.BOLD, 20));

            // Add action listener to set quizID and navigate to QuizFrame
            quizButton.addActionListener(e -> {
                user.setQuiz_id(quizID);
                System.out.println(quizzes[quizID - 1] + " selected. QuizID set to " + quizID);
                previousFrame.setContentPane(new QuizFrame(previousFrame, user));
                previousFrame.revalidate();
            });

            menuPanel.add(quizButton);
        }

        //Achievements panel
        JPanel achievementsPanel = new JPanel();
        achievementsPanel.setLayout(new BoxLayout(achievementsPanel, BoxLayout.Y_AXIS));
        achievementsPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        JButton achievementsButton = new JButton("Achievements");
        achievementsButton.setFont(new Font("Arial", Font.BOLD, 20));
        achievementsButton.setAlignmentX(CENTER_ALIGNMENT);
        achievementsButton.addActionListener(e -> {
            previousFrame.setContentPane(new AchievementsFrame(previousFrame,user));
            previousFrame.revalidate();
        });
        achievementsPanel.add(achievementsButton);

        
        // Add panels to main frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 20)); // Title on top, quizzes below
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.add(titlePanel);
        mainPanel.add(menuPanel);
        mainPanel.add(achievementsPanel);
        add(mainPanel);
    }
}