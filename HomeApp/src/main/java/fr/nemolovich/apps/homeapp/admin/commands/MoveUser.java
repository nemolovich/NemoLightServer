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
public class MoveUser extends Command {

	private static final Logger LOGGER = Logger.getLogger(MoveUser.class);

	public MoveUser() {
		super("user_mv", "Move an user to another in security");
	}

	@Override
	public String getHelp() {
		return String.format(
			"%s%n\tMove an user from a group to another in security managment%n",
			getUsage());
	}

	@Override
	public String getUsage() {
		return String.format("%s <SRC_GROUP_NAME> <USER_NAME> <DST_GROUP_NAME>",
			super.getUsage());
	}

	@Override
	public String doCommand(String... args) {
		int returnCode = CommandConstants.EXECUTION_ERROR_CODE;
		if (args.length < 3) {
			returnCode += CommandConstants.SYNTAX_ERROR_CODE;
		} else {
			String fromGroup = args[0];
			String toGroup = args[2];
			if (!SecurityConfiguration.getInstance().containsGroup(fromGroup)
				|| !SecurityConfiguration.getInstance().containsGroup(toGroup)) {
				returnCode += CommandConstants.GROUP_DOESNT_EXISTS_CODE;
			} else {
				String userName = args[1];
				if (!SecurityConfiguration.getInstance().containsUser(
					fromGroup, userName)) {
					returnCode += CommandConstants.USER_DOESNT_EXISTS_CODE;
				} else if (SecurityConfiguration.getInstance().containsUser(
					toGroup, userName)) {
					returnCode += CommandConstants.USER_ALREADY_EXISTS_CODE;
				} else {
					if (SecurityConfiguration.getInstance()
						.moveUser(fromGroup, userName, toGroup)) {
						try {
							GlobalSecurity.saveConfig();
							returnCode = CommandConstants.SUCCESS_CODE;
						} catch (JAXBException ex) {
							LOGGER.error("Can not move the user", ex);
						}
					}
				}
			}
		}
		return String.format("%s:%d", CommandConstants.CODE_RESPONSE,
			returnCode);
	}

}
