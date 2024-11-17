package com.brainboost.frames;

import javax.swing.*;

import java.awt.*;

public class QuizFrame extends JFrame {

    public QuizFrame(){

        setTitle("Quiz");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

         //Quiz Title Panel
         JPanel titlePanel = new JPanel();
         JLabel title = new JLabel("BrainBoost Quiz: Test");
         title.setFont(new Font("Arial", Font.BOLD, 32)); 
         titlePanel.add(title);

         //Quiz Panel
         JPanel quizPanel = new JPanel();
         JLabel question = new JLabel("This is an example question: How are you doing today?");
         question.setFont(new Font("Arial", Font.BOLD, 18)); 
         quizPanel.add(question);

         //Answer Panel
         JPanel answerPanel = new JPanel();
         JLabel AnswerA = new JLabel("A: Bad");
         JLabel AnswerB = new JLabel("B: Alright");
         JLabel AnswerC = new JLabel("C: Good");
         JLabel AnswerD = new JLabel("D: Great");
         answerPanel.add(AnswerA);
         answerPanel.add(AnswerB);
         answerPanel.add(AnswerC);
         answerPanel.add(AnswerD);

        //Button Panel
        JPanel buttonPanel = new JPanel();
        JButton AButton = new JButton("Answer A");
        JButton BButton = new JButton("Answer B");
        JButton CButton = new JButton("Answer C");
        JButton DButton = new JButton("Answer D");

        buttonPanel.add(AButton);
        buttonPanel.add(BButton);
        buttonPanel.add(CButton);
        buttonPanel.add(DButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); 

        //Main display panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); 
        mainPanel.add(quizPanel);
        mainPanel.add(answerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        add(mainPanel);
    }
}
