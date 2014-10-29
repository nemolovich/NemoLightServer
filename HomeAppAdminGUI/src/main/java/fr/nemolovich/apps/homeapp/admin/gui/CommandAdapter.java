package fr.nemolovich.apps.homeapp.admin.gui;

import fr.nemolovich.apps.homeapp.admin.Command;

abstract class CommandAdapter extends Command {

	public CommandAdapter(String commandName, String description) {
		super(commandName, description);
	}

	@Override
	public abstract String doCommand(String... args);

}
