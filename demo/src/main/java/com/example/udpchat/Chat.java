package com.example.udpchat;

import java.net.SocketException;
import java.util.Scanner;

public class Chat {

    public static void main(String[] args) throws SocketException, InterruptedException {
        Scanner input = new Scanner(System.in);
        String group = "230.0.0.1";
        int port;

        try {
            System.out.print("Choose your port: ");
            port = Integer.parseInt(input.nextLine());

            System.out.println("------------------------------------------------------------------");

            Receiver receiver = new Receiver(group, port);
            Sender sender = new Sender(group, port);
            receiver.start();
            sender.start();

            sender.join();
            receiver.changeRunStatus();

        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + e.getMessage());
        } finally {
            input.close();
        }
    }
}
