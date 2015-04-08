package fr.nemolovich.apps.nemolight.provided.ajax.functions;

import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import fr.nemolovich.apps.nemolight.provided.ajax.IAjaxFunction;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Nemolovich
 */
public class GetBeans implements IAjaxFunction {

	private static final String FUNC_NAME = "getBeans";

	@Override
	public JSONObject call(JSONObject param) {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray(
			DeployResourceManager.getBeans());
		result.put("list", array);
		return result;
	}

	@Override
	public String getFuncName() {
		return FUNC_NAME;
	}

}
