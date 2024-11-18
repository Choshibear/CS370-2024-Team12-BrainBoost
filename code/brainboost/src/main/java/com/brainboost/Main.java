package com.brainboost;
import javax.swing.JFrame;

import com.brainboost.frames.LoginFrame;

public class Main 
{
    public static void main( String[] args ) {
        try {
            String response = ServerAPI.sendMessage("Hello from Client!");
            System.out.println("Received from server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            }
        JFrame frame = new JFrame();

        //global window frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setTitle("BrainBoost");
        frame.setLocationRelativeTo(null);

        frame.setContentPane(new LoginFrame(frame));
        frame.setVisible(true);
    }
}
