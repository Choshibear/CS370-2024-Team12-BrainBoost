package com.brainboost.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AchievementsFrame extends JFrame
{
    private JLabel imageLabel, nameLabel, descriptionLabel, statusLabel;

    public AchievementsFrame()
    {
        //window frame
        setTitle("Achievements");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel title = new JLabel("Achievements");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(title);

        //images panel
        JPanel imagesPanel = new JPanel();
        //4 clickable images
        for(int i = 1; i < 5; i++)
        {
            int index = i;

            //load image
            ImageIcon image = new ImageIcon("code\\brainboost\\imgs\\" + index + ".png");
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
        statusLabel = new JLabel();

        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        descriptionLabel.setAlignmentX(CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        detailPanel.add(imageLabel);
        detailPanel.add(nameLabel);
        detailPanel.add(descriptionLabel);
        detailPanel.add(statusLabel);
    
        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(detailPanel);
        mainPanel.add(imagesPanel);
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
        boolean isUnlocked = checkUnlock(i-1);
        if(isUnlocked)
        {
            ImageIcon image = new ImageIcon("code\\brainboost\\imgs\\" + i + ".png");
            Image imageScaled = image.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(imageScaled);

            imageLabel.setIcon(scaledIcon);
            nameLabel.setText(names[i-1]);
            descriptionLabel.setText(descriptions[i-1]);
            statusLabel.setText("Unlocked!");
        }
        else
        {

            ImageIcon image = new ImageIcon("code\\brainboost\\imgs\\locked.jpg");
            Image imageScaled = image.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(imageScaled);

            imageLabel.setIcon(scaledIcon);
            nameLabel.setText(names[i-1]);
            descriptionLabel.setText(descriptions[i-1]);
            statusLabel.setText("Locked!");
        }
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
        return i != 1;
    }
}
