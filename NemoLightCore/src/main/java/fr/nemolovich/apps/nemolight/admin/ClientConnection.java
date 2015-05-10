package fr.nemolovich.apps.nemolight.admin;

import fr.nemolovich.apps.nemolight.admin.commands.CommandManager;
import fr.nemolovich.apps.nemolight.admin.commands.UnkownCommandException;
import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.Arrays;
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
        this.writer = new PrintWriter(new OutputStreamWriter(
            client.getOutputStream(), Charset.defaultCharset()), true);
        this.reader = new BufferedReader(
            new InputStreamReader(client.getInputStream(),
                Charset.defaultCharset()));

        String listCommand = "";
        try {
            listCommand = CommandManager.execute(CommandConstants.HELP_COMMAND);
        } catch (UnkownCommandException ex) {
            LOGGER.warn("Can not find help command", ex);
        }

        this.writer.printf("Welcome on administration management!%s%s",
            listCommand, String.valueOf(CommandConstants.MESSAGE_END));
        this.writer.flush();
    }

    public void run() throws IOException {
        String request = "";
        boolean listen = true;
        while (request != null
            && !request.equals(CommandConstants.QUIT_COMMAND) && listen) {
            request = this.reader.readLine();
            String response;
            if (request != null && request.startsWith(String.valueOf(
                CommandConstants.COMMAND_START))) {
                request = request.substring(1);
                String[] line = request.split(" ");
                String command = line[0];
                String[] args = Arrays.copyOfRange(line, 1, line.length);
                LOGGER.info(String.format("Client command: %s", command));

                if (CommandConstants.QUIT_COMMAND.equals(command)) {
                    response = "Good bye dude!";
                    listen = false;
                } else {
                    try {
                        response = CommandManager.execute(command, args);
                    } catch (UnkownCommandException ex) {
                        response = ex.getMessage();
                    }
                }
            } else {
                LOGGER.info(String.format("Client message: %s", request));
                response = String.format("Huhu? oO? What are you saying? %s",
                    CommandConstants.HELP_MESSAGE);
            }
            this.writer.printf("%s%s", response,
                String.valueOf(CommandConstants.MESSAGE_END));
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
