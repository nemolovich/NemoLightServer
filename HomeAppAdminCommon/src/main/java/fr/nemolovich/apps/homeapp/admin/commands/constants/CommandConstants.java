/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands.constants;

/**
 *
 * @author Nemolovich
 */
public interface CommandConstants {

    public static final String QUIT_DESC = "Leave admin console";
    public static final String QUIT_COMMAND = "quit";
    public static final String HELP_COMMAND = "help";
    public static final char COMMAND_START = '/';
    public static final String HELP_MESSAGE = "Try to type ".concat(
        String.valueOf(COMMAND_START)).concat(HELP_COMMAND)
        .concat("to get some help.");
    public static final char MESSAGE_END = '\\';
}
