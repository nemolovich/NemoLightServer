package fr.nemolovich.apps.nemolight.provided.ajax;

import java.io.IOException;

import org.json.JSONObject;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
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

		if (value == null || bean == null || uid == null || passedUid == null) {
			JSONObject result = new JSONObject();

			result.put("error", "INVALID_REQUEST");
			result.put("desc", "Request parameters are incorrect");

			root.put(NemoLightConstants.AJAX_BEAN_KEY, bean);
			root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
		}

		if (uid.equals(passedUid)) {
			WebRouteServlet beanRoute = null;
			for (String beanName : DeployResourceManager.getBeans()) {
				if (beanName.equalsIgnoreCase(bean)) {
					beanRoute = DeployResourceManager.getBean(beanName);
					break;
				}
			}
			if (beanRoute == null) {
				JSONObject result = new JSONObject();

				result.put("error", "UNKNOWN_BEAN");
				result.put("desc",
						String.format("Can not find bean named '%s'", bean));

				root.put(NemoLightConstants.AJAX_BEAN_KEY, bean);
				root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
			} else {
				beanRoute.getAjaxRequest(value, root);
			}

		} else {
			JSONObject result = new JSONObject();

			result.put("error", "INVALID_REQUEST");
			result.put("desc",
					"Given request UUID is not the same as parameter");

			root.put(NemoLightConstants.AJAX_BEAN_KEY, bean);
			root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
		}
	}
}