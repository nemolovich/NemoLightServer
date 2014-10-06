/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp;

import fr.nemolovich.apps.homeapp.route.ErrorPage;
import fr.nemolovich.apps.homeapp.route.FreemarkerRoute;
import fr.nemolovich.apps.homeapp.route.HomePage;
import freemarker.template.Configuration;
import java.io.IOException;
import spark.Spark;

/**
 *
 * @author Nemolovich
 */
public class Launcher {

    private static final Configuration config = new Configuration();

    private static final String TEMPLATE_FOLDER
            = "/fr/nemolovich/apps/homeapp/freemarker/";

    public static void main(String[] args) throws IOException {
        config.setClassForTemplateLoading(FreemarkerRoute.class, TEMPLATE_FOLDER);
        Spark.setPort(8081);
        Spark.get(new HomePage(config));
        Spark.get(new ErrorPage(config));
    }
}
