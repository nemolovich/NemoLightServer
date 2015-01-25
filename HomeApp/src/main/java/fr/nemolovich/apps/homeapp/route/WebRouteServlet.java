/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.route.pages.FreemarkerWebRoute;
import fr.nemolovich.apps.homeapp.security.User;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import spark.Request;
import spark.Response;
import spark.Session;

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
		User user = session.attribute(HomeAppConstants.USER_ATTR);
		if (user != null) {
			root.put(HomeAppConstants.SESSION_USER, user);
		}
	}

	protected abstract void doGet(Request request, Response response,
		SimpleHash root) throws TemplateException, IOException;

	protected abstract void doPost(Request request, Response response,
		SimpleHash root) throws TemplateException, IOException;

	public final FreemarkerWebRoute getGetRoute() {
		return this.getRoute;
	}

	public final FreemarkerWebRoute getPostRoute() {
		return this.postRoute;
	}

	public void enableSecurity() {
		this.getRoute.enableSecurity();
		this.postRoute.enableSecurity();
	}

}
