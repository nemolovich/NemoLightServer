package fr.nemolovich.apps.homeapp.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "security")
public class SecurityConfiguration {

	@XmlElementWrapper(name = "groups")
	private ConcurrentLinkedQueue<Group> group;

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
		this.group = new ConcurrentLinkedQueue();
	}

	/**
	 * @return the group
	 */
	public final ConcurrentLinkedQueue<Group> getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to add
	 */
	public final void addGroup(Group group) {
		if (!this.group.contains(group)) {
			this.group.add(group);
		}
	}

	/**
	 * 
	 * @param group
	 *            the group to remove
	 */
	public final void removeGroup(Group group) {
		this.group.remove(group);
	}

}
