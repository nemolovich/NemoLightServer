/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class ShutdownServer extends Command {

    private static final Logger LOGGER = Logger.getLogger(ShutdownServer.class);

    public ShutdownServer() {
        super("shutdown_server", "Stop the web server");
    }

    @Override
    public String doCommand(String... args) {
        LOGGER.info(
            "Server shutting down... bye bye :(");
        System.exit(0);
        return null;
    }

}
