/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.homeappadmin.socket;

import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

/**
 *
 * @author Nemolovich
 */
public class ClientSocket {

    private final String hostname;
    private final int port;
    Socket socketClient;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientSocket(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException {

        System.out.println("Attempting to connect to ".concat(this.hostname)
            .concat(":").concat(String.valueOf(this.port)));

        this.socketClient = new Socket(this.hostname, this.port);

        this.writer = new PrintWriter(
            this.socketClient.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(
            this.socketClient.getInputStream()));

        System.out.println("Connection Established");

    }

    public String readResponse() throws IOException {
        StringBuilder result = new StringBuilder();

        int value;
        while ((value = this.reader.read()) != -1) {
            char c = (char) value;
            if (c == CommandConstants.MESSAGE_END) {
                break;
            }
            result.append(c);
        }

        return result.toString();
    }

    public void sendRequest(String message) throws IOException {
        this.writer.printf("%s%n", message);
        this.writer.flush();
    }

    public void close() throws IOException {
        this.reader.close();
        this.writer.close();
    }

    public static final String getQuitCommand() {
        return String.valueOf(CommandConstants.COMMAND_START).concat(
            CommandConstants.QUIT_COMMAND);
    }

}
