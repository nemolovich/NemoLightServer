package fr.nemolovich.apps.nemolight.provided.ajax;

import org.json.JSONObject;

/**
 *
 * @author Nemolovich
 */
public interface IAjaxFunction {
	
	public JSONObject call(JSONObject param);
	
	public String getFuncName();
}
