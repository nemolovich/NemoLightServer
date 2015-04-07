package fr.nemolovich.apps.nemolight.provided.ajax.functions;

import fr.nemolovich.apps.nemolight.provided.ajax.IAjaxFunction;
import org.json.JSONObject;

/**
 *
 * @author Nemolovich
 */
public class GetBeans implements IAjaxFunction {

	private static final String FUNC_NAME = "getBeans";

	@Override
	public JSONObject call(JSONObject param) {
		System.out.println("param: " + param);
		return param;
	}

	@Override
	public String getFuncName() {
		return FUNC_NAME;
	}

}
