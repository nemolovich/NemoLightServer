package fr.nemolovich.apps.nemolight.route;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.file.FileRoute;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import fr.nemolovich.apps.nemolight.security.SecurityUtils;
import fr.nemolovich.apps.nemolight.security.User;
import fr.nemolovich.apps.nemolight.session.Session;
import fr.nemolovich.apps.nemolight.session.SessionUtils;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class WebRoute extends Route {

    private static WebRoute LOGIN_PAGE;
    private static WebRoute HOME_PAGE;
    private final String context;
    private final String route;
    private boolean secured;

    public WebRoute(String path, String context) {
        super(String.format("/%s%s", context, path));
        this.route = path;
        this.context = context;
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

    public final String getContext() {
        return this.context;
    }

    @Override
    public final Object handle(Request request, Response response) {
        spark.Session session = request.session(true);
        Session userSession = SessionUtils.getSession(session);
        User user = userSession.getUser();

        String loginPath = LOGIN_PAGE == null ? null : String.format("/%s%s",
            LOGIN_PAGE.getContext(), LOGIN_PAGE.getPath());
        String expectedPage = request.pathInfo();
        if (!FileRoute.class.isAssignableFrom(this.getClass())) {
            String uid = request.cookie(NemoLightConstants.SESSION_COOKIE);
            // TODO: LOGGER
            if (uid != null) {
                user = SecurityConfiguration.getInstance().getUserByUID(uid);
                userSession.setUser(user);
            }
        }

        Object result = null;
        if (securityIsNeeded(loginPath, expectedPage, user)) {
            userSession.setExpectedPage(expectedPage);
            // TODO: LOGGER
            response.redirect(loginPath);
        } else {
            if (user != null) {
                response.cookie("/", NemoLightConstants.SESSION_COOKIE,
                    user.getUID(), NemoLightConstants.COOKIE_TIME,
                    false);
            }
            result = doHandle(request, response);
        }
        return result;
    }

    private boolean securityIsNeeded(String loginPath, String expectedPath,
        User user) {
        return SecurityUtils.isSecured()
            && this.secured
            && ((loginPath != null && !loginPath.equals(expectedPath)) && user == null);
    }

    public abstract Object doHandle(Request request, Response response);

}
