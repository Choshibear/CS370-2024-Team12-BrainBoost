package com.brainboost.frames;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brainboost.User;

public class MenuFrame extends JPanel{
    private String state = "menu";
    public MenuFrame(JFrame previousFrame, User user) {
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Welcome to BrainBoost " + user.getUsername().toUpperCase() + "!");
        JLabel subtitle = new JLabel("Select an option below:");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        subtitle.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        // menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 300, 20, 300));
        
        //Buttons
        JButton QuizButton = new JButton("Take a Quiz");
        JButton LeaderboardButton = new JButton("Leaderboards");
        JButton AchievementsButton = new JButton("Achievements");
        JButton LogoutButton = new JButton("Logout");

        //Set Button Font
        QuizButton.setFont(new Font("Arial", Font.BOLD, 24));
        AchievementsButton.setFont(new Font("Arial", Font.BOLD, 24));        
        LeaderboardButton.setFont(new Font("Arial", Font.BOLD, 24));
        LogoutButton.setFont(new Font("Arial", Font.BOLD, 24));
        
        //Subjects panel
        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new GridLayout(4, 2, 20, 20)); // 4 rows, 2 columns, spacing between

        //Subjects buttons
        // Create buttons for each quiz//array of quizzes details
        String[] quizzes = {
        "Math 1", "Math 2", 
        "Science 1", "Science 2", 
        "History 1", "History 2", 
        "English 1", "English 2"
        };
        for (int i = 0; i < quizzes.length; i++) {
            int quizID = i + 1;
            JButton quizButton = new JButton(quizzes[i]);
            quizButton.setFont(new Font("Arial", Font.BOLD, 20));

            // Add action listener to set quizID and navigate to QuizFrame
            quizButton.addActionListener(e -> {
                user.setQuiz_id(quizID);
                System.out.println(quizzes[quizID - 1] + " selected. QuizID set to " + quizID);
                if ("quiz".equals(state)) {
                previousFrame.setContentPane(new QuizFrame(previousFrame, user));
                previousFrame.revalidate();
                }
                if ("leaderboard".equals(state)) {
                previousFrame.setContentPane(new Leaderboard(previousFrame, user));
                previousFrame.revalidate();
                }
            });
            subjectsPanel.add(quizButton);
        }
        //return to menu button from subjects panel
        JButton returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.setFont(new Font("Arial", Font.BOLD, 24));
        returnToMenuButton.addActionListener(e -> {
            System.out.println("Return to Menu Button Clicked");
            state = "menu";
            title.setVisible(true);
            subtitle.setText("Select an option below:");
            subjectsPanel.setVisible(false);
            subjectsPanel.setEnabled(false);
            returnToMenuButton.setVisible(false);
            returnToMenuButton.setEnabled(false);
            QuizButton.setVisible(true);
            QuizButton.setEnabled(true);
            LeaderboardButton.setVisible(true);
            LeaderboardButton.setEnabled(true);
            AchievementsButton.setVisible(true);
            AchievementsButton.setEnabled(true);
            LogoutButton.setVisible(true);
            LogoutButton.setEnabled(true);
        });        
        
        //Button alignment
        QuizButton.setAlignmentX(CENTER_ALIGNMENT);
        LeaderboardButton.setAlignmentX(CENTER_ALIGNMENT);
        AchievementsButton.setAlignmentX(CENTER_ALIGNMENT);
        LogoutButton.setAlignmentX(CENTER_ALIGNMENT);
        returnToMenuButton.setAlignmentX(CENTER_ALIGNMENT);

        //Menu Button listeners
        QuizButton.addActionListener(e -> {
            System.out.println("Quiz Button Clicked");
            state = "quiz";
            subtitle.setText("Select a quiz subject below:");
            title.setVisible(false);
            QuizButton.setVisible(false);
            QuizButton.setEnabled(false);
            LeaderboardButton.setVisible(false);
            LeaderboardButton.setEnabled(false);
            subjectsPanel.setVisible(true);
            subjectsPanel.setEnabled(true);
            returnToMenuButton.setVisible(true);
            returnToMenuButton.setEnabled(true);
            AchievementsButton.setVisible(false);
            AchievementsButton.setEnabled(false);
            LogoutButton.setVisible(false);
            LogoutButton.setEnabled(false);
            
        });
        LeaderboardButton.addActionListener(e -> {
            System.out.println("Leaderboard Button Clicked");
            state = "leaderboard";
            title.setVisible(false);
            subtitle.setText("Select the leaderboard for the subject below:");
            QuizButton.setVisible(false);
            QuizButton.setEnabled(false);
            LeaderboardButton.setVisible(false);
            LeaderboardButton.setEnabled(false);
            subjectsPanel.setVisible(true);
            subjectsPanel.setEnabled(true);
            returnToMenuButton.setVisible(true);
            returnToMenuButton.setEnabled(true);
            AchievementsButton.setVisible(false);
            AchievementsButton.setEnabled(false);
            LogoutButton.setVisible(false);
            LogoutButton.setEnabled(false);
        });
        AchievementsButton.addActionListener(e -> {
            System.out.println("Achievements Button Clicked");
            previousFrame.setContentPane(new AchievementsFrame(previousFrame,user));
            previousFrame.revalidate();
        });
        LogoutButton.addActionListener(e -> {
            System.out.println("Logout Button Clicked");
            user.logout();
            previousFrame.setContentPane(new LoginFrame(previousFrame));
            previousFrame.revalidate();
        });

        //add buttons to main panel
        menuPanel.add(returnToMenuButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(subjectsPanel);

        //hide subjects panel until user selects a quiz or leaderboard
        subjectsPanel.setVisible(false);
        subjectsPanel.setEnabled(false);
        returnToMenuButton.setVisible(false);
        returnToMenuButton.setEnabled(false);

        //add main menu buttons
        menuPanel.add(QuizButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(LeaderboardButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(AchievementsButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(LogoutButton);

        // add panels to frame// add panels to frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(menuPanel);
        add(mainPanel);
    }
}
