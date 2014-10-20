/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.ajax;

import java.io.IOException;
import java.io.Writer;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.route.WebRouteServlet;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/ajax", page = "ajax.tpl")
public class AjaxConnection extends WebRouteServlet {

	private static final String ACTION_KEY = "action";
	private static final String VALUE_KEY = "value";

	private static final String ACTION_SUCCESS = "confirm";

	public AjaxConnection(String routePath, String page, Configuration config)
			throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doGet(Request request, Response response, Writer writer)
			throws TemplateException, IOException {

		Object value = request.attribute("value");
		System.out.println(value);
	}

	@Override
	protected void doPost(Request request, Response response, Writer writer)
			throws TemplateException, IOException {

		SimpleHash root = new SimpleHash();

		String value = request.raw().getParameter("value");

		try {
			Object parse = JSON.parse(value);
		} catch (JSONParseException e) {

		}

		root.put(ACTION_KEY, ACTION_SUCCESS);
		root.put(VALUE_KEY, value);

		template.process(root, writer);
	}

}
