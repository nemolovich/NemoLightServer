/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route;

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
public class HomePage extends FreemarkerRoute {

    public HomePage(Configuration config) throws IOException {
        super("/", "login.ftl", config);
    }

    @Override
    protected void doHandle(Request request, Response response, Writer writer)
            throws IOException, TemplateException {
        
        SimpleHash root = new SimpleHash();
//        root.put("username", "");
        root.put("login_error", "");
        template.process(root, writer);
    }

}
