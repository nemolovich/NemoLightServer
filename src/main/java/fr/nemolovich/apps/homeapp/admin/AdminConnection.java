/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class AdminConnection extends Thread {

    private static final Logger LOGGER = Logger.getLogger(AdminConnection.class);
    private int port;

    public AdminConnection(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(this.port);
        } catch (IOException ex) {
            LOGGER.error("Can not open socket on port [".concat(
                String.valueOf(this.port)).concat("]"));
            return;
        }
        while (this.isAlive()) {

            try {
                Socket client = socket.accept();
                System.out.println("Waiting for client...");

                try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream()))) {
                    writer.write(
                        "Hello. You are connected to a Simple Socket "
                        + "Server. What is your name?");
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ex) {
                LOGGER.error("Error while listening", ex);
            }
        }
    }
}
