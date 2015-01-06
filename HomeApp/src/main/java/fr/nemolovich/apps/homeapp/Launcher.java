/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp;

import fr.nemolovich.apps.homeapp.config.WebConfig;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.deploy.DeployResourceManager;
import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
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
        boolean securityDisabled = false;
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
                } else if (arg.equals("--disable-security")) {
                    securityDisabled = true;
                } else if (needAdminPort) {
                    try {
                        adminport = Integer.parseInt(arg);
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE,
                            "Incorrect admin port parameter", e);
                    }
                    needAdminPort = false;
                } else {
                    LOGGER.log(Level.SEVERE, String.format(
                        "Unknown parameter '%s'", arg));
                }
            }
        }

        File templateFolder = new File(HomeAppConstants.TEMPLATE_FOLDER);

        if (extract) {

            LOGGER.info("Extracting files from archive...");

            DeployResourceManager
                .initResources(HomeAppConstants.PACKAGE_NAME);

            LOGGER.info("Extraction completed!");

        }

        LOGGER.info("Configuring log file...");

        PropertyConfigurator.configure(String.format("%s%s",
            HomeAppConstants.CONFIG_FOLDER,
            HomeAppConstants.LOGGER_FILE_PATH));
        org.apache.log4j.Logger log
            = org.apache.log4j.Logger.getLogger(Launcher.class);

        log.info("Log4j appender configuration successfully loaded");

        if (!securityDisabled) {
            try {
                log.info("Enabling security...");
                SecurityConfiguration.loadConfig(GlobalSecurity.loadConfig());
                try {
                    GlobalSecurity.loadPasswords();
                } catch (IOException | ClassNotFoundException ex) {
                    log.error("Can not load passwords", ex);
                }
                GlobalSecurity.enableSecurity();
                log.info("Security enabled");
            } catch (JAXBException ex) {
                log.error("Can not load security configuration", ex);
            }
        }

        log.info(String.format(
            "Log file settings set from '%s%s'",
            HomeAppConstants.CONFIG_FOLDER,
            HomeAppConstants.LOGGER_FILE_PATH));

        log.info(String.format(
            "Setting freemarker templates folder to '%s'",
            templateFolder.getAbsolutePath()));

        config.setDirectoryForTemplateLoading(templateFolder);

        log.info("Deploying resources...");

        DeployResourceManager.deployWebPages(config);

        DeployResourceManager.deloyWebApp(WebConfig.getStringValue("deploy.folder"));

        log.info("Resources deployed!");

        log.info(String.format("Starting server on port [%s]...",
            String.valueOf(port)));
        DeployResourceManager.startServer(port, adminport);

        log.info(String.format("Server started on port [%s]!",
            String.valueOf(port)));

    }

}
