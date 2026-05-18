package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private final QuizGame quizGame;

    private PrintWriter out;
    private String playerName;

    public ClientThread(Socket socket, GameServer server, QuizGame quizGame) {
        this.socket = socket;
        this.server = server;
        this.quizGame = quizGame;
    }

    @Override
    public void run() {
        try (socket) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            out = new PrintWriter(socket.getOutputStream(), true);

            sendMessage("Connected to quiz server.");

            String request;

            while ((request = in.readLine()) != null) {
                String response = executeCommand(request);

                if (response != null && !response.isBlank()) {
                    sendMessage(response);
                }

                if ("exit".equalsIgnoreCase(request.trim())) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally {
            server.removeClient(this);

            if (playerName != null) {
                server.broadcast("Player " + playerName + " disconnected.");
            }
        }
    }

    public synchronized void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }

    private String executeCommand(String request) {
        String[] parts = request.trim().split("\\s+");

        if (parts.length == 0 || request.isBlank()) {
            return "Invalid command.";
        }

        String command = parts[0].toLowerCase();

        switch (command) {
            case "join":
                if (parts.length < 2) {
                    return "Usage: join <name>";
                }
                if (playerName != null) {
                    return "You already joined as " + playerName;
                }

                String requestedName = parts[1];
                String joinResponse = quizGame.join(requestedName);

                if (joinResponse.equals("Player " + requestedName + " joined the game.")) {
                    playerName = requestedName;
                    server.savePlayerIfNeeded(playerName);
                    server.broadcast("Player " + playerName + " joined the game.");
                    return "";
                }

                return joinResponse;

            case "question":
                return quizGame.getCurrentQuestion();

            case "answer":
                if (playerName == null) {
                    return "You must join first. Use: join <name>";
                }

                if (parts.length < 2) {
                    return "Usage: answer <A/B/C/D>";
                }

                String result = quizGame.submitAnswer(playerName, parts[1]);
                server.broadcast(result);

                if (result.contains("Game over.")) {
                    server.saveFinalResults();
                }

                return "";

            case "scores":
                return quizGame.getScores();

            case "help":
                return quizGame.getHelp();

            case "stop":
                server.stopServer();
                return "";

            case "exit":
                return "Disconnected from server.";
            case "winner":
                return quizGame.getWinner();

            default:
                return "Unknown command: " + request;
        }
    }
}