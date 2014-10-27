/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class CommandManager {

    private static final Logger LOGGER = Logger.getLogger(CommandManager.class);
    private static final List<Command> COMMANDS;

    static {
        COMMANDS = new ArrayList<>();
        COMMANDS.add(new HelpCommand());
        COMMANDS.add(new ShutdownServer());
        COMMANDS.add(new SayHello());
        COMMANDS.add(new AddGroup());

        Collections.sort(COMMANDS);
    }

    public static List<String> getCommandsList() {
        List<String> result = new ArrayList<>();
        for (Command command : COMMANDS) {
            result.add(command.getCommandName());
        }
        return result;
    }

    public static Command getCommand(String commandName) {
        Command result = null;
        for (Command command : COMMANDS) {
            if (command.getCommandName().equalsIgnoreCase(commandName)) {
                result = command;
                break;
            }
        }
        return result;
    }

    public static String execute(String commandName, String... args)
        throws UnkownCommandException {
        String result;
        Command command = getCommand(commandName);
        if (command == null) {
            throw new UnkownCommandException(commandName);
        } else {
            result = command.doCommand(args);
        }
        return result;
    }

    public static List<Command> getCommands() {
        return COMMANDS;
    }
}
