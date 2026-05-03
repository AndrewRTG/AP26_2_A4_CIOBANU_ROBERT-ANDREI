package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final GameServer server;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (socket) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String request = in.readLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            if (request != null && request.equalsIgnoreCase("stop")) {
                out.println("Server stopped");
                server.stopServer();
            } else {
                out.println("Server received the request " + request);
            }

        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        }
    }
}