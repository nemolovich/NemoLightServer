/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;
import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;

/**
 *
 * @author Nemolovich
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Display command list or give help on command");
    }

    @Override
    public String getHelp() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s%n\t%s", getUsage(),
            this.getDescription()));
        return result.toString();
    }

    @Override
    public String getUsage() {
        return super.getUsage().concat(" [<COMMAND_NAME>]");
    }

    @Override
    public String doCommand(String... args) {
        String result = null;
        if (args.length == 0) {
            StringBuilder listCommand = new StringBuilder();
            listCommand.append("%nAvailable commands:");
            for (Command command : CommandManager.getCommands()) {
                listCommand.append("%n".concat(
                    String.format("\t%-20s\t%-40s",
                        String.valueOf(CommandConstants.COMMAND_START).concat(
                            command.getCommandName()),
                        command.getDescription())));
            }
            listCommand.append("%n".concat(
                String.format("\t%-20s\t%-40s",
                    String.valueOf(CommandConstants.COMMAND_START).concat(
                        CommandConstants.QUIT_COMMAND),
                    CommandConstants.QUIT_DESC)));
            result = listCommand.toString();
        } else {
            String commandName = args[0];
            if (commandName.equals(CommandConstants.QUIT_COMMAND)) {
                result = String.format("\t%s", CommandConstants.QUIT_DESC);
            } else {
                boolean found = false;
                for (Command command : CommandManager.getCommands()) {
                    if (command.getCommandName().equals(commandName)) {
                        result = command.getHelp();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    result = new UnkownCommandException(commandName).getMessage();
                }
            }
        }
        return result;
    }

}
