package com.brainboost;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerAPI {
  public static String sendMessage(String message) throws Exception {
        try (
            Socket socket = new Socket("localhost", 12345); // Connect to localhost on port 12345
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Send message to server
            out.println(message);

            // Read response from server
            return in.readLine();
        }
    }
}
