package com.chat.controller;

import com.chat.model.MessageModel;
import com.chat.view.ClientUI;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                            if ("TEXT".equals(type)) {
                                String msg = (String) in.readObject();
                                ui.appendMessage("Server: " + msg);

                                MessageModel receivedMsg = new MessageModel(1, 2, msg); // server to client
                                receivedMsg.setSend_at(new Timestamp(new Date().getTime()));
                                receivedMsg.setIs_read("no");
                                receivedMsg.sendMessage();
                            } else if ("AUDIO".equals(type)) {
                                byte[] audioData = (byte[]) in.readObject();
                                ui.appendMessage("🔊 Received audio");
                                // لا نخزن الصوت في قاعدة البيانات
                                playAudio(audioData);
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
public void recordAndSendAudio() {
    new Thread(() -> {
        TargetDataLine microphone = null;
        try {
            // إجبار التنسيق على little-endian
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                ui.appendMessage("Audio line with little-endian format not supported.");
                return;
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

//            ui.appendMessage("Recording with format: " + format);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            long endTime = System.currentTimeMillis() + 5000; // تسجيل 5 ثوانٍ

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

    private AudioFormat getSupportedFormat() {
        AudioFormat[] formatsToTry = new AudioFormat[] {
            new AudioFormat(16000, 16, 1, true, true),   // 16kHz, 16bit, mono, signed, big-endian
            new AudioFormat(16000, 16, 1, true, false),  // 16kHz, 16bit, mono, signed, little-endian
            new AudioFormat(44100, 16, 1, true, true),   // 44.1kHz, 16bit, mono, signed, big-endian
            new AudioFormat(44100, 16, 1, true, false)   // 44.1kHz, 16bit, mono, signed, little-endian
        };
        for (AudioFormat format : formatsToTry) {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (AudioSystem.isLineSupported(info)) {
                return format;
            }
        }
        return null;
    }

 private void playAudio(byte[] audioData) {
    try {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false); // نفس التنسيق little-endian

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
