/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;

/**
 *
 * @author Nemolovich
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Display command list, get some help on command");
    }

    @Override
    protected String doCommand(String... args) {
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
                "Leave admin console")));
        return listCommand.toString();
    }

}
