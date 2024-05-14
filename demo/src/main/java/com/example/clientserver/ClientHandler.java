package com.example.clientserver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter printer;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(reader);
    }

    @Override
    public void run() {

        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
