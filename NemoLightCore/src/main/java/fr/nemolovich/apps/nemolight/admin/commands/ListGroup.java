/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.security.Group;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class ListGroup extends Command {

    private static final Logger LOGGER = Logger.getLogger(ListGroup.class);

    public ListGroup() {
        super("group_list", "List all the security groups");
    }

    @Override
    public String getHelp() {
        return String.format(
            "%s%n\tList the groups from security managment%n",
            getUsage());
    }

    @Override
    public String getUsage() {
        return String.format("%s <GROUP_NAME>", super.getUsage());
    }

    @Override
    public String doCommand(String... args) {
        StringBuilder result = new StringBuilder();
        List<Group> groups = SecurityConfiguration.getInstance().getGroups();
        if (groups.isEmpty()) {
            result.append("There is no group in security management list");
        } else {
            result.append("Server security groups:\n");
            for (Group group : groups) {
                result.append(String.format("\t%-15s(%d user(s))\n",
                    group.getName(),
                    group.getUsers().size()));
            }
        }
        return result.toString();
    }

}
