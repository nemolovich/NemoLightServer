package fr.nemolovich.apps.nemolight.route;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.file.FileRoute;
import fr.nemolovich.apps.nemolight.security.GlobalSecurity;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import fr.nemolovich.apps.nemolight.security.User;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public abstract class WebRoute extends Route {

	private static WebRoute LOGIN_PAGE;
	private static WebRoute HOME_PAGE;
	private final String route;
	private boolean secured;

	public WebRoute(String path) {
		super(path);
		this.route = path;
	}

	public static final void setLoginPage(WebRoute loginPage) {
		LOGIN_PAGE = loginPage;
	}

	public static final void setHomePage(WebRoute homePage) {
		HOME_PAGE = homePage;
	}

	public final void disableSecurity() {
		this.secured = false;
	}

	public final void enableSecurity() {
		this.secured = true;
	}

	public final String getPath() {
		return this.route;
	}

	@Override
	public final Object handle(Request request, Response response) {
		Session session = request.session(true);
		User user = session.attribute(NemoLightConstants.USER_ATTR);

		String loginPath = LOGIN_PAGE == null ? null : LOGIN_PAGE.getPath();
		String expectedPage = request.pathInfo();
		if (!FileRoute.class.isAssignableFrom(this.getClass())) {
			String UID = request.cookie(NemoLightConstants.SESSION_COOKIE);
			// LOGGER
			if (UID != null) {
				user = SecurityConfiguration.getInstance().getUserByUID(UID);
			}
		}

		Object result = null;
		if (securityIsNeeded(loginPath, expectedPage, user)) {
			session.attribute(NemoLightConstants.EXPECTED_PAGE_ATTR,
					expectedPage);
			// LOGGER
			response.redirect(loginPath);
		} else {
			if (user != null) {
				response.cookie("/", NemoLightConstants.SESSION_COOKIE,
						user.getUID(), NemoLightConstants.COOKIE_TIME / 10,
						false);
			}
			result = doHandle(request, response);
			// session.invalidate();
		}
		return result;
	}

	private boolean securityIsNeeded(String loginPath, String expectedPath,
			User user) {
		return GlobalSecurity.isEnabled()
				&& this.secured
				&& ((loginPath != null && !loginPath.equals(expectedPath)) && user == null);
	}

	public abstract Object doHandle(Request request, Response response);

}
