package fr.nemolovich.apps.nemolight.session;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;

/**
 *
 * @author Nemolovich
 */
public class SessionUtils {

	public static void submitMessage(Session session,
		String title, String message,
		MessageSeverity severity) {
		session.submitMessage(new Message(title, message, severity));
	}

	public static void submitMessage(Session session,
		String message) {
		session.submitMessage(new Message(message));
	}

	public static Session getSession(spark.Session session) {
		initSession(session);
		return session.attribute(
			NemoLightConstants.SESSION_ATTR);
	}

	public static void initSession(spark.Session session) {
		Session userSession = session.attribute(
			NemoLightConstants.SESSION_ATTR);
		if (userSession == null) {
			userSession = new Session();
			session.attribute("Session", userSession);
		}
	}
}
