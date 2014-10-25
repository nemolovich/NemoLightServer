/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin;

import com.sun.net.ssl.internal.ssl.Provider;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
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

        Security.addProvider(new Provider());

        System.setProperty("javax.net.ssl.keyStore",
            HomeAppConstants.CONFIG_FOLDER.concat("certificates/server.ks"));
        System.setProperty("javax.net.ssl.keyStorePassword", "serversslpass");

        SSLServerSocket sslServerSocket;
        try {
            LOGGER.info("Setting administration connection on port ["
                .concat(String.valueOf(this.port)).concat("]..."));
            SSLServerSocketFactory sslServerSocketFactory
                = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.
                createServerSocket(this.port);
            LOGGER.info("Administration console is available on port ["
                .concat(String.valueOf(this.port)).concat("]!"));
        } catch (IOException ex) {
            LOGGER.error("Can not open socket on port [".concat(
                String.valueOf(this.port)).concat("]"));
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
