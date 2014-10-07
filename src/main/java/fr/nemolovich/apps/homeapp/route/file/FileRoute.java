package fr.nemolovich.apps.homeapp.route.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;

public class FileRoute extends Route {

	private InputStream file;
	private static final Logger LOGGER = Logger.getLogger(FileRoute.class);

	public FileRoute(String route, String filePath) {
		super(route);
		this.file = FileRoute.class.getResourceAsStream(filePath);
		if (this.file == null) {
			LOGGER.log(
					Level.ERROR,
					"Can not load stream for file '".concat(filePath).concat(
							"'"));
		}
	}

	@Override
	public Object handle(Request request, Response response) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.file));

		StringWriter writer = new StringWriter();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				writer.write(String.format("%s%n", line));
				writer.flush();
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "Error while reading file", e);
		}

		return writer;
	}
}
