/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.commands;

import fr.nemolovich.apps.homeapp.admin.Command;
import fr.nemolovich.apps.homeapp.security.Group;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class ListUser extends Command {

	private static final Logger LOGGER = Logger.getLogger(ListUser.class);

	public ListUser() {
		super("user_list", "List all the security users");
	}

	@Override
	public String getHelp() {
		return String.format(
			"%s%n\tList the users from security managment (groups name filter can be used)%n",
			getUsage());
	}

	@Override
	public String getUsage() {
		return String.format("%s [<GROUP_NAME),]", super.getUsage());
	}

	@Override
	public String doCommand(String... args) {
		StringBuilder result = new StringBuilder();
		List<Group> groups = SecurityConfiguration.getInstance().getGroups();
		if (groups.isEmpty()) {
			result.append("There is no users in security management list");
		} else {
			List<String> groupsName = new ArrayList<>(Arrays.asList(args));
			for (String group : args) {
				if (!SecurityConfiguration.getInstance().containsGroup(group)) {
					result.append(String.format("The group '%s' does not exist\n",
						group));
					groupsName.remove(group);
				}
			}
			boolean filterGroups = groupsName.size() > 0;
			String headerEnd = filterGroups
				? (" (in groups: ".concat(Arrays.toString(
						groupsName.toArray())).concat(")"))
				: "";
			result.append(String.format("Server security users%s:\n",
				headerEnd));
			for (Group group : groups) {
				if (!filterGroups
					|| (filterGroups && groupsName.contains(group.getName()))) {
					for (User user : group.getUsers()) {
						result.append(String.format("\t%-15s(%s)\n",
							user.getName(),
							group.getName()));
					}
				}
			}
		}
		return result.toString();
	}

}
