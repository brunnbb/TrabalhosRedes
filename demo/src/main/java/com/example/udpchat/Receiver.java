package com.example.udpchat;

import java.net.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Receiver extends Thread {
    private String group;
    private int port;
    private boolean isRunning;

    public Receiver(String group, int port) {
        this.group = group;
        this.port = port;
        this.isRunning = true;
    }

    public void changeRunStatus() {
        isRunning = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(InetAddress.getByName(group));

            ObjectMapper objectMapper = new ObjectMapper();
            String data, time, date, username, txt;

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.setSoTimeout(1000);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    if (!isRunning) {
                        break;
                    }
                    continue;
                }

                data = new String(packet.getData());
                Message message = objectMapper.readValue(data, Message.class);
                time = message.getTime();
                date = message.getdate();
                username = message.getUsername();
                txt = message.getmessage();

                System.out.println(date + " " + time + " <" + username + "> said: " + txt);

            }

            socket.leaveGroup(InetAddress.getByName(group));
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
