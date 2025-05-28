package com.chat.controller;

import com.chat.view.ServerUI;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerController {

    private ServerUI ui;
    private ServerSocket serverSocket;
    private Socket socket;
    private Scanner scanner;
    private PrintWriter writer;

    public ServerController(ServerUI ui) {
        this.ui = ui;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(4789);
            ui.appendMessage("Server started, waiting for client...");
            socket = serverSocket.accept();
            ui.appendMessage("Client connected.");

            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                while (true) {
                    if (scanner.hasNextLine()) {
                        String msg = scanner.nextLine();
                        ui.appendMessage("Client: " + msg);
                    }
                }
            });
            receiveThread.start();

        } catch (Exception e) {
            ui.appendMessage("Error: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        if (writer != null) {
            writer.println(msg);
            ui.appendMessage("Server: " + msg);
        } else {
            ui.appendMessage("Error: No client connected.");
        }
    }
}
