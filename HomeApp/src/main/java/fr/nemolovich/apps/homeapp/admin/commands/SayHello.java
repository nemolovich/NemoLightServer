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
public class SayHello extends Command {

    public SayHello() {
        super("hello", "Be polite! Say Hello!");
    }

    @Override
    protected String doCommand(String... args) {
        return "Hello guy!";
    }

}
