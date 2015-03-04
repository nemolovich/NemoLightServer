package fr.nemolovich.apps.nemolight.session;

import fr.nemolovich.apps.nemolight.security.User;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Nemolovich
 */
public class Session {

	private User user;
	private final ConcurrentLinkedQueue<Message> messages;
	private String exceptedPage;

	public Session() {
		this.messages = new ConcurrentLinkedQueue<>();
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
		return exceptedPage;
	}

}
