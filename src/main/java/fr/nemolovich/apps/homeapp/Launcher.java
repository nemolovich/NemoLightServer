/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import fr.nemolovich.apps.homapp.config.route.DeploymentConfig;
import fr.nemolovich.apps.homeapp.deploy.DeployResourceManager;
import freemarker.template.Configuration;

/**
 *
 * @author Nemolovich
 */
public class Launcher {

    private static final Configuration config = new Configuration();

    private static final String CONFIG_FOLDER
            = DeployResourceManager.RESOURCES_FOLDER.concat("/configs/");

    private static final String PACKAGE_NAME = "fr/nemolovich/apps/homeapp/";

    private static final String TEMPLATE_FOLDER
            = DeployResourceManager.RESOURCES_FOLDER.concat(
                    "/freemarker/");

    private static final Logger LOGGER = Logger.getLogger(Launcher.class);

    private static final List PARAMS = Arrays.asList(
            getArray("init", "Initialize resources"),
            getArray("start", "Start server"),
            getArray("deploy", "Deploy resources"));

    private static final String HELP;

    static {
        StringBuilder help = new StringBuilder(
                "Usage: Launcher <PARAM> [<OPTIONAL_PARAMS>,]\n");
        for (Object o : PARAMS) {
            String[] param = (String[]) o;
            help.append(String.format("\t--%-15s%-30s%n", param[0], param[1]));
        }
        HELP = help.toString();
    }

    private static String[] getArray(String str1, String str2) {
        String[] a = new String[2];
        a[0] = str1;
        a[1] = str2;
        return a;
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println(HELP);
            return;
        }
        
        boolean templateInit=false;
        File templateFolder=new File(TEMPLATE_FOLDER);
        
        if(templateFolder.exists()) {
        	config.setDirectoryForTemplateLoading(templateFolder);
        	templateInit=true;
        }

        for (String arg : args) {
            System.out.println("arg: " + arg);
            if (arg.equals("--" + ((String[]) PARAMS.get(0))[0])) {

                System.out.println("Init");

                DeployResourceManager.initResources(PACKAGE_NAME);

            } else if (arg.equals("--" + ((String[]) PARAMS.get(1))[0])) {

                System.out.println("Start");

                PropertyConfigurator.configure(CONFIG_FOLDER
                        .concat("log4j/log4j.properties"));
                LOGGER.debug("Log4j appender configuration successfully loaded");

                Spark.setPort(8081);
                Spark.get(new Route("") {
                    @Override
                    public Object handle(Request rqst, Response rspns) {
                        return "Server started!";
                    }
                });

            } else if (arg.equals("--" + ((String[]) PARAMS.get(2))[0])) {

                System.out.println("Deploy");
                
                DeploymentConfig.getInstance().initialize(config);

//                Spark.get(new FileRoute("/webapp/js/jquery-min.js",
//                        DeployResourceManager.RESOURCES_FOLDER.concat("webapp/js/")
//                        .concat(WebConfig.getStringValue("jquery.file"))));
//                Spark.get(new HomePage(config));
//                Spark.get(new ErrorPage(config));

            }
            
            if(!templateInit&&templateFolder.exists()) {
            	config.setDirectoryForTemplateLoading(templateFolder);
            }	

        }

    }
}
