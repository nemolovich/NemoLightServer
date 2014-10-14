/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp;

import fr.nemolovich.apps.homeapp.config.WebConfig;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.deploy.DeployResourceManager;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Nemolovich
 */
public class Launcher {

    private static final Configuration config = new Configuration();

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

        boolean templateInit = false;
        File templateFolder = new File(HomeAppConstants.TEMPLATE_FOLDER);

        if (templateFolder.exists()) {
            config.setDirectoryForTemplateLoading(templateFolder);
            templateInit = true;
        }

        for (String arg : args) {
            if (arg.equals("--" + ((String[]) PARAMS.get(0))[0])) {

                System.out.println("Init");

                DeployResourceManager
                    .initResources(HomeAppConstants.PACKAGE_NAME);

            } else if (arg.equals("--" + ((String[]) PARAMS.get(1))[0])) {

                System.out.println("Start");

                PropertyConfigurator.configure(HomeAppConstants.CONFIG_FOLDER
                    .concat("log4j/log4j.properties"));
                LOGGER.debug("Log4j appender configuration successfully loaded");

                
                DeployResourceManager.startServer();

            } else if (arg.equals("--" + ((String[]) PARAMS.get(2))[0])) {

                System.out.println("Deploy");

                DeployResourceManager.deployWebPages(config);
                DeployResourceManager.deloyWebApp(WebConfig
                    .getStringValue("deploy.folder"));

            }

            if (!templateInit && templateFolder.exists()) {
                config.setDirectoryForTemplateLoading(templateFolder);
            }

        }

    }
}
