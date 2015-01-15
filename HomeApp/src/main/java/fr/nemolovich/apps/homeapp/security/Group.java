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
@XmlType(propOrder = {"name", "users"})
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

    public boolean addUser(User user) {
        boolean result = false;
        if (!this.users.contains(user)) {
            result = this.users.add(user);
        }
        return result;
    }

    public boolean removeUser(String userName) {
        User user = getUser(userName);
        return user != null && this.users.remove(user);
    }

    /**
     * @return the name
     */
    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return new ArrayList<>(this.users);
    }

    private User getUser(String userName) {
        User result = null;
        for (User user : this.users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                result = user;
            }
        }
        return result;
    }

    public boolean addUser(String userName, String password) {
        boolean result = false;
        User user = new User(userName, password);
        if (!this.users.contains(user)) {
            result = this.users.add(user);
        }
        return result;
    }

    public boolean containsUser(String userName) {
        return getUser(userName) != null;
    }

}
