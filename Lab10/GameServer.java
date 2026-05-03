package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GameServer {
    public static final int PORT = 2026;
    private ServerSocket serverSocket = null;
    private volatile boolean running = true; //variabila pentru stop

    public GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("GameServer started. Listening on port " + PORT + "...");

            while (running) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                new ClientThread(socket, this).start();
            }
        } catch (SocketException e) {
            if (running) {
                System.err.println("Socket error: " + e);
            } else {
                System.out.println("Server has successfully shut down.");
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing ServerSocket: " + e);
            }
        }
    }


    public void stopServer() {
        this.running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while stopping server: " + e);
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}