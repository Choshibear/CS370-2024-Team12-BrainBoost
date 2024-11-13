package com.brainboost;
import com.brainboost.frames.*;

public class Main 
{
    public static void main( String[] args ) {
        // try {
        //     String response = ServerAPI.sendMessage("Hello from Client!");
        //     System.out.println("Received from server: " + response);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        LoginFrame lf = new LoginFrame();
        lf.setVisible(true);
    }
}
