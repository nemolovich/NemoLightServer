package fr.nemolovich.apps.nemolight.session;

import java.util.HashMap;
import spark.Request;

/**
 *
 * @author Nemolovich
 */
public class RequestParameters extends HashMap<String, Object> {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = 3218070446384452016L;

	public RequestParameters(Request request) {
		super();
		for (String param : request.queryParams()) {
			this.put(param, request.queryParams(param));
		}
	}
}
