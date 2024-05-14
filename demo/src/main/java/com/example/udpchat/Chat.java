package com.example.udpchat;

import java.util.Scanner;

public class Chat {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String group = "225.0.0.1";
        int srcPort, dstPort;

        try {
            System.out.print("Choose your source port: ");
            srcPort = Integer.parseInt(input.nextLine());
            System.out.print("Choose your destination port: ");
            dstPort = Integer.parseInt(input.nextLine());

            System.out.println("------------------------------------------");

            Receiver receiver = new Receiver(group, srcPort);
            Sender sender = new Sender(group, srcPort, dstPort);
            receiver.start();
            sender.start();

            sender.join();
            receiver.interrupt();

        } catch (NumberFormatException | InterruptedException e) {
            System.err.println("Invalid port number: " + e.getMessage());
        } finally {
            input.close();
            System.exit(0);

        }
    }
}
