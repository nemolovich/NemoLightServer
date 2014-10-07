/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route;

import fr.nemolovich.apps.homeapp.config.WebConfig;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public class ErrorPage extends FreemarkerRoute {

    public ErrorPage(Configuration config) throws IOException {
        super(WebConfig.getStringValue("page.error").concat("/:code"),
                WebConfig.getStringValue("page.error.template"), config);
    }

    @Override
    protected void doHandle(Request request, Response response, Writer writer)
            throws IOException, TemplateException {
        SimpleHash root = new SimpleHash();
        String error = request.params("code");
        root.put("code", error);
        String details=request.session().attribute("error_details");
        if(details!=null) {
            root.put("error_details", details);
            request.session().removeAttribute("error_details");
        } else {
            root.put("error_details", "");
        }
        root.put("error", "System has encountered an error.");
        template.process(root, writer);
    }

}
