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
public class UnkownCommand extends Exception {

    public UnkownCommand(String command) {
        super("Unknown command '".concat(command).concat("'"));
    }

}
