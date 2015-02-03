package fr.nemolovich.apps.nemolight.security;

import java.io.Serializable;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "user")
public class User implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -7593826775848734917L;

	private String name;
	private String password;
	private String UID;

	private User() {
		this.name = "guest";
		this.UID = null;
	}

	public User(String name, String password) {
		this.name = name;
		this.UID = UUID.randomUUID().toString();
		this.password = password;
	}

	/**
	 * @return the name
	 */
	@XmlElement(name = "name")
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	@XmlTransient
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the UID
	 */
	@XmlElement(name = "UID")
	public String getUID() {
		return this.UID;
	}

	/**
	 * @param UID the UID to set
	 */
	public void setUID(String UID) {
		this.UID = UID;
	}

}
