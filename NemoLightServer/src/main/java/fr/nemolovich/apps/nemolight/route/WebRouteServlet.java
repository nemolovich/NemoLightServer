package fr.nemolovich.apps.nemolight.route;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.freemarker.FreemarkerWebRoute;
import fr.nemolovich.apps.nemolight.session.RequestParameters;
import fr.nemolovich.apps.nemolight.session.Session;
import fr.nemolovich.apps.nemolight.session.SessionUtils;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public abstract class WebRouteServlet {

	private static final Logger LOGGER
		= Logger.getLogger(WebRouteServlet.class);

	private final FreemarkerWebRoute getRoute;
	private final FreemarkerWebRoute postRoute;

	private final ConcurrentMap<Field, String> fieldsList
		= new ConcurrentHashMap<>();

	private boolean processTemplate = false;

	protected final Template template;

	public WebRouteServlet(String path, String templateName,
		Configuration config) throws IOException {
		this.getRoute = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
				Writer writer) throws IOException, TemplateException {
				SimpleHash root = initRoute(request);
				doGet(request, response, root);
				if (processTemplate) {
					template.process(root, writer);
				}
			}
		};
		this.postRoute = new FreemarkerWebRoute(path, config) {

			@Override
			protected void doHandle(Request request, Response response,
				Writer writer) throws IOException, TemplateException {
				SimpleHash root = initRoute(request);
				doPost(request, response, root);
				if (processTemplate) {
					template.process(root, writer);
				}
			}
		};
		this.template = config.getTemplate(templateName);
	}

	private SimpleHash initRoute(Request request) {
		this.processTemplate = true;
		SimpleHash root = new SimpleHash();
		Session userSession = SessionUtils.getSession(
			request.session());
		root.put(NemoLightConstants.SESSION_ATTR,
			userSession);
		root.put(NemoLightConstants.REQUEST_ATTR,
			new RequestParameters(request));
		root.put(NemoLightConstants.AJAX_BEAN_KEY,
			this.getName());
		JSONObject fields = new JSONObject();
		fields.put(NemoLightConstants.AJAX_FIELDS_KEY,
			new JSONArray(fieldsList.values()));
		root.put(NemoLightConstants.AJAX_FIELDS_KEY,
			fields.toString());
		for (String field : fieldsList.values()) {
			root.put(field, "");
		}
		return root;
	}

	public void addPageField(Field field, String pageFieldName) {
		fieldsList.put(field, pageFieldName);
	}

	protected abstract void doGet(Request request, Response response,
		SimpleHash root) throws TemplateException, IOException;

	protected abstract void doPost(Request request, Response response,
		SimpleHash root) throws TemplateException, IOException;

	public final WebRoute getGetRoute() {
		return this.getRoute;
	}

	public final WebRoute getPostRoute() {
		return (WebRoute) this.postRoute;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public void getAjaxRequest(JSONObject request,
		SimpleHash root) {
		for (Entry<Field, String> entry : fieldsList.entrySet()) {
			String value = request.getString(entry.getValue());
			setFieldValue(entry.getKey(), value);
		}
		root.put(NemoLightConstants.AJAX_ACTION_KEY,
			NemoLightConstants.AJAX_ACTION_UPDATE);
		root.put(NemoLightConstants.AJAX_VALUE_KEY, request);
	}

	public void stopProcess() {
		this.processTemplate = false;
	}

	public void enableSecurity() {
		this.getRoute.enableSecurity();
		this.postRoute.enableSecurity();
	}

	private void setFieldValue(Field field, String value) {
		try {
			field.setAccessible(true);
			field.set(this, value);
			field.setAccessible(false);
		} catch (IllegalArgumentException |
			IllegalAccessException ex) {
			LOGGER.warn(String.format(
				"Can not set field '%s' value",
				field.getName()), ex);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.template.getName();
	}

}
