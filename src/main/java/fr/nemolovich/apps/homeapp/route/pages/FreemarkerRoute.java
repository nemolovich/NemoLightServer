/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.pages;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author Nemolovich
 */
public abstract class FreemarkerRoute extends Route {

    final Template template;

    /**
     * Constructor
     *
     * @param path The route path which is used for matching. (e.g. /hello,
     * users/:name)
     * @param templateName
     * @param cfg
     * @throws java.io.IOException
     */
    protected FreemarkerRoute(final String path, final String templateName,
        Configuration cfg) throws IOException {
        super(path);
        template = cfg.getTemplate(templateName);
    }

    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            doHandle(request, response, writer);
        } catch (TemplateException | IOException e) {
            // FIXME: MAKE LOGGER WITH FILE
            StringBuilder details = new StringBuilder(e.getMessage()
                .concat("<br/>\n"));
            StackTraceElement[] elements = e.getStackTrace();
            for (StackTraceElement element : elements) {
                details.append("&nbsp;".concat(element.toString())
                    .concat("<br/>\n"));
            }

            request.session().attribute("error_details", details.toString());
            response.redirect("error/500");
        }
        return writer;
    }

    protected abstract void doHandle(final Request request, final Response response, final Writer writer)
        throws IOException, TemplateException;

}
