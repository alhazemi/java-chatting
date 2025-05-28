package com.chat.controller;

import com.chat.view.ClientUI;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private ClientUI ui;
    private Socket socket;
    private Scanner scanner;
    private PrintWriter writer;

    public ClientController(ClientUI ui) {
        this.ui = ui;
    }

    public void startClient() {
        try {
            socket = new Socket("localhost", 4789);
            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());

            Thread receiveThread = new Thread(() -> {
                while (true) {
                    if (scanner.hasNextLine()) {
                        String msg = scanner.nextLine();
                        ui.appendMessage("Server: " + msg);
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
            writer.flush();
            ui.appendMessage("Client: " + msg);
        }
    }
}
