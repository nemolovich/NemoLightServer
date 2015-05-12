package fr.nemolovich.apps.nemolight.admin;

import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.nemolight.admin.socket.ClientSocket;
import fr.nemolovich.apps.nemolight.security.CommonUtils;
import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Nemolovich
 */
public final class NemoLightClient {

    private static final ConcurrentLinkedQueue<Command> LOCAL_COMMANDS;
    private static final String HELP = "Expected parameters: <HOST> <PORT>";
    private static final String MSG_TEMPLATE = "%s\n";
    private static final int MIN_PORT = 100;
    private static final PrintStream ERR;
    private static final PrintStream OUT;

    static {

        LOCAL_COMMANDS = new ConcurrentLinkedQueue<>();
        LOCAL_COMMANDS.add(new Command("/get_encrypted_paswword",
            "Display the encrypted password from password") {

                @Override
                public String doCommand(String... args) {
                    String result;
                    if (args.length < 1) {
                        result = "Please provide a password to encrypt";
                    } else {
                        String password = args[0];
                        result = CommonUtils.getEncryptedPassword(password);
                    }
                    return result;
                }
            });

        ERR = System.err;
        OUT = System.out;
    }

    private NemoLightClient() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            err(MSG_TEMPLATE, HELP);
            return;
        }
        String host;
        int port;
        try {
            host = args[0];
            port = Integer.valueOf(args[1]);
            if (host.length() < 2 || port < MIN_PORT) {
                throw new InvalidParameterException("Invalid parameters");
            }
        } catch (InvalidParameterException ex) {
            err(MSG_TEMPLATE, ex.getMessage());
            err(MSG_TEMPLATE, HELP);
            return;
        }

        Console console = System.console();
        String password;
        if (console == null) {
            if (args.length < 3) {
                err("%s <PASSORD>", HELP);
                return;
            } else {
                password = args[2];
            }
        } else {
            if (args.length < 3) {
                log("Please enter the certificate password: ");
                password = new String(console.readPassword());
            } else {
                password = args[2];
            }
        }

        ClientSocket client = new ClientSocket(host, port, password);
        client.connect();

        log(MSG_TEMPLATE, client.readResponse());
        log(MSG_TEMPLATE, getLocalCommandInfo());

        Scanner sc = new Scanner(System.in, Charset.defaultCharset().name());
        String commandLine;
        String[] params, tmp;
        do {
            log("> ");
            commandLine = sc.nextLine();
            tmp = commandLine.split(" ");
            String commandName = tmp[0];
            params = new String[tmp.length - 1];
            System.arraycopy(tmp, 1, params, 0, tmp.length - 1);
            Command command = getLocalCommand(commandName);
            if (command == null) {
                client.sendRequest(commandLine);
                log(MSG_TEMPLATE, client.readResponse());
                if (commandLine.equals(String.format("%s%s",
                    CommandConstants.COMMAND_START,
                    CommandConstants.HELP_COMMAND))) {
                    log(MSG_TEMPLATE, getLocalCommandInfo());
                }
            } else {
                log(MSG_TEMPLATE, command.doCommand(params));
            }
        } while (!ClientSocket.getQuitCommand().equalsIgnoreCase(commandLine));

        client.close();
    }

    private static String getLocalCommandInfo() {
        StringBuilder result = new StringBuilder();
        for (Command command : LOCAL_COMMANDS) {
            result.append(String.format("\t".concat(MSG_TEMPLATE),
                command.getCommandName()));
        }
        if (result.toString().endsWith("\n")) {
            result = new StringBuilder(result.substring(0, result.length() - 1));
        }
        return result.toString();
    }

    private static Command getLocalCommand(String commandName) {
        Command result = null;
        for (Command command : LOCAL_COMMANDS) {
            if (command.getCommandName().equals(commandName)) {
                result = command;
                break;
            }
        }
        return result;
    }

    private static void log(String message, Object... args) {
        OUT.printf(message, args);
    }

    private static void err(String message, Object... args) {
        ERR.printf(message, args);
    }
}
