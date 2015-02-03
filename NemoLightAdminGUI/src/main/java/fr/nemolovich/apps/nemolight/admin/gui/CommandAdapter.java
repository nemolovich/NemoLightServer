package fr.nemolovich.apps.nemolight.admin.gui;

import fr.nemolovich.apps.nemolight.admin.Command;

abstract class CommandAdapter extends Command {

	public CommandAdapter(String commandName, String description) {
		super(commandName, description);
	}

	@Override
	public abstract String doCommand(String... args);

}
