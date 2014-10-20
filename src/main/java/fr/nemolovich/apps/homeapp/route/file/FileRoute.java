package fr.nemolovich.apps.homeapp.route.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.homeapp.route.WebRoute;

public class FileRoute extends WebRoute {

	private final File file;
	private static final Logger LOGGER = Logger.getLogger(FileRoute.class);

	public FileRoute(String route, File file) {
		super(route);
		this.file = file;
		if (this.file == null || !this.file.exists()) {
			LOGGER.log(Level.ERROR, "Can not load file '"
					.concat(file.getPath()).concat("'"));
		}
	}

	@Override
	public Object handle(Request request, Response response) {
		try {
			if (this.file.getName().toLowerCase().endsWith(".png")) {
				return this.deployPNGFile(request, response);
			} else {
				return this.deployTextFile(request, response);
			}
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "Error while reading file", e);
		}
		return null;

	}

	private final Object deployTextFile(Request request, Response response)
			throws IOException {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.file));
		} catch (FileNotFoundException ex) {
			LOGGER.log(Level.ERROR, "Can not find file", ex);
			return null;
		}

		StringWriter writer = new StringWriter();

		String line;
		while ((line = reader.readLine()) != null) {
			writer.write(String.format("%s%n", line));
			writer.flush();
		}
		writer.close();
		reader.close();

		return writer;
	}

	private final Object deployPNGFile(Request request, Response response)
			throws IOException {

		HttpServletResponse resp = response.raw();

		resp.setContentType("image/png");

		FileInputStream in = new FileInputStream(this.file);

		OutputStream out = resp.getOutputStream();

		byte[] buf = new byte[2048];
		int count = 0;
		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		return null;
	}
}
