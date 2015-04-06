package fr.nemolovich.apps.nemolight.provided.ajax;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import fr.nemolovich.apps.nemolight.route.WebRouteServletAdapter;
import fr.nemolovich.apps.nemolight.route.WebRouteServletInterface;
import fr.nemolovich.apps.nemolight.route.annotations.RouteElement;
import fr.nemolovich.apps.nemolight.route.exceptions.ServerException;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/ajax/function/:method/:uid", page = "ajax_functions.tpl")
public class AjaxFunction extends WebRouteServletAdapter {

	public AjaxFunction(String routePath, String page, Configuration config)
		throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doPost(Request request, Response response, SimpleHash root)
		throws ServerException {

		String passedUid = request.params("uid");
		String passedMethod = request.params("method");
		String value = request.raw().getParameter("value");
		String uid = request.raw().getParameter("uid");
		String bean = request.raw().getParameter("bean");

		if (value == null || bean == null || uid == null
			|| passedUid == null) {
			JSONObject result = new JSONObject();

			result.put(NemoLightConstants.AJAX_ERROR_KEY,
				NemoLightConstants.AJAX_ERROR_INVALID_REQUEST);
			result.put(NemoLightConstants.AJAX_ERROR_DESC,
				"Request parameters are incorrect");

			root.put(NemoLightConstants.AJAX_ACTION_KEY, bean);
			root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
		} else if (uid.equals(passedUid)) {
			WebRouteServletInterface beanRoute = null;
			for (String beanName : DeployResourceManager.getBeans()) {
				if (beanName.equalsIgnoreCase(bean)) {
					beanRoute = DeployResourceManager.getBean(beanName);
					break;
				}
			}
			if (beanRoute == null) {
				JSONObject result = new JSONObject();

				result.put(NemoLightConstants.AJAX_ERROR_KEY,
					NemoLightConstants.AJAX_ERROR_UNKNOWN_BEAN);
				result.put(NemoLightConstants.AJAX_ERROR_DESC,
					String.format("Can not find bean named '%s'", bean));

				root.put(NemoLightConstants.AJAX_ACTION_KEY,
					NemoLightConstants.AJAX_ERROR_KEY);
				root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
			} else {
				JSONObject valueObject;
				try {
					valueObject = new JSONObject(value);
					beanRoute.getAjaxRequest(valueObject, root);
				} catch (JSONException je) {
					valueObject = new JSONObject();

					valueObject.put(NemoLightConstants.AJAX_ERROR_KEY,
						NemoLightConstants.AJAX_ERROR_INVALID_SYNTAX);
					valueObject.put(NemoLightConstants.AJAX_ERROR_DESC, String
						.format("Value '%s' is not a valid JSON object: %s",
							value, je.getMessage()));

					root.put(NemoLightConstants.AJAX_ACTION_KEY,
						NemoLightConstants.AJAX_ERROR_KEY);
					root.put(NemoLightConstants.AJAX_VALUE_KEY, valueObject);
				}
			}

		} else {
			JSONObject result = new JSONObject();

			result.put(NemoLightConstants.AJAX_ERROR_KEY,
				NemoLightConstants.AJAX_ERROR_INVALID_REQUEST);
			result.put(NemoLightConstants.AJAX_ERROR_DESC,
				"Given request UUID is not the same as parameter");

			root.put(NemoLightConstants.AJAX_ACTION_KEY,
				NemoLightConstants.AJAX_ERROR_KEY);
			root.put(NemoLightConstants.AJAX_VALUE_KEY, result);
		}
	}
}
