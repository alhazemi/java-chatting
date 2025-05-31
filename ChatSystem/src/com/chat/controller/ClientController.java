package com.chat.controller;

import com.chat.model.MessageModel;
import com.chat.view.ClientUI;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class ClientController {

    private ClientUI ui;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientController(ClientUI ui) {
        this.ui = ui;
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 4789);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        Object obj = in.readObject();
                        if (obj instanceof String) {
                            String type = (String) obj;
                            switch (type) {
                                case "TEXT":
                                    String msg = (String) in.readObject();
                                    ui.appendMessage("Server: " + msg);

                                    MessageModel receivedMsg = new MessageModel(1, 2, msg); // server to client
                                    receivedMsg.setSend_at(new Timestamp(new Date().getTime()));
                                    receivedMsg.setIs_read("no");
                                    receivedMsg.sendMessage();
                                    break;

                                case "AUDIO":
                                    byte[] audioData = (byte[]) in.readObject();
                                    ui.appendMessage("🔊 Received audio");
                                    playAudio(audioData);
                                    break;

                                case "IMAGE":
                                    byte[] imageData = (byte[]) in.readObject();
                                    ui.appendMessage("🖼️ Received image:");
                                    ui.appendImage(imageData);
                                    // يمكنك إضافة تخزين الصورة أو غيره هنا إذا أردت
                                    break;

                                default:
                                    ui.appendMessage("Unknown message type: " + type);
                            }
                        }
                    }
                } catch (Exception e) {
                    ui.appendMessage("Receive error: " + e.getMessage());
                }
            });
            receiveThread.start();

        } catch (Exception e) {
            ui.appendMessage("Connection error: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            if (out != null) {
                out.writeObject("TEXT");
                out.writeObject(msg);
                out.flush();

                ui.appendMessage("Client: " + msg);

                MessageModel sentMsg = new MessageModel(2, 1, msg); // client to server
                sentMsg.setSend_at(new Timestamp(new Date().getTime()));
                sentMsg.setIs_read("yes");
                sentMsg.sendMessage();
            } else {
                ui.appendMessage("Not connected to server.");
            }
        } catch (Exception e) {
            ui.appendMessage("Send error: " + e.getMessage());
        }
    }

    public void sendImage() {
        // اختيار صورة وإرسالها للسيرفر
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(ui);
        if (result == JFileChooser.APPROVE_OPTION) {
            File imageFile = fileChooser.getSelectedFile();
            try {
                byte[] imageBytes = readFileToBytes(imageFile);
                if (out != null) {
                    out.writeObject("IMAGE");
                    out.writeObject(imageBytes);
                    out.flush();

                    ui.appendMessage("Client: [Sent an image]");
                }
            } catch (Exception e) {
                ui.appendMessage("Error sending image: " + e.getMessage());
            }
        }
    }

    private byte[] readFileToBytes(File file) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        }
    }

    public void recordAndSendAudio() {
        new Thread(() -> {
            TargetDataLine microphone = null;
            try {
                AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    ui.appendMessage("Audio line with little-endian format not supported.");
                    return;
                }

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                long endTime = System.currentTimeMillis() + 5000; // 5 ثوان تسجيل

                while (System.currentTimeMillis() < endTime) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    baos.write(buffer, 0, bytesRead);
                }

                microphone.stop();
                microphone.close();

                ui.appendMessage("Recording stopped. Sending audio...");

                byte[] audioBytes = baos.toByteArray();

                if (out != null) {
                    out.writeObject("AUDIO");
                    out.writeObject(audioBytes);
                    out.flush();
                }
            } catch (Exception e) {
                ui.appendMessage("Audio recording error: " + e.getMessage());
            } finally {
                if (microphone != null && microphone.isOpen()) {
                    microphone.stop();
                    microphone.close();
                }
            }
        }).start();
    }

    private void playAudio(byte[] audioData) {
        try {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
            speakers.start();

            speakers.write(audioData, 0, audioData.length);
            speakers.drain();
            speakers.stop();
            speakers.close();
        } catch (Exception e) {
            ui.appendMessage("Playback error: " + e.getMessage());
        }
    }
}
