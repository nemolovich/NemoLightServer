package fr.nemolovich.apps.nemolight.provided.ajax;

import fr.nemolovich.apps.nemolight.provided.ajax.functions.GetBeans;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONObject;

/**
 *
 * @author Nemolovich
 */
public class AjaxFunctionsManager {

	private static final ConcurrentLinkedQueue<IAjaxFunction> AVAILABLE_FUNCTIONS;

	static {
		AVAILABLE_FUNCTIONS = new ConcurrentLinkedQueue<>();
		AVAILABLE_FUNCTIONS.add(new GetBeans());
	}

	public static void addFunction(IAjaxFunction function) {
		AVAILABLE_FUNCTIONS.add(function);
	}

	public static List<IAjaxFunction> getAvailableFunctions() {
		return new ArrayList<>(AVAILABLE_FUNCTIONS);
	}

	public static boolean functionExists(String funcName) {
		boolean result = false;
		for (IAjaxFunction fun : AVAILABLE_FUNCTIONS) {
			if (fun.getFuncName().equals(funcName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	static JSONObject call(String method, JSONObject param) {
		JSONObject result = null;
		for (IAjaxFunction fun : AVAILABLE_FUNCTIONS) {
			if (fun.getFuncName().equals(method)) {
				result = fun.call(param);
				break;
			}
		}
		return result;
	}

}
