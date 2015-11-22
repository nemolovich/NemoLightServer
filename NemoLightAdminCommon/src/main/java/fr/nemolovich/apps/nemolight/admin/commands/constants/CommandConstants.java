package fr.nemolovich.apps.nemolight.admin.commands.constants;

import java.util.regex.Pattern;

/**
 *
 * @author Nemolovich
 */
public interface CommandConstants {

    String QUIT_DESC = "Leave admin console";
    String QUIT_COMMAND = "quit";
    String HELP_COMMAND = "help";
    char COMMAND_START = '/';
    String HELP_MESSAGE = String.format(
        "Try to type %s%s to get some help.",
        String.valueOf(COMMAND_START), HELP_COMMAND);
    char MESSAGE_END = '\\';
    String CODE_RESPONSE = "CODE";
    Pattern CODE_RESPONSE_PATTERN
        = Pattern.compile(String.format("^%s:(?<code>\\d+)$", CODE_RESPONSE));
    int SUCCESS_CODE = 0;
    int EXECUTION_ERROR_CODE = 1;
    int EXECUTION_WARNING_CODE = 2;
    int SYNTAX_ERROR_CODE = 4;
    int GROUP_ALREADY_EXISTS_CODE = 8;
    int GROUP_DOESNT_EXISTS_CODE = 16;
    int USER_ALREADY_EXISTS_CODE = 32;
    int USER_DOESNT_EXISTS_CODE = 64;

}
