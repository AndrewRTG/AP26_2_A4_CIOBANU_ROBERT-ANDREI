package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 2026;

        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server.");
            System.out.println("Type 'help' for commands.");
            System.out.println("Type 'exit' to quit.");

            Thread listenerThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listenerThread.start();

            while (true) {
                String command = scanner.nextLine();
                out.println(command);

                if ("exit".equalsIgnoreCase(command.trim())) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
        }
    }
}