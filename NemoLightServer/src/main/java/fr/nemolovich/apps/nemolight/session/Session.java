package fr.nemolovich.apps.nemolight.session;

import fr.nemolovich.apps.nemolight.security.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Nemolovich
 */
public class Session extends HashMap<String, Object> {

	private static final long serialVersionUID
		= -646424518559816081L;

	public static final String ATTR_SESSION_USER = "User";
	public static final String ATTR_SESSION_MSG = "Message";
	public static final String ATTR_SESSION_EXPECTED_PAGE
		= "ExpectedPage";
	public static final String ATTR_SESSION_PROPERTIES
		= "Properties";

	private User user;
	private final ConcurrentLinkedQueue<Message> messages;
	private final Map<String, Object> properties;
	private String exceptedPage;

	public Session() {
		super();
		this.messages = new ConcurrentLinkedQueue<>();
		this.properties = new HashMap<>();
		super.put(ATTR_SESSION_USER, this.user);
		super.put(ATTR_SESSION_MSG, this.messages);
		super.put(ATTR_SESSION_PROPERTIES,
			this.properties);
		super.put(ATTR_SESSION_EXPECTED_PAGE,
			this.exceptedPage);
	}

	/**
	 * Use
	 * {@link #addProperty(java.lang.String, java.lang.Object)}
	 * instead of this method.
	 *
	 * @return Nothing because of throw.
	 * @deprecated
	 * @see #addProperty(java.lang.String, java.lang.Object)
	 */
	@Deprecated
	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException(
			"This method can not be used for this object");
	}

	/**
	 * Associates the specified value with the specified key
	 * in this session. If the session already contains an
	 * object for the key, the old value is replaced and
	 * returned.
	 *
	 * @param key {@link String} - The key with which the
	 * specified value is to be associated value.
	 * @param value {@link Object} - The value to be
	 * associated with the specified key.
	 * @return {@link Object} - The previous value if there
	 * was, <code>null</code> otherwise.
	 */
	public Object addProperty(String key, Object value) {
		return this.properties.put(key, value);
	}

	public Object getProperty(String name) {
		return this.properties.get(name);
	}

	public void submitMessage(Message message) {
		this.messages.add(message);
	}

	public void consumeMessages() {
		this.messages.clear();
	}

	public ConcurrentLinkedQueue<Message> getMessages() {
		return this.messages;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public void setExceptedPage(String exceptedPage) {
		this.exceptedPage = exceptedPage;
	}

	public String getExceptedPage() {
		return this.exceptedPage;
	}

}
