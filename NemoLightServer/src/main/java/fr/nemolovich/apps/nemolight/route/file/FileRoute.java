package fr.nemolovich.apps.nemolight.route.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.route.WebRoute;
import fr.nemolovich.apps.nemolight.route.file.mimetype.ForcedMimeType;
import fr.nemolovich.apps.nemolight.stream.ReplaceOutputStream;

public class FileRoute extends WebRoute {

	private final File file;
	private static final Logger LOGGER = Logger.getLogger(FileRoute.class);
	private static final String TEXT_PLAIN_MIMETYPE = "text/plain";
	private static final List<ForcedMimeType> FORCED_MIMETYPES;

	static {
		FORCED_MIMETYPES = new ArrayList<>();
		FORCED_MIMETYPES.add(ForcedMimeType.newInstance(".js",
				"application/javascript"));
	}

	public FileRoute(String route, String context, File file) {
		super(route, context);
		this.file = file;
		if (this.file == null || !this.file.exists()) {
			LOGGER.log(Level.ERROR,
					String.format("Can not load file '%s'", file.getPath()));
		}
		super.disableSecurity();
	}

	private static boolean isForcedExtensions(String path) {
		boolean result = false;
		for (String ext : getForcedExtensions()) {
			if (path.toLowerCase().endsWith(ext.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	private static List<String> getForcedExtensions() {
		List<String> extensions = new ArrayList<>();
		for (ForcedMimeType mimeType : FORCED_MIMETYPES) {
			extensions.add(mimeType.getExtension());
		}
		return extensions;
	}

	private static String getMimeType(String path) {
		String result = null;
		for (ForcedMimeType mimeType : FORCED_MIMETYPES) {
			if (path.toLowerCase().endsWith(
					mimeType.getExtension().toLowerCase())) {
				result = mimeType.getMimeType();
				break;
			}
		}
		return result;
	}

	@Override
	public Object doHandle(Request request, Response response) {
		try {
			this.deployFile(request, response);
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "Error while reading file", e);
		}
		return null;

	}

	private void deployFile(Request request, Response response)
			throws IOException {

		HttpServletResponse resp = response.raw();

		String fileName = this.file.getName();

		String mimeType;
		if (isForcedExtensions(fileName)) {
			mimeType = getMimeType(fileName);
		} else {
			mimeType = Files.probeContentType(this.file.toPath());
			mimeType = mimeType == null ? TEXT_PLAIN_MIMETYPE : mimeType;
		}

		resp.setContentType(mimeType);

		try (FileInputStream in = new FileInputStream(this.file)) {
			OutputStream out = resp.getOutputStream();

			ReplaceOutputStream replaceOutputStream = new ReplaceOutputStream(
					in, out);
			replaceOutputStream.replace(String.format("\\$\\{%s\\}",
									"/".concat(NemoLightConstants.APPLICATION_CONTEXT)),
							this.getContext());

			out.close();
			in.close();
		}
	}
}
