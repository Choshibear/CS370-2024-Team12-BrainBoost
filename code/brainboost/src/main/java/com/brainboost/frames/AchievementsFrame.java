package com.brainboost.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brainboost.ServerAPI;
import com.brainboost.User;

public class AchievementsFrame extends JPanel
{
    private JLabel imageLabel, nameLabel, descriptionLabel;
    private String username;
    private String[] achievementArray;

    public AchievementsFrame(JFrame previousFrame,User user)
    {
        username = user.getUsername();
        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel title = new JLabel("Achievements");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        //Achievements panel
        JPanel imagesPanel = new JPanel();
        for(int i = 1; i < 6; i++) //5 clickable image
        {
            int index = i;

            //load image
            ImageIcon image = new ImageIcon();
            //windows path
            String unlockedPath = "code\\brainboost\\imgs\\" + index + ".png";
            String lockedPath = "code\\brainboost\\imgs\\locked.jpg";
            //linux path
            //unlockedPath = "code/brainboost/imgs/" + index + ".png";
            //lockedPath = "code/brainboost/imgs/locked.jpg";

            if(checkUnlock(index-1))
            {
                image = new ImageIcon(unlockedPath);
            }
            else
            {
                image = new ImageIcon(lockedPath);
            }
            //scale image down
            Image imageScaled = image.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(imageScaled);

            JButton clickButton = new JButton(scaledIcon);
            clickButton.setPreferredSize(new Dimension(110, 110));
            clickButton.setFocusPainted(false);
            //add mouse listener
            clickButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showAchievement(index); //display the clicked image
                }
            });
            imagesPanel.add(clickButton);
        }

        //detail panel
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        imageLabel = new JLabel();
        nameLabel = new JLabel();
        descriptionLabel = new JLabel();

        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        descriptionLabel.setAlignmentX(CENTER_ALIGNMENT);

        detailPanel.add(imageLabel);
        detailPanel.add(nameLabel);
        detailPanel.add(descriptionLabel);

        //return button
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JButton returnButton = new JButton("Return");
        returnButton.setFont(new Font("Arial", Font.BOLD, 24));
        returnButton.addActionListener(e -> {
            previousFrame.setContentPane(new MenuFrame(previousFrame, user));
            previousFrame.revalidate();
        });
        returnPanel.add(returnButton);
    
        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(detailPanel);
        mainPanel.add(imagesPanel);
        mainPanel.add(returnPanel);
        add(mainPanel);
    }

    //details of the image clicked
    private void showAchievement(int i)
    {
        
        String[] names = {
            "Math Wizard", 
            "Science Guru", 
            "History Buff", 
            "English Genius", 
            "Superstar"
        };
        String[] descriptions = {
            "Score 5 in both Math 1 and Math 2", 
            "Score 5 in both Science 1 and Science 2", 
            "Score 5 in both History 1 and History 2", 
            "Score 5 in both English 1 and English 2", 
            "Score 5 in all Quizzes, Nice Job Superstar!"
        };
        //display highlighted image details
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        descriptionLabel.setText(descriptions[i-1]);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        ImageIcon image = new ImageIcon();
        boolean isUnlocked = checkUnlock(i-1);
        if(isUnlocked)
        {
            image = new ImageIcon("code\\brainboost\\imgs\\" + i + ".png");
            nameLabel.setText(names[i-1]);
        }
        else
        {
            image = new ImageIcon("code\\brainboost\\imgs\\locked.jpg");
            nameLabel.setText("Locked");
        }
        Image imageScaled = image.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imageScaled);
        imageLabel.setIcon(scaledIcon);
    }

    // Order is --> Math, Science, History, English, All
    //checks if the achievement is unlocked given the index of the array
    private boolean checkUnlock(int i)
    {
        if(achievementArray != null) {
            return achievementArray[i].equals("1");
        }

        try
        {
            String achievements = ServerAPI.sendMessage("getAchievements," + username);
            achievementArray = achievements.split(",");
            System.out.println("Raw Achievement data from server in the client: " + achievements);
            System.out.println("Achievement data processed: " + Arrays.toString(achievementArray));
        }
        catch (Exception ex)
        {
            System.out.println("Error getting leaderboard: " + ex.getMessage());
        }

        return achievementArray[i].equals("1");
    }
}
