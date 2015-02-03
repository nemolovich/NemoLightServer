/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin;

import com.sun.net.ssl.internal.ssl.Provider;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import java.io.IOException;
import java.security.Security;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class AdminConnection extends Thread {

	private static final Logger LOGGER = Logger.getLogger(AdminConnection.class);
	private final int port;

	public AdminConnection(int port) {
		this.port = port;
	}

	@Override
	public void run() {

		Thread.currentThread().setName("AdminSession");
		Security.addProvider(new Provider());

		System.setProperty("javax.net.ssl.keyStore", String.format("%s%s",
			NemoLightConstants.CONFIG_FOLDER, "certificates/server.ks"));
		System.setProperty("javax.net.ssl.keyStorePassword", "serversslpass");

		SSLServerSocket sslServerSocket;
		try {
			LOGGER.info(String.format(
				"Setting administration connection on port [%s]...",
				String.valueOf(this.port)));
			SSLServerSocketFactory sslServerSocketFactory
				= (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			sslServerSocket = (SSLServerSocket) sslServerSocketFactory.
				createServerSocket(this.port);
			LOGGER.info(String.format(
				"Administration console is available on port [%s]!",
				String.valueOf(this.port)));
		} catch (IOException ex) {
			LOGGER.error(String.format("Can not open socket on port [%s]",
				String.valueOf(this.port)));
			return;
		}
		LOGGER.info("Waiting for client...");
		while (this.isAlive()) {
			try {
				new ClientConnection(sslServerSocket).run();
			} catch (IOException ex) {
				LOGGER.error("Error while listening", ex);
			}
		}
		LOGGER.info("Administration console closed!");
	}
}
