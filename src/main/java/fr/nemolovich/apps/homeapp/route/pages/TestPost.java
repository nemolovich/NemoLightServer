package fr.nemolovich.apps.homeapp.route.pages;

import java.io.IOException;
import java.io.Writer;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.constants.RouteElementMethod;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

@RouteElement(path = "/post", page = "post.html", method = RouteElementMethod.POST)
public class TestPost extends FreemarkerRoute {

	public TestPost(String routePath, String page, Configuration config)
			throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doHandle(Request request, Response response, Writer writer)
			throws IOException, TemplateException {
		String name = request.queryParams("name");
		String password = request.queryParams("password");

		SimpleHash root = new SimpleHash();
		root.put("name", name == null ? "" : null);
		root.put("password", password == null ? "" : null);
		template.process(root, writer);
	}

}
