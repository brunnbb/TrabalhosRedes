package com.example.udpchat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class ShowNetWorkInterfaces {
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            displayInterfaceInformation(netint);
        }
    }

    public static void displayInterfaceInformation(NetworkInterface netint) {
        System.out.println("Nome da interface: " + netint.getName());
        System.out.println("Display Name: " + netint.getDisplayName());
        System.out.println("Endereços:");
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.println("    " + inetAddress);
        }
        System.out.println();

    }
}