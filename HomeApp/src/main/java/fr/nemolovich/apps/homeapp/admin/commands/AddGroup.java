/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;

/**
 *
 * @author Nemolovich
 */
public class AddGroup extends Command {

    public AddGroup() {
        super("group_add", "Add a group to security");
    }

    @Override
    public String getHelp() {
        return String.format(
            "%s%n\tAdd the specific group in security managment%n", getUsage());
    }

    @Override
    public String getUsage() {
        return String.format("%s <GROUP_NAME>", super.getUsage());
    }

    @Override
    public String doCommand(String... args) {
        return null;
    }

}
