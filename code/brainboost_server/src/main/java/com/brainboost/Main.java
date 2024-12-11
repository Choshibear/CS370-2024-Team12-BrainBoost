package com.brainboost;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main 
{
    public static void main(String[] args) {
        // System.out.println(quizzes.getQuiz(1));
        // System.out.println(questions.getQuestion(1));
        try (ServerSocket serverSocket = new ServerSocket(12345)) { 
            System.out.println("Server is listening...");

            while (true) {
                try (Socket socket = serverSocket.accept()) { 
                    System.out.println("Client connected.");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String message = in.readLine();
                    System.out.println("Received from client: " + message);

                    String res = processReq(message);
                    out.println(res);
                } catch (Exception e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String processReq(String message) {
        UserDB users = new UserDB();
        QuestionDB questions = new QuestionDB();
        QuizDB quizzes = new QuizDB();
        AttemptDB attempts = new AttemptDB();

        String[] req = message.split(",");
        switch(req[0]){
            case "registerUser":
                return users.addUser(req[1], req[2]);
            case "checkUser":
                return users.checkUser(req[1], req[2]);
            case "getQuiz":
                return quizzes.getQuiz(Integer.parseInt(req[1]));
            case "getQuestion":
                return questions.getQuestion(Integer.parseInt(req[1]));
            case "getAnswer":
                return questions.getAnswer(Integer.parseInt(req[1]));
            case "addAttempt":
                return attempts.addAttempt(Integer.parseInt(req[1]), req[2], Integer.parseInt(req[3]));
            case "updateAttempt":
                return attempts.updateAttempt(Integer.parseInt(req[1]), req[2], Integer.parseInt(req[3]));
            case "getAttempt":
                return attempts.getAttempt(Integer.parseInt(req[1]), req[2]);
            case "printLeaderboard":
                return attempts.printLeaderboard(Integer.parseInt(req[1]));
            case "getAchievements":
                return attempts.getAchievements(req[1]);
            case "checkServer":
                return "true";
            default:
                return "false";
        }
    }
}
