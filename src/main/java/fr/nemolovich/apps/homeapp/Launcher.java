/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp;

import fr.nemolovich.apps.homeapp.config.WebConfig;
import fr.nemolovich.apps.homeapp.route.file.FileRoute;
import freemarker.template.Configuration;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pages.ErrorPage;
import pages.FreemarkerRoute;
import pages.HomePage;
import spark.Spark;

/**
 *
 * @author Nemolovich
 */
public class Launcher {

	private static final Configuration config = new Configuration();

	private static final String CONFIG_FOLDER = "configs/";

	private static final String PACKAGE_NAME = "/fr/nemolovich/apps/homeapp/";

	private static final String TEMPLATE_FOLDER = PACKAGE_NAME
			.concat("freemarker/");

	private static final Logger LOGGER = Logger.getLogger(Launcher.class);

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure(CONFIG_FOLDER
				.concat("log4j/log4j.properties"));

		LOGGER.debug("Log4j appender configuration successfully loaded");
		config.setClassForTemplateLoading(FreemarkerRoute.class,
				TEMPLATE_FOLDER);
		Spark.setPort(8081);

		Spark.get(new FileRoute("/webapp/js/jquery-min.js", PACKAGE_NAME
				.concat("/webapp/js/").concat(
						WebConfig.getStringValue("jquery.file"))));
		Spark.get(new HomePage(config));
		Spark.get(new ErrorPage(config));
	}
}
