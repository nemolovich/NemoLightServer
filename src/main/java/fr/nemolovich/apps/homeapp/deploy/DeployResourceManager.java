/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nemolovich
 */
public final class DeployResourceManager {

    private static final Logger LOGGER = Logger.getLogger(
            DeployResourceManager.class.getName());
    private static final String SRC_FOLDER = "src/main/resources";
    public static final String RESOURCES_FOLDER = "resources/";
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";

    public static void initResources(String resourcesPath) {
        URL url = DeployResourceManager.class.getClassLoader()
                .getResource(resourcesPath);
        if (url == null) {
            url = DeployResourceManager.class.getClassLoader()
                    .getResource(SRC_FOLDER.concat("/").concat(resourcesPath));
        }
        List<String> files = null;
        if (url != null) {
            String basePath = null;
            String protocol = url.getProtocol();
            if (protocol.equalsIgnoreCase(FILE_PROTOCOL)) {
                try {
                    File path = new File(url.toURI());
                    files = getAllFilesFrom("", path);
                    basePath = url.toURI().getPath().substring(1);
                } catch (URISyntaxException ex) {
                    LOGGER.log(Level.SEVERE, "Can not load path as folder", ex);
                }
            } else if (protocol.equalsIgnoreCase(JAR_PROTOCOL)) {
                String jarPath = "jar:file:/C:/Users/Nemolovich/Desktop/Tests/"
                        + "HomeApp-0.1-jar-with-dependencies.jar!/fr/"
                        + "nemolovich/apps/homeapp/";
                jarPath = jarPath.substring(JAR_PROTOCOL.length() + 1, jarPath
                        .indexOf("!"));
                if (jarPath.startsWith(FILE_PROTOCOL)) {
                    jarPath = jarPath.substring(FILE_PROTOCOL.length() + 1);
                }
                jarPath = jarPath.substring(1);
                JarFile jar;
                try {
                    jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                    files = getAllFilesFrom(jar, resourcesPath);
                    basePath = resourcesPath;
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Can not load path from jar file",
                            ex);
                }
            } else {
                LOGGER.log(Level.SEVERE, "Unknown protocol '".concat(protocol)
                        .concat("'"));
            }
            if (files != null) {
                deployFiles(files, basePath, protocol);
            }
        }
    }

    private static List<String> getAllFilesFrom(String basePath, File folder) {
        List<String> files = new ArrayList<>();
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                files.addAll(getAllFilesFrom(basePath.concat(f.getName())
                        .concat("/"), f));
            } else {
                if (!f.getName().endsWith(".class")) {
                    files.add(basePath.concat(f.getName()));
                }
            }
        }
        return files;
    }

    private static List<String> getAllFilesFrom(JarFile jar, String resourcesPath) {
        List<String> files = new ArrayList<>();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(resourcesPath)) {
                String entry = name.substring(
                        resourcesPath.length());
                if (!entry.isEmpty() && !entry.endsWith("/")
                        && !entry.endsWith(".class")) {
                    files.add(entry);
                }
            }
        }
        return files;
    }

    private static void deployFiles(List<String> files, String basePath,
            String protocol) {

        InputStream input = null;
        for (String fileName : files) {
            try {
                if (protocol.equalsIgnoreCase(FILE_PROTOCOL)) {
                    File f = new File(
                            basePath.concat(fileName));
                    input = new FileInputStream(f);
                } else if (protocol.equalsIgnoreCase(JAR_PROTOCOL)) {
                    URL res = DeployResourceManager.class.getClassLoader()
                            .getResource(basePath.concat(fileName));
                    if (res != null) {
                        input = res.openStream();
                    }
                } else {
                    LOGGER.log(Level.SEVERE, "Unknown protocol '".concat(protocol)
                            .concat("'"));
                    return;
                }
                if (input == null) {
                    throw new IOException(
                            "Can not read input file");
                }
                File target = new File(RESOURCES_FOLDER.concat(
                        fileName));
                if (!target.exists()) {
                    if (!target.getParentFile().mkdirs()
                            && !target.createNewFile()) {
                        throw new IOException(
                                "Can not create target file");
                    }
                }
                Files.copy(input, target.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                LOGGER.log(Level.INFO, "Resources: '"
                        .concat(fileName).concat("' deployed"));
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Can not deploy resources: '"
                        .concat(fileName).concat("'"), ex);
            }
        }
    }

}
