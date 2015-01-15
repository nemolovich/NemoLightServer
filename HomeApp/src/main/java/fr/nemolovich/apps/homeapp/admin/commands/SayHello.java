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
