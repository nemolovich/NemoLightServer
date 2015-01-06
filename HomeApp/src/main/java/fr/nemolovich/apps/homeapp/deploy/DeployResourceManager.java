/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.deploy;

import fr.nemolovich.apps.homeapp.admin.AdminConnection;
import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.reflection.AnnotationTypeFilter;
import fr.nemolovich.apps.homeapp.reflection.ClassPathScanner;
import fr.nemolovich.apps.homeapp.reflection.SuperClassFilter;
import fr.nemolovich.apps.homeapp.route.WebRoute;
import fr.nemolovich.apps.homeapp.route.WebRouteServlet;
import fr.nemolovich.apps.homeapp.route.file.FileRoute;
import fr.nemolovich.apps.homeapp.utils.SearchFileOptionException;
import fr.nemolovich.apps.homeapp.utils.Utils;
import freemarker.template.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import org.apache.log4j.Logger;
import spark.Spark;

/**
 *
 * @author Nemolovich
 */
public final class DeployResourceManager {

    private static final Logger LOGGER = Logger
        .getLogger(DeployResourceManager.class.getName());

    private static final List<WebRouteServlet> SERVLETS = new ArrayList<>();
    private static final List<FileRoute> FILES = new ArrayList<>();

    public static void initResources(String resourcesPath) {
        URL url = DeployResourceManager.class.getClassLoader().getResource(
            resourcesPath);
        if (url == null) {
            url = DeployResourceManager.class.getClassLoader().getResource(
                String.format("%s/%s", HomeAppConstants.SRC_FOLDER,
                    resourcesPath));
        }
        List<String> files = null;
        if (url != null) {
            String basePath = null;
            String protocol = url.getProtocol();
            try {
                if (protocol.equalsIgnoreCase(HomeAppConstants.FILE_PROTOCOL)) {
                    File path = new File(url.toURI());
                    files = Utils.getAllFilesFrom("", path,
                        HomeAppConstants.EXCLUDE_CLASS_FILES
                        | HomeAppConstants.EXCLUDE_FOLDERS);
                    basePath = url.toURI().getPath().substring(1);

                } else if (protocol
                    .equalsIgnoreCase(HomeAppConstants.JAR_PROTOCOL)) {
                    JarFile jar = Utils.extractJar(url);
                    files = Utils.getAllFilesFrom(jar, resourcesPath,
                        HomeAppConstants.EXCLUDE_CLASS_FILES
                        | HomeAppConstants.EXCLUDE_FOLDERS);

                    basePath = resourcesPath;
                } else {
                    LOGGER.error(String.format("Unknown protocol '%s'",
                        protocol));
                }
            } catch (SearchFileOptionException | URISyntaxException ex) {
                LOGGER.error("Error while searching files", ex);
            }
            if (files != null) {
                extractFiles(files, basePath, protocol);
            }
        }
    }

