package fr.nemolovich.apps.nemolight.route;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.exceptions.ServerException;
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
abstract class WebRouteServlet implements
    IWebRouteServlet {

    private static final Logger LOGGER = Logger
        .getLogger(WebRouteServlet.class);

    private final FreemarkerWebRoute getRoute;
    private final FreemarkerWebRoute postRoute;

    private final ConcurrentMap<Field, String> fieldsList = new ConcurrentHashMap<>();

    private boolean processTemplate = false;

    protected final Template template;
    private final String context;

    public WebRouteServlet(String path, String context, String templateName,
        Configuration config) throws IOException {
        this.context = context;
        this.getRoute = new FreemarkerWebRoute(path, context, config) {

            @Override
            protected void doHandle(Request request, Response response,
                Writer writer) throws ServerException {
                SimpleHash root = initRoute(request);
                doGet(request, response, root);
                if (processTemplate) {
                    try {
                        template.process(root, writer);
                    } catch (TemplateException | IOException ex) {
                        throw new ServerException("Template process exception", ex);
                    }
                }
            }
        };
        this.postRoute = new FreemarkerWebRoute(path, context, config) {

            @Override
            protected void doHandle(Request request, Response response,
                Writer writer) throws ServerException {
                SimpleHash root = initRoute(request);
                doPost(request, response, root);
                if (processTemplate) {
                    try {
                        template.process(root, writer);
                    } catch (TemplateException | IOException ex) {
                        throw new ServerException("Template process exception", ex);
                    }
                }
            }
        };
        this.template = config.getTemplate(templateName);
    }

    private SimpleHash initRoute(Request request) {
        this.processTemplate = true;
        SimpleHash root = new SimpleHash();
        Session userSession = SessionUtils.getSession(request.session());
        root.put(NemoLightConstants.SESSION_ATTR, userSession);
        root.put(NemoLightConstants.REQUEST_ATTR,
            new RequestParameters(request));
        root.put(NemoLightConstants.AJAX_BEAN_KEY, this.getName());
        JSONObject fields = new JSONObject();
        fields.put(NemoLightConstants.AJAX_FIELDS_KEY,
            new JSONArray(fieldsList.values()));
        root.put(NemoLightConstants.AJAX_FIELDS_KEY, fields.toString());
        root.put(NemoLightConstants.APPICATION_CONTEXT, "/".concat(this.context));
        for (String field : fieldsList.values()) {
            root.put(field, "");
        }
        return root;
    }

    @Override
    public final void addPageField(Field field, String pageFieldName) {
        fieldsList.put(field, pageFieldName);
    }

    protected abstract void doGet(Request request, Response response,
        SimpleHash root) throws ServerException;

    protected abstract void doPost(Request request, Response response,
        SimpleHash root) throws ServerException;

    @Override
    public final WebRoute getGetRoute() {
        return this.getRoute;
    }

    @Override
    public final WebRoute getPostRoute() {
        return (WebRoute) this.postRoute;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void getAjaxRequest(JSONObject request, SimpleHash root) {
        for (Entry<Field, String> entry : fieldsList.entrySet()) {
            String value = request.getString(entry.getValue());
            setFieldValue(entry.getKey(), value);
        }
        root.put(NemoLightConstants.AJAX_ACTION_KEY,
            NemoLightConstants.AJAX_ACTION_UPDATE);
        root.put(NemoLightConstants.AJAX_VALUE_KEY, request);
    }

    @Override
    public final void redirect(Request request, Response response) {

        spark.Session session = request.session(true);
        Session userSession = SessionUtils.getSession(session);

        String page = userSession.getExpectedPage();
        userSession.setExpectedPage(null);
        this.stopProcess();
        response.redirect(page);

    }

    @Override
    public final void stopProcess() {
        this.processTemplate = false;
    }

    @Override
    public final void enableSecurity() {
        this.getRoute.enableSecurity();
        this.postRoute.enableSecurity();
    }

    private void setFieldValue(Field field, String value) {
        try {
            field.setAccessible(true);
            field.set(this, value);
            field.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LOGGER.warn(
                String.format("Can not set field '%s' value",
                    field.getName()), ex);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.template.getName();
    }

}
