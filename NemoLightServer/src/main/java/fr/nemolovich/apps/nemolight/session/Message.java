package fr.nemolovich.apps.nemolight.session;

/**
 *
 * @author Nemolovich
 */
public class Message {

	private final String title;
	private final String message;
	private final MessageSeverity severity;

	public Message(String message) {
		this(message == null ? ""
			: message.substring(0, 17).concat("..."),
			message, MessageSeverity.DEFAULT);
	}

	public Message(String title, String message,
		MessageSeverity severity) {
		this.title = title == null ? "" : title;
		this.message = message == null ? "" : message;
		this.severity = severity == null
			? MessageSeverity.DEFAULT : severity;
	}

	public String getTitle() {
		return this.title;
	}

	public String getMessage() {
		return this.message;
	}

	public MessageSeverity getSeverity() {
		return this.severity;
	}
}
