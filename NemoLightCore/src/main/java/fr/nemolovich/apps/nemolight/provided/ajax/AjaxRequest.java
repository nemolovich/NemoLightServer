/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.provided.ajax;

import java.io.IOException;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.nemolight.route.WebRouteServlet;
import fr.nemolovich.apps.nemolight.route.annotations.RouteElement;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/ajax/:uid", page = "ajax.tpl")
public class AjaxRequest extends WebRouteServlet {

	private static final String BEAN_KEY = "action";
	private static final String VALUE_KEY = "value";

	public AjaxRequest(String routePath, String page, Configuration config)
			throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doGet(Request request, Response response, SimpleHash root)
			throws TemplateException, IOException {
	}

	@Override
	protected void doPost(Request request, Response response, SimpleHash root)
			throws TemplateException, IOException {

		String passedUid = request.params("uid");
		String value = request.raw().getParameter("value");
		String uid = request.raw().getParameter("uid");
		String bean = request.raw().getParameter("bean");

		if (value != null || bean != null || uid != null || passedUid != null) {
			throw new IllegalArgumentException("Passed arguments are incorrect");
		}

		if (uid.equals(passedUid)) {
			
		}
	}
}
