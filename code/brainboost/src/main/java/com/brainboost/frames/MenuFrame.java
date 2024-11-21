package com.brainboost.frames;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brainboost.User;

public class MenuFrame extends JPanel{
    public MenuFrame(JFrame previousFrame, User user)
    {
        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel title = new JLabel("Welcome to BrainBoost "+user.getUsername()+"!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        // menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(120, 300, 120, 300));

        
        JButton QuizButton = new JButton("Take a Quiz");
        JButton AchievementsButton = new JButton("Achievements");
        JButton LeaderboardButton = new JButton("Leaderboard");
        JButton LogoutButton = new JButton("Logout");
        //Button Font
        QuizButton.setFont(new Font("Arial", Font.BOLD, 24));
        AchievementsButton.setFont(new Font("Arial", Font.BOLD, 24));        
        LeaderboardButton.setFont(new Font("Arial", Font.BOLD, 24));
        LogoutButton.setFont(new Font("Arial", Font.BOLD, 24));
        
        //Button alignment
        QuizButton.setAlignmentX(CENTER_ALIGNMENT);
        LeaderboardButton.setAlignmentX(CENTER_ALIGNMENT);
        AchievementsButton.setAlignmentX(CENTER_ALIGNMENT);
        LogoutButton.setAlignmentX(CENTER_ALIGNMENT);

        //Button listeners
        QuizButton.addActionListener(e -> {
            System.out.println("Quiz Button Clicked");
            previousFrame.setContentPane(new QuizFrame(previousFrame, user));
            previousFrame.revalidate();

        });
        LeaderboardButton.addActionListener(e -> {
            System.out.println("Leaderboard Button Clicked");
            previousFrame.setContentPane(new Leaderboard(previousFrame,user));
            previousFrame.revalidate();
        });
        AchievementsButton.addActionListener(e -> {
            System.out.println("Achievements Button Clicked");
            previousFrame.setContentPane(new AchievementsFrame(previousFrame,user));
            previousFrame.revalidate();
        });
        LogoutButton.addActionListener(e -> {
            System.out.println("Logout Button Clicked");
            previousFrame.setContentPane(new LoginFrame(previousFrame));
            previousFrame.revalidate();
        });
        //add buttons to main panel
        menuPanel.add(QuizButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(AchievementsButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(LeaderboardButton);
        menuPanel.add(Box.createVerticalStrut(20));//Button padding
        menuPanel.add(LogoutButton);

        // add panels to frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(menuPanel);
        add(mainPanel);
    }
}