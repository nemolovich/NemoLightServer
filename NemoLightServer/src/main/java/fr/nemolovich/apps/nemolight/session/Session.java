package fr.nemolovich.apps.nemolight.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.nemolovich.apps.nemolight.security.User;

/**
 *
 * @author Nemolovich
 */
public class Session {

    private static final long serialVersionUID = -646424518559816081L;

    private User user;
    private final ConcurrentLinkedQueue<Message> messages;
    private final Map<String, Object> properties;
    private String expectedPage;

    public Session() {
        super();
        this.messages = new ConcurrentLinkedQueue<>();
        this.properties = new HashMap<>();
    }

    /**
     * Associates the specified value with the specified key in this session. If
     * the session already contains an object for the key, the old value is
     * replaced and returned.
     *
     * @param key {@link String} - The key with which the specified value is to
     * be associated value.
     * @param value {@link Object} - The value to be associated with the
     * specified key.
     * @return {@link Object} - The previous value if there was,
     * <code>null</code> otherwise.
     */
    public Object addProperty(String key, Object value) {
        return this.properties.put(key, value);
    }

    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void submitMessage(Message message) {
        this.messages.add(message);
    }

    public List<Message> getConsumeMessages() {
        List<Message> result = new ArrayList<>(this.messages);
        this.messages.clear();
        return result;
    }

    public ConcurrentLinkedQueue<Message> getMessages() {
        return new ConcurrentLinkedQueue<>(this.messages);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setExpectedPage(String exceptedPage) {
        this.expectedPage = exceptedPage;
    }

    public String getExpectedPage() {
        return this.expectedPage;
    }

    @Override
    public String toString() {
        return String.format("%s[user=%s, expectedPage=%s, messages=%d]", this
            .getClass().getName(),
            this.user == null ? "null" : this.user.getName(),
            this.expectedPage == null ? "null" : this.expectedPage,
            this.messages.size());
    }

}
