package fr.nemolovich.apps.homeapp.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "security")
public class SecurityConfiguration {

    @XmlElementWrapper(name = "groups")
    private final ConcurrentLinkedQueue<Group> groups;

    @XmlTransient
    private static SecurityConfiguration INSTANCE;

    static {
        INSTANCE = new SecurityConfiguration();
    }

    public static final SecurityConfiguration getInstance() {
        return INSTANCE;
    }

    public static final void loadConfig(SecurityConfiguration config) {
        INSTANCE = config;
    }

    private SecurityConfiguration() {
        this.groups = new ConcurrentLinkedQueue();
    }

    /**
     * @return the group
     */
    public final ConcurrentLinkedQueue<Group> getGroups() {
        return groups;
    }

    /**
     * @param group the group to add
     */
    public final void addGroup(Group group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
        }
    }

    /**
     *
     * @param group the group to remove
     */
    public final void removeGroup(Group group) {
        this.groups.remove(group);
    }

    public List<User> getUsers() {
        List<User> result = new ArrayList<>();
        for (Group g : this.groups) {
            result.addAll(g.getUsers());
        }
        return result;
    }

}
