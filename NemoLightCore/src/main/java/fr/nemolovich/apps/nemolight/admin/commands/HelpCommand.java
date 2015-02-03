/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;

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
		result.append(String.format("%s%n\t%s%n", getUsage(),
			this.getDescription()));
		return result.toString();
	}

	@Override
	public String getUsage() {
		return String.format("%s [<COMMAND_NAME>]", super.getUsage());
	}

	@Override
	public String doCommand(String... args) {
		String result = null;
		if (args.length == 0) {
			StringBuilder listCommand = new StringBuilder();
			listCommand.append("Available commands:");
			for (Command command : CommandManager.getCommands()) {
				listCommand.append(String.format("%n\t%-20s\t%-40s",
					String.valueOf(CommandConstants.COMMAND_START).concat(
						command.getCommandName()),
					command.getDescription()));
			}
			listCommand.append(
				String.format("%n\t%-20s\t%-40s",
					String.valueOf(CommandConstants.COMMAND_START).concat(
						CommandConstants.QUIT_COMMAND),
					CommandConstants.QUIT_DESC));
			result = String.format(listCommand.toString());
		} else {
			String commandName = args[0];
			if (commandName.equals(CommandConstants.QUIT_COMMAND)) {
				result = String.format("\t%s%n", CommandConstants.QUIT_DESC);
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
					result = String.format("%s%n",
						new UnkownCommandException(commandName).getMessage());
				}
			}
		}
		return result;
	}

}
