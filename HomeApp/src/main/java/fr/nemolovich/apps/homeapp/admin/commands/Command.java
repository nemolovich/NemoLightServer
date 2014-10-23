/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

/**
 *
 * @author Nemolovich
 */
public abstract class Command implements Comparable<Command> {

    private final String commandName;
    private final String description;

    public Command(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getDescription() {
        return description;
    }

    protected abstract String doCommand(String... args);

    @Override
    public int compareTo(Command command) {
        return this.commandName.compareTo(command.commandName);
    }

}
