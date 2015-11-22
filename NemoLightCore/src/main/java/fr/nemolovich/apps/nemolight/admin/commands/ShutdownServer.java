package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.admin.Command;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class ShutdownServer extends Command {

    private static final Logger LOGGER
        = Logger.getLogger(ShutdownServer.class);

    public ShutdownServer() {
        super("shutdown_server", "Stop the web server");
    }

    @Override
    public String getHelp() {
        return String.format("\t%s%n", this.getDescription());
    }

    @Override
    public String doCommand(String... args) {
        LOGGER.info("Server shutting down... bye bye :(");
        System.exit(0);
        return null;
    }

}
