package fr.nemolovich.apps.nemolight.route;

import java.io.IOException;
import java.io.Writer;

import spark.Request;
import spark.Response;
import spark.Session;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.freemarker.FreemarkerWebRoute;
import fr.nemolovich.apps.nemolight.security.User;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * @author Nemolovich
 */
public abstract class WebRouteServlet {

	private final FreemarkerWebRoute getRoute;
	private final FreemarkerWebRoute postRoute;

	protected final Template template;

	public WebRouteServlet(String path, String templateName,
			Configuration config) throws IOException {
		this.getRoute = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
					Writer writer) throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();
				setUser(root, request.session());
				doGet(request, response, root);
				template.process(root, writer);
			}
		};
		this.postRoute = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
					Writer writer) throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();
				setUser(root, request.session());
				doPost(request, response, root);
				template.process(root, writer);
			}
		};
		this.template = config.getTemplate(templateName);
	}

	protected void setUser(SimpleHash root, Session session) {
		User user = session.attribute(NemoLightConstants.USER_ATTR);
		if (user != null) {
			root.put(NemoLightConstants.SESSION_USER, user);
		}
	}

	protected abstract void doGet(Request request, Response response,
			SimpleHash root) throws TemplateException, IOException;

	protected abstract void doPost(Request request, Response response,
			SimpleHash root) throws TemplateException, IOException;

	public final WebRoute getGetRoute() {
		return this.getRoute;
	}

	public final WebRoute getPostRoute() {
		return (WebRoute) this.postRoute;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public void getAjaxRequest(String request, SimpleHash root) {
	}

	public void enableSecurity() {
		this.getRoute.enableSecurity();
		this.postRoute.enableSecurity();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.template.getName();
	}

}
