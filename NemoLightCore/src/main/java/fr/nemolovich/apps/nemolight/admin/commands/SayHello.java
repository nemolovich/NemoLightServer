package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.admin.Command;

/**
 *
 * @author Nemolovich
 */
public class SayHello extends Command {

    public SayHello() {
        super("hello", "Be polite! Say Hello!");
    }

    @Override
    public String getHelp() {
        return String.format("\t%s%n", this.getDescription());
    }

    @Override
    public String doCommand(String... args) {
        return "Hello guy!";
    }

}
