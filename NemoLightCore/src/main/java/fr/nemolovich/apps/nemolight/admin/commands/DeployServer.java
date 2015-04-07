package fr.nemolovich.apps.nemolight.admin.commands;

import fr.nemolovich.apps.nemolight.Launcher;
import fr.nemolovich.apps.nemolight.admin.Command;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.deploy.DeployResourceManager;
import java.util.List;
import java.util.Map;
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

		List<Map<String, Object>> apps = DeployResourceManager
			.initializeClassLoader();

		String packageName;
		int identifier;
		for (Map<String, Object> app : apps) {
			packageName = (String) app.get(
				NemoLightConstants.APP_PACKAGE);
			identifier = (int) app.get(
				NemoLightConstants.APP_IDENTIFIER);
			if (packageName == null) {
				LOGGER.warn(String.format(
					"There is no package for application '[%02d] %s'",
					identifier,
					app.get(NemoLightConstants.APP_NAME)));
			} else {
				DeployResourceManager.deployWebPages(
					Launcher.CONFIG,
					packageName.replaceAll("/", "."),
					identifier);
			}
		}

		DeployResourceManager.deployWebApp(WebConfig
			.getStringValue(WebConfig.DELPOYMENT_FOLDER));

		DeployResourceManager.startListening();

		LOGGER.info("Resources deployed!");
		return "Resources has been deployed!";
	}
}
