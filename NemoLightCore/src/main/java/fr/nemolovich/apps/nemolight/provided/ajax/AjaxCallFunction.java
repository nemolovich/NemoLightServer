package fr.nemolovich.apps.nemolight.provided.ajax;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.WebRouteServletAdapter;
import fr.nemolovich.apps.nemolight.route.annotations.RouteElement;
import fr.nemolovich.apps.nemolight.route.exceptions.ServerException;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import java.io.IOException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/ajax/functions/:uid", page = "ajax_function.tpl")
public class AjaxCallFunction extends WebRouteServletAdapter {

	public AjaxCallFunction(String routePath, String page, Configuration config)
		throws IOException {
		super(routePath, page, config);
	}

	@Override
	protected void doPost(Request request, Response response, SimpleHash root)
		throws ServerException {

		String passedUid = request.params("uid");
		String method = request.raw().getParameter("method");
		String param = request.raw().getParameter("param");
		String uid = request.raw().getParameter("uid");

		JSONObject result;
		if (method == null || param == null || uid == null
			|| passedUid == null) {
			result = new JSONObject();

			result.put(NemoLightConstants.AJAX_ERROR_KEY,
				NemoLightConstants.AJAX_ERROR_INVALID_REQUEST);
			result.put(NemoLightConstants.AJAX_ERROR_DESC,
				"Request parameters are incorrect");

			root.put(NemoLightConstants.AJAX_FUNC_NAME_KEY, method);
			root.put(NemoLightConstants.AJAX_RESULT_KEY, result);
		} else if (uid.equals(passedUid)) {
			result = new JSONObject();

			if (AjaxFunctionsManager.functionExists(method)) {
				result = AjaxFunctionsManager.call(method,
					new JSONObject(param));

				root.put(NemoLightConstants.AJAX_FUNC_NAME_KEY,
					method);
				root.put(NemoLightConstants.AJAX_RESULT_KEY,
					result);
			} else {

				result.put(NemoLightConstants.AJAX_ERROR_KEY,
					NemoLightConstants.AJAX_ERROR_UNKNOWN_FUNCTION);
				result.put(NemoLightConstants.AJAX_ERROR_DESC,
					String.format("Can not find function named '%s'", method));

				root.put(NemoLightConstants.AJAX_FUNC_NAME_KEY,
					NemoLightConstants.AJAX_ERROR_KEY);
				root.put(NemoLightConstants.AJAX_RESULT_KEY, result);
			}
		} else {
			result = new JSONObject();

			result.put(NemoLightConstants.AJAX_ERROR_KEY,
				NemoLightConstants.AJAX_ERROR_INVALID_REQUEST);
			result.put(NemoLightConstants.AJAX_ERROR_DESC,
				"Given request UUID is not the same as parameter");

			root.put(NemoLightConstants.AJAX_ACTION_KEY,
				NemoLightConstants.AJAX_ERROR_KEY);
			root.put(NemoLightConstants.AJAX_RESULT_KEY, result);
		}
	}
}
