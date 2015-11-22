package fr.nemolovich.apps.nemolight.security;

import java.io.Serializable;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "user")
public class User implements Serializable {

    /**
     * uid
     */
    private static final long serialVersionUID = -7593826775848734917L;

    private String name;
    private String password;
    private String uid;

    private User() {
        this.name = "guest";
        this.uid = null;
    }

    public User(String name, String password) {
        this.name = name;
        this.uid = UUID.randomUUID().toString();
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
    protected String getPassword() {
        return this.password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the uid
     */
    @XmlElement(name = "UID")
    public String getUID() {
        return this.uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
