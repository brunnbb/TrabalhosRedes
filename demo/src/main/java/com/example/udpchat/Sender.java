package com.example.udpchat;

import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Sender extends Thread {
    private String group;
    private int srcPort;
    private int dstPort;

    public Sender(String group, int srcPort, int dstPort) {
        this.group = group;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
    }

    @Override
    public void run() {

        try {
            MulticastSocket socket = new MulticastSocket(srcPort);
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner input = new Scanner(System.in);
            String text;

            System.out.print("Hello, please choose your username: ");
            String username = input.nextLine();
            System.out.println("You can start typing \n");

            while (true) {

                text = input.nextLine();

                if ("<exit>".equals(text)) {
                    break;
                }

                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                Message message = new Message(date, time, username, text);
                String payload = objectMapper.writeValueAsString(message);

                byte[] msg = payload.getBytes();

                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(group), dstPort);
                socket.send(packet);

            }

            System.out.println("Exiting....");
            input.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
