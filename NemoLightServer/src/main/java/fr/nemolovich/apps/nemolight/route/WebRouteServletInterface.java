package fr.nemolovich.apps.nemolight.route;

import freemarker.template.SimpleHash;
import java.lang.reflect.Field;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface WebRouteServletInterface {

	void addPageField(Field field, String pageFieldName);

	void enableSecurity();

	void getAjaxRequest(JSONObject request, SimpleHash root);

	WebRoute getGetRoute();

	String getName();

	WebRoute getPostRoute();

	void redirect(Request request, Response response);

	void stopProcess();

	@Override
	String toString();
	
}
