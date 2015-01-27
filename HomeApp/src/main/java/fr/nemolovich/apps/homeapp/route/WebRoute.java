package fr.nemolovich.apps.homeapp.route;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.security.User;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public abstract class WebRoute extends Route {

	private static WebRoute LOGIN_PAGE;
	private final String route;
	private boolean secured;

	public WebRoute(String path) {
		super(path);
		this.route = path;
	}

	public static final void setLoginPage(WebRoute loginPage) {
		LOGIN_PAGE = loginPage;
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
		User user = session.attribute(
			HomeAppConstants.USER_ATTR);

		String loginPath = LOGIN_PAGE == null ? null
			: LOGIN_PAGE.getPath();

		Object result = null;
//		if (GlobalSecurity.isEnabled() && this.secured
//			&& ((loginPath != null
//			&& !loginPath.equals(request.pathInfo()))
//			&& user == null)) {
//			String expectedPage = request.pathInfo();
//			session.attribute(
//				HomeAppConstants.EXPECTED_PAGE_ATTR,
//				expectedPage);
//			response.redirect(loginPath);
//		} else {
//			request.attribute(HomeAppConstants.SESSION_USER,
//				user);
		result = doHandle(request, response);
//			session.invalidate();
//		}
		return result;
	}

	public abstract Object doHandle(Request request, Response response);

}
