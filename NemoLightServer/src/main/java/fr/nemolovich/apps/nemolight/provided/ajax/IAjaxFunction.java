package fr.nemolovich.apps.nemolight.provided.ajax;

import org.json.JSONObject;

/**
 *
 * @author Nemolovich
 */
public interface IAjaxFunction {

    JSONObject call(JSONObject param);

    String getFuncName();
}
