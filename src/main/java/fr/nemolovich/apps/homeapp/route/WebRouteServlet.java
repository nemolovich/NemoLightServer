/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route;

import fr.nemolovich.apps.homeapp.route.pages.FreemarkerWebRoute;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public abstract class WebRouteServlet {

    private final FreemarkerWebRoute getRoute;
    private final FreemarkerWebRoute postRoute;
    private final FreemarkerWebRoute putRoute;

    protected final Template template;

    public WebRouteServlet(String path, String templateName, Configuration config)
        throws IOException {
        this.getRoute = new FreemarkerWebRoute(path, config) {

            @Override
            protected void doHandle(Request request, Response response,
                Writer writer) throws IOException, TemplateException {
                doGet(request, response, writer);
            }
        };
        this.postRoute = new FreemarkerWebRoute(path, config) {

            @Override
            protected void doHandle(Request request, Response response,
                Writer writer) throws IOException, TemplateException {
                doPost(request, response, writer);
            }
        };
        this.putRoute = new FreemarkerWebRoute(path, config) {

            @Override
            protected void doHandle(Request request, Response response,
                Writer writer) throws IOException, TemplateException {
                doPut(request, response, writer);
            }
        };
        this.template = config.getTemplate(templateName);
    }

    protected abstract void doGet(Request request, Response response,
        Writer writer) throws TemplateException, IOException;

    protected abstract void doPost(Request request, Response response,
        Writer writer) throws TemplateException, IOException;

    protected abstract void doPut(Request request, Response response,
        Writer writer) throws TemplateException, IOException;

    public final FreemarkerWebRoute getGetRoute() {
        return this.getRoute;
    }

    public final FreemarkerWebRoute getPostRoute() {
        return this.postRoute;
    }

    public final FreemarkerWebRoute getPutRoute() {
        return this.putRoute;
    }

}
