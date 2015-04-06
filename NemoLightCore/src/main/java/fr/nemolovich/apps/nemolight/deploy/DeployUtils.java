package fr.nemolovich.apps.nemolight.deploy;

import fr.nemolovich.apps.mavendependenciesdownloader.DependenciesDownloader;
import fr.nemolovich.apps.mavendependenciesdownloader.DependenciesException;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.utils.SearchFileOptionException;
import fr.nemolovich.apps.nemolight.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 *
 * @author Nemolovich
 */
final class DeployUtils {

	static void addMavenDependencies(URL url,
		URLClassLoader classLoader) {
		MavenXpp3Reader reader = new MavenXpp3Reader();

		try {
			JarFile jarFile = new JarFile(new File(url.toURI()));

			List<String> files = Utils.getAllFilesFrom(jarFile,
				NemoLightConstants.JAR_MAVEN_FOLDER,
				NemoLightConstants.EXCLUDE_CLASS_FILES
				| NemoLightConstants.EXCLUDE_FOLDERS);

			String pomPath = null;
			for (String file : files) {
				if (file.endsWith("pom.xml")) {
					pomPath = file;
					break;
				}
			}
			if (pomPath == null) {
				throw new IOException("Can not locate pom file in JAR file");
			}

			InputStream is = classLoader.getResourceAsStream(String.format(
				"%s%s", NemoLightConstants.JAR_MAVEN_FOLDER, pomPath));
			Model model = reader.read(new InputStreamReader(is));

			String mavenURL = WebConfig
				.getStringValue(WebConfig.MAVEN_REPOSITORY);

			String outputPath = NemoLightConstants.DEPENDENCIES_FOLDER;

			DependenciesDownloader.downloadDependencies(model, outputPath,
				mavenURL);

		} catch (IOException | DependenciesException | XmlPullParserException | SearchFileOptionException | URISyntaxException ex) {
			System.err.printf("Can not read pom file: %s\n", ex.getMessage());
		}
	}

	static List<File> getAllFiles(File root) {
		return getAllFiles(root, NemoLightConstants.DEFAULT_SEARCH);
	}

	static List<File> getAllFiles(File root, int options) {
		List<File> files = new ArrayList<>();
		for (File f : root.listFiles()) {
			if (f.isFile()) {
				files.add(f);
			} else if (f.isDirectory()
				&& options == NemoLightConstants.RECURSIVE_SEARCH) {
				files.addAll(getAllFiles(f, options));
			}
		}
		return files;
	}

	static void extractFiles(List<String> files,
		String basePath, String protocol) {

		InputStream input = null;
		ClassLoader classLoader;
		File f;
		URL res;
		File target;
		for (String fileName : files) {
			try {
				if (protocol.equalsIgnoreCase(
					NemoLightConstants.FILE_PROTOCOL)) {
					f = new File(String.format("%s%s",
						basePath, fileName));
					input = new FileInputStream(f);
				} else if (protocol.equalsIgnoreCase(NemoLightConstants.JAR_PROTOCOL)) {
					classLoader = (ClassLoader) WebConfig
						.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
					res = classLoader.getResource(String.format("%s%s",
						basePath, fileName));
					if (res != null) {
						input = res.openStream();
					}
				} else {
					DeployResourceManager.LOGGER.error(
						String.format("Unknown protocol '%s'",
							protocol));
					return;
				}
				if (input == null) {
					throw new IOException("Can not read input file");
				}
				target = new File(String.format("%s%s",
					NemoLightConstants.RESOURCES_FOLDER, fileName));
				if (!target.exists()) {
					if (!target.getParentFile().mkdirs()
						&& !target.createNewFile()) {
						throw new IOException("Can not create target file");
					}
				}
				Files.copy(input, target.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
				DeployResourceManager.LOGGER.info(
					String.format("Resource '%s' extracted.",
						fileName));
			} catch (IOException ex) {
				DeployResourceManager.LOGGER.error(
					String.format("Can not extract resources: '%s'",
						fileName), ex);
			}
		}
	}
}
