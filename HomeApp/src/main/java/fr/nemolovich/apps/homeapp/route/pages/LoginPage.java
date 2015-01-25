/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.pages;

import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.route.WebRouteServlet;
import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.SecurityUtils;
import fr.nemolovich.apps.homeapp.security.User;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import java.io.IOException;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/login", page = "login.html", login = true)
public class LoginPage extends WebRouteServlet {

	public LoginPage(String routePath, String page, Configuration config)
		throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doGet(Request request, Response response, SimpleHash root)
		throws TemplateException, IOException {
		root.put("username", "");
		root.put("login_error", "");
	}

	@Override
	protected void doPost(Request request, Response response, SimpleHash root)
		throws TemplateException, IOException {

		String name = request.queryParams("name");
		String password = request.queryParams("password");

		root.put("username", name);

		String expectedPass = null;
		for (User u : GlobalSecurity.getUsers()) {
			if (u.getName().equals(name)) {
				expectedPass = u.getPassword();
				break;
			}
		}
		if (expectedPass != null) {

			String encrypted = SecurityUtils.getEncryptedPassword(password);

			if (!expectedPass.equals(encrypted)) {
				root.put("login_error",
					"Login error. Please check your login/password.");
			} else {
				User user = SecurityConfiguration.getInstance().getUser(name);
				Session session = request.session(true);
				session.attribute(HomeAppConstants.USER_ATTR,
					user);
				String expectedPage = request.session().attribute(
					HomeAppConstants.EXPECTED_PAGE_ATTR);
				if (expectedPage != null) {
					session.removeAttribute(
						HomeAppConstants.EXPECTED_PAGE_ATTR);
					response.redirect(expectedPage);
				}
			}
		}
	}

}
