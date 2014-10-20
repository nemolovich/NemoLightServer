package fr.nemolovich.apps.homeapp.route.file;

import fr.nemolovich.apps.homeapp.route.WebRoute;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;

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
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(this.file));
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.ERROR, "Can not find file", ex);
            return null;
        }

        int width = 200, height = 200;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D ig2 = bi.createGraphics();
        ig2.fillRect(0, 0, width - 1, height - 1);
        ImageWriter w = (ImageWriter) ImageIO.getImageWritersByFormatName(
            "PNG").next();
        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(this.file);
            w.setOutput(ios);
            w.write(bi);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileRoute.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Object o=w.getOutput();
        
        StringWriter writer = new StringWriter();

        String line;
        try {
            while ((line = ((FileImageOutputStream)o).readLine()) != null) {
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
