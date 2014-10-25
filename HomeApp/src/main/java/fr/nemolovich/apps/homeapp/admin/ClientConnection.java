/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin;

import fr.nemolovich.apps.homeapp.admin.commands.CommandManager;
import fr.nemolovich.apps.homeapp.admin.commands.UnkownCommand;
import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class ClientConnection {

    private static final Logger LOGGER = Logger.getLogger(ClientConnection.class);

    private final PrintWriter writer;
    private final BufferedReader reader;

    ClientConnection(ServerSocket socket) throws IOException {
        SSLSocket client = (SSLSocket) socket.accept();
        LOGGER.info("Client connected!");
        this.writer = new PrintWriter(
            client.getOutputStream(), true);
        this.reader = new BufferedReader(
            new InputStreamReader(client.getInputStream()));

        String listCommand = "";
        try {
            listCommand = CommandManager.execute(CommandConstants.HELP_COMMAND);
        } catch (UnkownCommand ex) {
            LOGGER.warn("Can not find help command");
        }

        this.writer.printf("Welcome on administration management!"
            .concat(listCommand)
            .concat(String.valueOf(CommandConstants.MESSAGE_END)));
        this.writer.flush();
    }

    public void run() throws IOException {
        String command = "";
        boolean listen = true;
        while (!command.equals(CommandConstants.QUIT_COMMAND) && listen) {
            command = this.reader.readLine();
            String response;
            if (command.startsWith(String.valueOf(
                CommandConstants.COMMAND_START))) {
                command = command.substring(1);
                LOGGER.info(String.format("Client command: %s", command));

                if (CommandConstants.QUIT_COMMAND.equals(command)) {
                    response = "Good bye dude!";
                    listen = false;
                } else {
                    try {
                        response = CommandManager.execute(command);
                    } catch (UnkownCommand ex) {
                        response = ex.getMessage();
                    }
                }
            } else {
                LOGGER.info(String.format("Client message: %s", command));
                response = "Huhu? oO? What are you saying? ".concat(
                    CommandConstants.HELP_MESSAGE);
            }
            this.writer.printf(response.concat(String.valueOf(
                CommandConstants.MESSAGE_END)));
            this.writer.flush();
        }
        this.close();
    }

    private void close() throws IOException {
        this.reader.close();
        this.writer.close();
        LOGGER.info("Client disconnected!");
    }
}
