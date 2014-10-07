package fr.nemolovich.apps.homeapp.route.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class FileRoute extends Route {

    private final File file;
    private static final Logger LOGGER = Logger.getLogger(FileRoute.class);

    public FileRoute(String route, String filePath) {
        super(route);
        this.file = new File(filePath);
        if (this.file == null || !this.file.exists()) {
            LOGGER.log(Level.ERROR, "Can not load file '"
                    .concat(filePath).concat("'"));
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    this.file));
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.ERROR, "Can not find file", ex);
            return null;
        }

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
