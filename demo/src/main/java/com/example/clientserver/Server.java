package com.example.clientserver;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(6777);
            int counter = 0;
            System.out.println("[SERVER] Wainting for client connection...");
            while (true) {
                counter++;
                Socket socket = serverSocket.accept();
                System.out.println("[SERVER] Connected to client " + counter);
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("[SERVER] Fatal error, shutingdown");
            System.exit(0);
        }
    }
}