    private static void extractFiles(List<String> files, String basePath,
        String protocol) {

        InputStream input = null;
        for (String fileName : files) {
            try {
                if (protocol.equalsIgnoreCase(HomeAppConstants.FILE_PROTOCOL)) {
                    File f = new File(String.format("%s%s", basePath, fileName));
                    input = new FileInputStream(f);
                } else if (protocol
                    .equalsIgnoreCase(HomeAppConstants.JAR_PROTOCOL)) {
                    URL res = DeployResourceManager.class.getClassLoader()
                        .getResource(String.format("%s%s", basePath, fileName));
                    if (res != null) {
                        input = res.openStream();
                    }
                } else {
                    LOGGER.error(String.format("Unknown protocol '%s'",
                        protocol));
                    return;
                }
                if (input == null) {
                    throw new IOException("Can not read input file");
                }
                File target = new File(String.format("%s%s",
                    HomeAppConstants.RESOURCES_FOLDER, fileName));
                if (!target.exists()) {
                    if (!target.getParentFile().mkdirs()
                        && !target.createNewFile()) {
                        throw new IOException("Can not create target file");
                    }
                }
                Files.copy(input, target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info(String.format("Resource '%s' extracted.",
                    fileName));
            } catch (IOException ex) {
                LOGGER.error(String.format("Can not extract resources: '%s'",
                    fileName), ex);
            }
        }
    }

    public static final boolean deployWebPages(Configuration config) {
        ClassPathScanner scanner = new ClassPathScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(RouteElement.class));
        scanner.addIncludeFilter(new SuperClassFilter(WebRouteServlet.class));

        WebRouteServlet servlet;
        boolean loginPageDefined = false;

        for (Class<?> c : scanner
            .findCandidateComponents("fr.nemolovich.apps.homeapp")) {
            try {
                RouteElement annotation = c.getAnnotation(RouteElement.class);
                String path = annotation.path();
                String page = annotation.page();
                boolean isLoginPage = annotation.login();
                Constructor<?> cst = c.getConstructor(String.class,
                    String.class, Configuration.class);
                Object o = cst.newInstance(path, page, config);
                if (o instanceof WebRouteServlet) {
                    servlet = (WebRouteServlet) o;
                    SERVLETS.add(servlet);
                    LOGGER.info(String.format(
                        "Resource '%s' has been deployed! [%s]",
                        c.getName(), path));
                    if (!loginPageDefined && isLoginPage) {
                        loginPageDefined = true;
                        WebRoute.setLoginPage(servlet.getPostRoute());
                        LOGGER.info(String.format(
                            "Resource '%s' has been set to login page",
                            c.getName()));
                    } else if (isLoginPage) {
                        LOGGER.warn("Login page is already set");
                    }
                }

            } catch (InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException |
                NoSuchMethodException | SecurityException ex) {
                LOGGER.error("Error while deploying resources", ex);
            }
        }
        return false;
    }

    public static void deloyWebApp(String deployFolderPath) {
        File deployFolder = new File(deployFolderPath);
        try {
            if (!deployFolder.exists() || !deployFolder.isDirectory()) {
                throw new FileNotFoundException(String.format(
                    "The deploy folder '%s can not be located",
                    deployFolderPath));
            }
            FileRoute route;
            for (File f : getAllFile(deployFolder,
                HomeAppConstants.RECURSIVE_SEARCH)) {
                String uriPath = f.toURI().toString();
                String routePath = uriPath.substring(uriPath
                    .lastIndexOf(deployFolderPath)
                    + (deployFolderPath.length()));
                route = new FileRoute(routePath, f);
                route.disableSecurity();
                FILES.add(route);
                LOGGER.info(String.format(
                    "Resource '%s' has been deployed! [%s]", f.getName(),
                    routePath));
            }
        } catch (FileNotFoundException ex) {
            LOGGER.error("Error while deploying webapp", ex);
        }
    }

    private static List<File> getAllFile(File root) {
        return getAllFile(root, HomeAppConstants.DEFAULT_SEARCH);
    }

    private static List<File> getAllFile(File root, int options) {
        List<File> files = new ArrayList();
        for (File f : root.listFiles()) {
            if (f.isFile()) {
                files.add(f);
            } else if (f.isDirectory()
                && options == HomeAppConstants.RECURSIVE_SEARCH) {
                files.addAll(getAllFile(f, options));
            }
        }
        return files;
    }

    public static void startServer() {
        startServer(8080);
    }

    public static void startServer(int port) {
        startServer(port, 8181);
    }

    public static void startServer(int port, int adminPort) {

        if (adminPort != port) {
            AdminConnection ac = new AdminConnection(adminPort);
            ac.start();
        } else {
            LOGGER.error("The admin port can be the same as deployment port");
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("Server stopped... I'll miss you ;)");
            }
        });
        Spark.setPort(port);

        for (WebRouteServlet servlet : DeployResourceManager.SERVLETS) {
            Spark.get(servlet.getGetRoute());
            Spark.post(servlet.getPostRoute());
        }
        for (FileRoute route : FILES) {
            Spark.get(route);
        }
    }

}
