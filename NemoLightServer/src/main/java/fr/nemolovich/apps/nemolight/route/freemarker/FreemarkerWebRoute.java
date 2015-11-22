package fr.nemolovich.apps.nemolight.route.freemarker;

import fr.nemolovich.apps.nemolight.route.WebRoute;
import fr.nemolovich.apps.nemolight.route.exceptions.ServerException;
import freemarker.template.Configuration;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public abstract class FreemarkerWebRoute extends WebRoute
    implements FreemarkerRoute {

    private static final Logger LOGGER = Logger
        .getLogger(FreemarkerWebRoute.class);

    protected FreemarkerWebRoute(final String path, String context,
        Configuration cfg) throws IOException {
        super(path, context);
    }

    @Override
    public Object doHandle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            doHandle(request, response, writer);
        } catch (ServerException ex) {
            LOGGER.log(Level.ERROR, "Error while processing with route", ex);
            StringBuilder details = new StringBuilder(String.format("%s<br/>\n",
                ex.getMessage()));
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                details.append(String.format("&nbsp;%s<br/>\n",
                    element.toString()));
            }

            request.session().attribute("error_details", details.toString());
            response.redirect(String.format("%s/error/500",
                this.getContext() == null ? "" : "/".concat(this.getContext())));
        }
        return writer;
    }

    protected abstract void doHandle(final Request request,
        final Response response, Writer writer) throws ServerException;

    @Override
    public String toString() {
        return "FreemarkerWebRoute: " + this.getPath();
    }

}
