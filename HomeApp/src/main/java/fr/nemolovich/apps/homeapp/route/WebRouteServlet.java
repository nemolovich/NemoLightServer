/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.route.filter.proxy.ServletHandler;
import fr.nemolovich.apps.homeapp.route.filter.proxy.ServletProxy;
import fr.nemolovich.apps.homeapp.route.freemarker.FreemarkerRoute;
import fr.nemolovich.apps.homeapp.route.freemarker.FreemarkerWebRoute;
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
		FreemarkerRoute get = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
				Writer writer) throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();
				setUser(root, request.session());
				doGet(request, response, root);
				template.process(root, writer);
			}
		};
		FreemarkerWebRoute route = (FreemarkerWebRoute) get;
		try {
			ServletHandler handler = new ServletHandler(get);
			Object proxy = ServletProxy.newProxyInstance(
				FreemarkerRoute.class.getClassLoader(),
				FreemarkerRoute.class.getInterfaces(),
				handler);
			Object o = FreemarkerRoute.class.cast(proxy);
			System.out.println(o.toString());
		} catch (ClassCastException ex) {
			System.err.println("Can not instantiate get proxy: " + ex.getMessage());
		}
		this.getRoute = route;
		FreemarkerRoute post = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
				Writer writer) throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();
				setUser(root, request.session());
				doPost(request, response, root);
				template.process(root, writer);
			}
		};
		this.postRoute = (FreemarkerWebRoute) post;
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

	public final WebRoute getGetRoute() {
		return this.getRoute;
	}

	public final WebRoute getPostRoute() {
		return (WebRoute) this.postRoute;
	}

	public void enableSecurity() {
		this.getRoute.enableSecurity();
		this.postRoute.enableSecurity();
	}

	@Override
	public String toString() {
		return "WebRouteServlet: " + this.template.getName();
	}

}
