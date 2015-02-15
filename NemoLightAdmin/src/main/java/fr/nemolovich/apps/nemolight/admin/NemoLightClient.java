
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin;

import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.nemolight.admin.socket.ClientSocket;
import fr.nemolovich.apps.nemolight.security.SecurityUtils;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Nemolovich
 */
public class NemoLightClient {

	private static final ConcurrentLinkedQueue<Command> LOCAL_COMMANDS;
	private static final String HELP = "Expected parameters: <HOST> <PORT>";

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
						result = SecurityUtils.getEncryptedPassword(password);
					}
					return result;
				}
			});
	}

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
			if (args.length < 3) {
				System.err.printf("%s <PASSORD>", HELP);
				return;
			} else {
				password = args[2];
			}
		} else {
			if (args.length < 3) {
				System.out.print("Please enter the certificate password: ");
				password = new String(console.readPassword());
			} else {
				password = args[2];
			}
		}

		ClientSocket client = new ClientSocket(host, port, password);
		client.connect();

		System.out.println(client.readResponse());
		System.out.println(getLocalCommandInfo());

		Scanner sc = new Scanner(System.in);
		String commandLine;
		String[] params, tmp;
		do {
			System.out.print("> ");
			commandLine = sc.nextLine();
			tmp = commandLine.split(" ");
			String commandName = tmp[0];
			params = new String[tmp.length - 1];
			System.arraycopy(tmp, 1, params, 0, tmp.length - 1);
			Command command = getLocalCommand(commandName);
			if (command == null) {
				client.sendRequest(commandLine);
				System.out.println(client.readResponse());
				if (commandLine.equals(String.format("%s%s",
					CommandConstants.COMMAND_START,
					CommandConstants.HELP_COMMAND))) {
					System.out.println(getLocalCommandInfo());
				}
			} else {
				System.out.println(command.doCommand(params));
			}
		} while (!ClientSocket.getQuitCommand().equalsIgnoreCase(commandLine));

		client.close();
	}

	private static String getLocalCommandInfo() {
		StringBuilder result = new StringBuilder();
		for (Command command : LOCAL_COMMANDS) {
			result.append(String.format("\t%s\n", command.getCommandName()));
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
}
