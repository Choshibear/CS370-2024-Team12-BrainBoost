package com.brainboost.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class AchievementsFrame extends JPanel
{
    private JLabel imageLabel, nameLabel, descriptionLabel;

    public AchievementsFrame(JFrame previousFrame)
    {
        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel title = new JLabel("Achievements");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        //Achievements panel
        JPanel imagesPanel = new JPanel();
        for(int i = 1; i < 5; i++) //4 clickable image
        {
            int index = i;

            //load image
            ImageIcon image = new ImageIcon();
            if(checkUnlock(index-1))
            {
                image = new ImageIcon("code\\brainboost\\imgs\\" + index + ".png");
            }
            else
            {
                image = new ImageIcon("code\\brainboost\\imgs\\locked.jpg");
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
            previousFrame.setContentPane(new MenuFrame(previousFrame));
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
            "Achievement 1", 
            "Achievement 2", 
            "Achievement 3", 
            "Achievement 4"
        };
        String[] descriptions = {
            "Description 1", 
            "Description 2", 
            "Description 3", 
            "Description 4"
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
    private boolean checkUnlock(int i)
    {
        //checks user data to see if achievement at the index is unlocked
        //make a list for user like boolean[] achievements ={false,false,false,false}
        //for(int i = 0; i < thisUser.achievements.length; i++) (not sure about the format rn figure it out when making User class)
        //{
        //    if(achievements[i] == false)
        //    {
        //        return false;
        //    }
        //}
        return i != 2;//testing when achievement 3 is locked
    }
}
