package fr.nemolovich.apps.homeapp.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "group")
@XmlType(propOrder = { "name", "users" })
public class Group implements Serializable {

	@XmlElement(name = "user")
	@XmlElementWrapper(name = "users")
	private ConcurrentLinkedQueue<User> users;

	private String name;

	private Group() {
		this.users = new ConcurrentLinkedQueue();
	}

	public Group(String name) {
		this();
		this.name = name;
	}

	public void addUser(User user) {
		if (!this.users.contains(user)) {
			this.users.add(user);
		}
	}

	public boolean removeUser(User user) {
		return this.users.remove(user);
	}

	/**
	 * @return the name
	 */
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return new ArrayList<User>(this.users);
	}

}
