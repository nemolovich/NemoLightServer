package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.Launcher;
import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import java.util.List;
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

		List<String> packagesName
			= DeployResourceManager.initializeClassLoader();
		
		for (String packageName : packagesName) {
			DeployResourceManager.deployWebPages(
				Launcher.CONFIG,
				packageName.replaceAll("/", "."));
		}

		DeployResourceManager.deployWebApp(WebConfig
			.getStringValue(WebConfig.DELPOYMENT_FOLDER));
		
		DeployResourceManager.startListening();

		LOGGER.info("Resources deployed!");
		return "Resources has been deployed!";
	}
}
