/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.nemolight.security.GlobalSecurity;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class AddGroup extends Command {

    private static final Logger LOGGER = Logger.getLogger(AddGroup.class);

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
        int returnCode = CommandConstants.EXECUTION_ERROR_CODE;
        if (args.length < 1) {
            returnCode += CommandConstants.SYNTAX_ERROR_CODE;
        } else {
            String groupName = args[0];
            if (SecurityConfiguration.getInstance().containsGroup(groupName)) {
                returnCode += CommandConstants.GROUP_ALREADY_EXISTS_CODE;
            } else {
                if (SecurityConfiguration.getInstance().addGroup(groupName)) {
                    try {
                        GlobalSecurity.saveConfig();
                        returnCode = CommandConstants.SUCCESS_CODE;
                    } catch (JAXBException ex) {
                        LOGGER.error("Can not save the new group", ex);
                    }
                }
            }
        }
        return String.format("%s:%d", CommandConstants.CODE_RESPONSE,
            returnCode);
    }

}
