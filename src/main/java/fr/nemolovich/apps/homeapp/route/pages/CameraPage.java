/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.pages;

import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.route.WebRouteServlet;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
@RouteElement(path = "/camera/:size", page = "camera.html")
public class CameraPage extends WebRouteServlet {

    private String cameraServer;
    private int cameraPort;
    private String cameraProtocol;

    private static final Pattern DIMENSION
        = Pattern.compile("^(?<width>\\d{2,})x(?<height>\\d{2,})$");

    public CameraPage(String path, String templateName, Configuration config)
        throws IOException {
        super(path, templateName, config);
        this.cameraServer="localhost";
        this.cameraPort=5000;
        this.cameraProtocol="rtsp";
    }

    @Override
    protected void doGet(Request request, Response response, Writer writer)
        throws TemplateException, IOException {
        SimpleHash root = new SimpleHash();
        String size = request.params("size");

        Matcher matcher = DIMENSION.matcher(size);

        int cameraWidth,cameraHeight;
        if (matcher.matches()) {
            cameraWidth = Integer.parseInt(matcher.group("width"));
            cameraHeight = Integer.parseInt(matcher.group("height"));
        } else {
            cameraWidth = 420;
            cameraHeight = 280;
        }
        root.put("pi_camera_width", cameraWidth);
        root.put("pi_camera_height", cameraHeight);
        root.put("pi_camera_protocol", this.cameraProtocol);
        root.put("pi_camera_server", this.cameraServer);
        root.put("pi_camera_port", String.valueOf(this.cameraPort));
        template.process(root, writer);
    }

    @Override
    protected void doPost(Request request, Response response, Writer writer)
        throws TemplateException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
