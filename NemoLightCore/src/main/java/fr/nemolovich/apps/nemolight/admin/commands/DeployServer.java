package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.Launcher;
import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class DeployServer extends Command {

	private static final Logger LOGGER
		= Logger.getLogger(ShutdownServer.class);

	public DeployServer() {
		super("deploy_server", "Deploy the server again (means recompile pages from local files)");
	}

	@Override
	public String getHelp() {
		return String.format("\t%s%n", this.getDescription());
	}

	@Override
	public String doCommand(String... args) {
		LOGGER.info("Deploying resources...");

		Map<String, String> packagesName
			= DeployResourceManager.initializeClassLoader();

		for (Entry<String, String> packageEntry : packagesName.entrySet()) {
			DeployResourceManager.deployWebPages(
				Launcher.CONFIG,
				packageEntry.getValue().replaceAll("/", "."),
				packageEntry.getKey());

			DeployResourceManager.deployWebApp(WebConfig
				.getStringValue(WebConfig.DELPOYMENT_FOLDER),
				packageEntry.getKey());
		}

		DeployResourceManager.startListening();

		LOGGER.info("Resources deployed!");
		return "Resources has been deployed!";
	}
}
