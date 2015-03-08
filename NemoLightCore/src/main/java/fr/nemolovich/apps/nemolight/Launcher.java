/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight;

import fr.nemolovich.apps.nemolight.admin.AdminConnection;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import fr.nemolovich.apps.nemolight.security.GlobalSecurity;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

	private static final Logger LOGGER = Logger.getLogger(Launcher.class
		.getName());

	public static void main(String[] args) throws IOException {

		Thread.currentThread().setName("Server");
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
					LOGGER.log(Level.SEVERE,
						String.format("Unknown parameter '%s'", arg));
				}
			}
		}

		if (!securityDisabled) {
			AdminConnection.setSystemProperties();
		}

		File templateFolder = new File(
			NemoLightConstants.TEMPLATE_FOLDER);

		List<String> packagesName
			= DeployResourceManager.initializeClassLoader();

		if (extract) {

			LOGGER.info("Extracting files from archive...");

			DeployResourceManager.initResources(
				NemoLightConstants.PACKAGE_NAME);
			for (String packageName : packagesName) {
				DeployResourceManager.initResources(
					packageName);
			}

			LOGGER.info("Extraction completed!");

		}

		LOGGER.info("Configuring log file...");

		PropertyConfigurator.configure(String.format("%s%s",
			NemoLightConstants.CONFIG_FOLDER,
			NemoLightConstants.LOGGER_FILE_PATH));
		org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Launcher.class);

		log.info("Log4j appender configuration successfully loaded");

		if (!securityDisabled) {
			try {
				log.info("Enabling security...");
				SecurityConfiguration.loadConfig(
					GlobalSecurity.loadConfig());
				try {
					GlobalSecurity.loadPasswords();
					GlobalSecurity.enableSecurity();
					log.info("Security enabled");
				} catch (IOException | ClassNotFoundException ex) {
					log.error("Can not load passwords", ex);
				}
			} catch (JAXBException ex) {
				log.error("Can not load security configuration", ex);
			}
		}

		log.info(String.format("Log file settings set from '%s%s'",
			NemoLightConstants.CONFIG_FOLDER,
			NemoLightConstants.LOGGER_FILE_PATH));

		log.info(String.format("Setting freemarker templates folder to '%s'",
			templateFolder.getAbsolutePath()));

		config.setDirectoryForTemplateLoading(templateFolder);

		log.info("Deploying resources...");

		for (String packageName : packagesName) {
			DeployResourceManager.deployWebPages(config,
				packageName.replaceAll("/", "."));
		}

		DeployResourceManager.deployWebApp(WebConfig
			.getStringValue(WebConfig.DELPOYMENT_FOLDER));

		log.info("Resources deployed!");

		log.info(String.format("Starting server on port [%s]...",
			String.valueOf(port)));
		DeployResourceManager.startServer(port, adminport);

		log.info(String.format("Server started on port [%s]!",
			String.valueOf(port)));

	}
}
