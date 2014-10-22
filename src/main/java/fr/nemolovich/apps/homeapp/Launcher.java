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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Nemolovich
 */
public class Launcher {

    private static final Configuration config = new Configuration();

    private static final Logger LOGGER = Logger.getLogger(
        Launcher.class.getName());

    public static void main(String[] args) throws IOException {

        boolean extract = false;
        int adminport = 8081;
        int port = 8080;
        if (args.length > 0) {
            boolean needPort = false;
            boolean needAdminPort = false;
            for (String arg : args) {
                if (arg.startsWith("--port")) {
                    needPort = true;
                } else if (needPort) {
                    try {
                        port = Integer.parseInt(arg);
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Incorrect port parameter", e);
                    }
                    needPort = false;
                } else if (arg.equals("--extract")) {
                    extract = true;
                } else if (arg.equals("--admin-port")) {
                    needAdminPort = true;
                } else if (needAdminPort) {
                    try {
                        adminport = Integer.parseInt(arg);
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE,
                            "Incorrect admin port parameter", e);
                    }
                    needAdminPort = false;
                } else {
                    LOGGER.log(Level.SEVERE,
                        "Unknown parameter '".concat(arg).concat("'"));
                }
            }
        }

        File templateFolder = new File(HomeAppConstants.TEMPLATE_FOLDER);

        if (extract) {

            LOGGER.log(Level.INFO, "Extracting files from archive...");

            DeployResourceManager
                .initResources(HomeAppConstants.PACKAGE_NAME);

            LOGGER.log(Level.INFO, "Extraction completed!");

        }

        LOGGER.log(Level.INFO,
            "Configuring log file...");

        PropertyConfigurator.configure(HomeAppConstants.CONFIG_FOLDER.concat(HomeAppConstants.LOGGER_FILE_PATH));
        org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Launcher.class);

        log.info(
            "Log4j appender configuration successfully loaded");

        log.info(
            "Log file settings set from '".concat(
                HomeAppConstants.CONFIG_FOLDER.concat(
                    HomeAppConstants.LOGGER_FILE_PATH)).concat("'"));

        log.info(
            "Setting freemarker templates folder to '"
            .concat(templateFolder.getAbsolutePath()).concat("'"));

        config.setDirectoryForTemplateLoading(templateFolder);

        log.info(
            "Deploying resources...");

        DeployResourceManager.deployWebPages(config);

        DeployResourceManager.deloyWebApp(WebConfig.getStringValue("deploy.folder"));

        log.info(
            "Resources deployed!");

        log.info(
            "Starting server on port [".concat(String.valueOf(port))
            .concat("]..."));
        DeployResourceManager.startServer(port, adminport);

        log.info(
            "Server started on port [".concat(String.valueOf(port))
            .concat("]..."));

    }

}
