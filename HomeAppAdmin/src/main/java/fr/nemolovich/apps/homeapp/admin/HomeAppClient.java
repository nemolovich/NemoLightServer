/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin;

import fr.nemolovich.apps.homeapp.admin.socket.ClientSocket;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Nemolovich
 */
public class HomeAppClient {

    private static final String HELP = "Expected parameters: <HOST> <PORT>";

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println(HELP);
            return;
        }
        String host;
        int port;
        try {
            host = args[0];
            port = Integer.valueOf(args[1]);
            if (host.length() < 2 || port < 100) {
                throw new Exception("Invalid parameters");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println(HELP);
            return;
        }

        Console console = System.console();
        String password;
        if (console == null) {
            password = args[2];
        } else {
            System.out.print("Please enter the certificate password: ");
            password = new String(console.readPassword());
        }

        ClientSocket client = new ClientSocket(host, port, password);
        client.connect();

        System.out.println(client.readResponse());

        Scanner sc = new Scanner(System.in);
        String command;
        do {
            System.out.print("> ");
            command = sc.nextLine();
            client.sendRequest(command);
            System.out.println(client.readResponse());
        } while (!ClientSocket.getQuitCommand().equalsIgnoreCase(command));

        client.close();
    }
}
