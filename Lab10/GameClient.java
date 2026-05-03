package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class GameClient {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int PORT = 2026;
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server.");
            System.out.println("Type 'exit' to quit the client, or 'stop' to shut down the server.");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command.trim())) {
                    System.out.println("Exiting the GameClient...");
                    break;
                }
                out.println(command);
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server has closed the connection.");
                    break;
                }

                System.out.println(response);
            }

        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
        }
    }
}
