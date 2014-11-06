/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.pages;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.homeapp.route.WebRoute;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 *
 * @author Nemolovich
 */
public abstract class FreemarkerWebRoute extends WebRoute {

    private static final Logger LOGGER = Logger
        .getLogger(FreemarkerWebRoute.class);

    protected FreemarkerWebRoute(final String path,
        Configuration cfg) throws IOException {
        super(path);
    }

    @Override
    public Object doHandle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            doHandle(request, response, writer);
        } catch (TemplateException | IOException ex) {
            LOGGER.log(Level.ERROR, "Error while processing with route", ex);
            StringBuilder details = new StringBuilder(ex.getMessage().concat(
                "<br/>\n"));
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                details.append("&nbsp;".concat(element.toString()).concat(
                    "<br/>\n"));
            }

            request.session().attribute("error_details", details.toString());
            response.redirect("error/500");
        }
        return writer;
    }

    protected abstract void doHandle(final Request request,
        final Response response, Writer writer) throws IOException,
        TemplateException;

}
