/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.commands.constants;

import java.util.regex.Pattern;

/**
 *
 * @author Nemolovich
 */
public interface CommandConstants {

    public static final String QUIT_DESC = "Leave admin console";
    public static final String QUIT_COMMAND = "quit";
    public static final String HELP_COMMAND = "help";
    public static final char COMMAND_START = '/';
    public static final String HELP_MESSAGE = String.format(
        "Try to type %s%s to get some help.",
        String.valueOf(COMMAND_START), HELP_COMMAND);
    public static final char MESSAGE_END = '\\';
    public static final String CODE_RESPONSE = "CODE";
    public static final Pattern CODE_RESPONSE_PATTERN
        = Pattern.compile(String.format("^%s:(?<code>\\d+)$", CODE_RESPONSE));
    public static final int SUCCESS_CODE = 0;
    public static final int EXECUTION_ERROR_CODE = 1;
    public static final int EXECUTION_WARNING_CODE = 2;
    public static final int SYNTAX_ERROR_CODE = 4;
    public static final int GROUP_ALREADY_EXISTS_CODE = 8;
    public static final int GROUP_DOESNT_EXISTS_CODE = 16;
    public static final int USER_ALREADY_EXISTS_CODE = 32;
    public static final int USER_DOESNT_EXISTS_CODE = 64;

}
