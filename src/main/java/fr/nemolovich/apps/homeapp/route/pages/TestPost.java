package fr.nemolovich.apps.homeapp.route.pages;

import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.route.WebRouteServlet;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import spark.Request;
import spark.Response;

@RouteElement(path = "/post", page = "post.html")
public class TestPost extends WebRouteServlet {

    public TestPost(String routePath, String page, Configuration config)
        throws IOException {
        super(routePath, page, config);
    }

    @Override
    protected void doGet(Request request, Response response, Writer writer)
        throws TemplateException, IOException {

        String name = request.queryParams("name");
        String password = request.queryParams("password");

        SimpleHash root = new SimpleHash();
        root.put("name", name == null ? "" : name);
        root.put("password", password == null ? "" : password);
        template.process(root, writer);
    }

    @Override
    protected void doPost(Request request, Response response, Writer writer)
        throws TemplateException, IOException {
        String name = request.queryParams("name");
        String password = request.queryParams("password");

        SimpleHash root = new SimpleHash();
        root.put("name", name == null ? "" : name);
        root.put("password", password == null ? "" : password);
        template.process(root, writer);
    }

    @Override
    protected void doPut(Request request, Response response, Writer writer) 
        throws TemplateException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
