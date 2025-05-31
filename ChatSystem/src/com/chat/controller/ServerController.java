package com.chat.controller;

import com.chat.model.MessageModel;
import com.chat.view.ServerUI;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class ServerController {

    private ServerUI ui;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerController(ServerUI ui) {
        this.ui = ui;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(4789);
            ui.appendMessage("Server started, waiting for client...");
            socket = serverSocket.accept();
            ui.appendMessage("Client connected.");

            // فتح ObjectOutputStream أولاً لتجنب حظر الاتصال
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        Object obj = in.readObject();
                        if (obj instanceof String) {
                            String type = (String) obj;

                            if ("TEXT".equals(type)) {
                                String msg = (String) in.readObject();
                                ui.appendMessage("Client: " + msg);

                                // تخزين الرسالة الواردة في قاعدة البيانات فقط للنصوص
                                MessageModel receivedMsg = new MessageModel(2, 1, msg); // 2 = client, 1 = server
                                receivedMsg.setSend_at(new Timestamp(new Date().getTime()));
                                receivedMsg.setIs_read("no");
                                receivedMsg.sendMessage();

                            } else if ("AUDIO".equals(type)) {
                                byte[] audioData = (byte[]) in.readObject();
                                ui.appendMessage("🔊 Received audio");
                                // لا نخزن الصوت في قاعدة البيانات
                                playAudio(audioData);

                            } else if ("IMAGE".equals(type)) {
                                byte[] imageData = (byte[]) in.readObject();
                                ui.appendMessage("🖼 Received image");
                                // عرض الصورة في الواجهة (تأكد أن اسم الدالة مطابق)
                                ui.appendImage(imageData);
                            }
                        }
                    }
                } catch (Exception e) {
                    ui.appendMessage("Error receiving: " + e.getMessage());
                }
            });
            receiveThread.start();

        } catch (Exception e) {
            ui.appendMessage("Error: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            if (out != null) {
                out.writeObject("TEXT");
                out.writeObject(msg);
                out.flush();

                ui.appendMessage("Server: " + msg);

                // تخزين الرسالة المرسلة في قاعدة البيانات فقط للنصوص
                MessageModel sentMsg = new MessageModel(1, 2, msg); // 1 = server, 2 = client
                sentMsg.setSend_at(new Timestamp(new Date().getTime()));
                sentMsg.setIs_read("yes");
                sentMsg.sendMessage();
            } else {
                ui.appendMessage("Error: No client connected.");
            }
        } catch (Exception e) {
            ui.appendMessage("Send error: " + e.getMessage());
        }
    }

    // دالة جديدة لإرسال الصور (تحتاج تمرير بيانات الصورة من الواجهة)
    public void sendImage(byte[] imageBytes) {
        try {
            if (out != null) {
                out.writeObject("IMAGE");
                out.writeObject(imageBytes);
                out.flush();
                ui.appendMessage("Server: Sent an image");
            } else {
                ui.appendMessage("Error: No client connected.");
            }
        } catch (Exception e) {
            ui.appendMessage("Send image error: " + e.getMessage());
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
                long endTime = System.currentTimeMillis() + 5000; // تسجيل 5 ثوان

                while (System.currentTimeMillis() < endTime) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    baos.write(buffer, 0, bytesRead);
                }

                microphone.stop();
                microphone.close();

                ui.appendMessage("Recording stopped. Sending audio to client...");

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
            DataLine.Info info = new DataLine.Info(javax.sound.sampled.SourceDataLine.class, format);
            javax.sound.sampled.SourceDataLine speakers = (javax.sound.sampled.SourceDataLine) AudioSystem.getLine(info);
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
