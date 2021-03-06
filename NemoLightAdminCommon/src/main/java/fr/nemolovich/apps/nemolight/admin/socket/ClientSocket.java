/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.socket;

import com.sun.net.ssl.internal.ssl.Provider;
import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.regex.Matcher;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Nemolovich
 */
public class ClientSocket {

	private static final String SSL_CERT_FILE_PATH
		= "security/certificates/client.ks";
	private static final String SSL_CERT_PATH_LOCAL
		= String.format("resources/%s", SSL_CERT_FILE_PATH);
	private static final String SSL_CERT_PATH_NB
		= String.format("src/main/resources/fr/nemolovich/apps/nemolight/admin/%s",
			SSL_CERT_FILE_PATH);

	private final String hostname;
	private final int port;
	private Socket socketClient;
	private PrintWriter writer;
	private BufferedReader reader;
	private final String password;
	private ISocketLogger logger = new ConsoleLogger();

	public ClientSocket(String hostname, int port, String passsword) {
		this.hostname = hostname;
		this.port = port;
		this.password = passsword;
	}

	public void setLogger(ISocketLogger logger) {
		this.logger = logger;
	}

	public void connect() throws UnknownHostException, IOException {

		logger.info(String.format("Attempting to connect to %s:%d%n",
			this.hostname, this.port));

		String path;

		if (new File(SSL_CERT_PATH_LOCAL).exists()) {
			path = SSL_CERT_PATH_LOCAL;
		} else {
			if (new File(SSL_CERT_PATH_NB).exists()) {
				path = SSL_CERT_PATH_NB;
			} else {
				throw new IOException("Can not locate the trust store file");
			}
		}

		System.setProperty("javax.net.ssl.trustStore", path);
		System.setProperty("javax.net.ssl.trustStorePassword", this.password);

		Security.addProvider(new Provider());
		SSLSocketFactory sslSocketFactory
			= (SSLSocketFactory) SSLSocketFactory.getDefault();

		this.socketClient = (SSLSocket) sslSocketFactory.createSocket(
			this.hostname, this.port);

		this.writer = new PrintWriter(
			this.socketClient.getOutputStream(), true);

		this.reader = new BufferedReader(new InputStreamReader(
			this.socketClient.getInputStream()));

		logger.info(String.format("Connection Established%n"));

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
		String response = result.toString();
		Matcher matcher = CommandConstants.CODE_RESPONSE_PATTERN.matcher(
			response);
		if (matcher.find()) {
			String codeValue = matcher.group("code");
			if (codeValue != null) {
				int code = Integer.valueOf(codeValue);
				boolean error = false;
				boolean warning = false;
				if ((code | CommandConstants.EXECUTION_ERROR_CODE) == code) {
					error = true;
				}
				if ((code | CommandConstants.EXECUTION_WARNING_CODE) == code) {
					warning = true;
				}
				if (error) {
					response = "ERROR: ";
				} else if (warning) {
					response = "WARNING: ";
				} else {
					response = "INFO: ";
				}
				if (code == CommandConstants.SUCCESS_CODE) {
					response += "Command succeed";
				}
				if ((code | CommandConstants.SYNTAX_ERROR_CODE) == code) {
					response += "Syntax error";
				}
				if ((code | CommandConstants.GROUP_ALREADY_EXISTS_CODE) == code) {
					response += "The group already exists";
				}
				if ((code | CommandConstants.GROUP_DOESNT_EXISTS_CODE) == code) {
					response += "The group does not exist";
				}
				if ((code | CommandConstants.USER_ALREADY_EXISTS_CODE) == code) {
					response += "The user already exists";
				}
				if ((code | CommandConstants.USER_DOESNT_EXISTS_CODE) == code) {
					response += "The user does not exist";
				}
			}
		}

		return response;
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
		return String.format("%s%s",
			String.valueOf(CommandConstants.COMMAND_START),
			CommandConstants.QUIT_COMMAND);
	}

}
