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
	public final List<Group> getGroups() {
		return new ArrayList(this.groups);
	}

	/**
	 * @param groupName the name of the group to add
	 * @return {@link Boolean boolean} - <code>true</code>
	 * if the group has been added in the security
	 * configuration, <code>false</code> otherwise.
	 */
	public final boolean addGroup(String groupName) {
		boolean result = false;
		Group group = new Group(groupName);
		if (!this.groups.contains(group)) {
			result = this.groups.add(group);
		}
		return result;
	}

	private Group getGroup(String groupName) {
		Group result = null;
		for (Group group : this.groups) {
			if (group.getName().equalsIgnoreCase(groupName)) {
				result = group;
				break;
			}
		}
		return result;
	}

	public User getUser(String userName) {
		User result = null;
		grouploop:
		for (Group group : this.groups) {
			for (User user : group.getUsers()) {
				if (user.getName().equalsIgnoreCase(userName)) {
					result = user;
					break grouploop;
				}
			}
		}
		return result;
	}

	public final boolean addUser(String groupName, String userName,
		String password) {
		boolean added = false;
		Group group = getGroup(groupName);
		if (group != null) {
			if (!group.containsUser(userName)) {
				added = group.addUser(userName, password);
			}
		}
		return added;
	}

	public final boolean moveUser(String srcGroup, String userName,
		String dstGroup) {
		boolean moved = false;
		Group fromGroup = getGroup(srcGroup);
		Group toGroup = getGroup(dstGroup);
		if (fromGroup != null) {
			if (fromGroup.containsUser(userName)) {
				User user = getUser(userName);
				if (fromGroup.removeUser(userName)) {
					moved = toGroup.addUser(user);
				}
			}
		}
		return moved;
	}

	public final boolean removeGroup(String groupName) {
		boolean result = false;
		Group group = this.getGroup(groupName);
		if (group != null) {
			result = this.groups.remove(group);
		}
		return result;
	}

	public final boolean removeUser(String groupName, String userName) {
		boolean result = false;

		grouploop:
		for (Group group : this.groups) {
			if (group.getName().equalsIgnoreCase(groupName)) {
				for (User user : group.getUsers()) {
					if (user.getName().equalsIgnoreCase(userName)) {
						result = group.removeUser(user.getName());
						break grouploop;
					}
				}
			}
		}

		return result;
	}

	public List<User> getUsers() {
		List<User> result = new ArrayList<>();
		for (Group g : this.groups) {
			result.addAll(g.getUsers());
		}
		return result;
	}

	public final boolean containsGroup(String groupName) {
		return this.getGroup(groupName) != null;
	}

	public final void reset() {
		this.groups.clear();
	}

	public boolean containsUser(String groupName, String userName) {
		Group group = this.getGroup(groupName);
		return group != null && group.containsUser(userName);
	}

}
