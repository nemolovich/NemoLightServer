/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;
import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class AddUser extends Command {

    private static final Logger LOGGER = Logger.getLogger(AddUser.class);

    public AddUser() {
        super("user_add", "Add an user to security");
    }

    @Override
    public String getHelp() {
        return String.format(
            "%s%n\tAdd an user in the the specific group in security managment%n",
            getUsage());
    }

    @Override
    public String getUsage() {
        return String.format("%s <GROUP_NAME> <USER_NAME> <ENCRYPTED_PASSWORD>",
            super.getUsage());
    }

    @Override
    public String doCommand(String... args) {
        int returnCode = CommandConstants.EXECUTION_ERROR_CODE;
        if (args.length < 3) {
            returnCode += CommandConstants.SYNTAX_ERROR_CODE;
        } else {
            String groupName = args[0];
            if (!SecurityConfiguration.getInstance().containsGroup(groupName)) {
                returnCode += CommandConstants.GROUP_DOESNT_EXISTS_CODE;
            } else {

                String userName = args[1];
                String password = args[2];
                if (SecurityConfiguration.getInstance().containsUser(
                    groupName, userName)) {
                    returnCode += CommandConstants.USER_ALREADY_EXISTS_CODE;
                } else {
                    if (SecurityConfiguration.getInstance()
                        .addUser(groupName, userName, password)) {
                        try {
                            GlobalSecurity.saveConfig();
                            returnCode = CommandConstants.SUCCESS_CODE;
                        } catch (JAXBException ex) {
                            LOGGER.error("Can not save the new user", ex);
                        }
                    }
                }
            }
        }
        return String.format("%s:%d", CommandConstants.CODE_RESPONSE,
            returnCode);
    }

}
