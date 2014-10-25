/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin;

import fr.nemolovich.apps.homeapp.admin.socket.ClientSocket;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Nemolovich
 */
public class HomeAppClient {

    public static void main(String[] args) throws IOException {
        ClientSocket client = new ClientSocket("localhost", 8081, "serversslpass");
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
