package fr.nemolovich.apps.nemolight.utils;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.log4j.Logger;

public final class Utils {

    private static final Logger LOGGER = Logger
        .getLogger(Utils.class.getName());

    public static JarFile extractJar(URL jarURL) {
        String jarPath;

        try {
            jarPath = jarURL.toURI().toString();
        } catch (URISyntaxException ex) {
            LOGGER.error("The JAR URI can not be used", ex);
            return null;
        }
        jarPath = jarPath.substring(NemoLightConstants.JAR_PROTOCOL.length() + 1,
            jarPath.indexOf("!"));
        if (jarPath.startsWith(NemoLightConstants.FILE_PROTOCOL)) {
            jarPath = jarPath
                .substring(NemoLightConstants.FILE_PROTOCOL.length() + 1);
        }
        if (!jarPath.startsWith("/")) {
            jarPath = jarPath.substring(1);
        }

        JarFile jar = null;

        try {
            jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        } catch (IOException ex) {
            LOGGER.error("Can not load path from JAR file", ex);
        }

        return jar;
    }

    public static List<String> getAllFilesFrom(String basePath, File folder)
        throws SearchFileOptionException {
        return getAllFilesFrom(basePath, folder, NemoLightConstants.INCLUDE_ALL);
    }

    public static List<String> getAllFilesFrom(String basePath, File folder,
        int options) throws SearchFileOptionException {
        /*
         * Check options
         */
        boolean all = false;
        boolean onlyClasses = false;
        boolean excludeClasses = false;
        boolean onlyFodlers = false;
        boolean excludeFolders = false;
        if (options == NemoLightConstants.INCLUDE_ALL) {
            all = true;
        } else {
            if (((options & NemoLightConstants.ONLY_CLASS_FILES))
                == NemoLightConstants.ONLY_CLASS_FILES) {
                onlyClasses = true;
            }
            if (((options & NemoLightConstants.EXCLUDE_CLASS_FILES))
                == NemoLightConstants.EXCLUDE_CLASS_FILES) {
                excludeClasses = true;
            }
            if (((options & NemoLightConstants.ONLY_FOLDERS))
                == NemoLightConstants.ONLY_FOLDERS) {
                onlyFodlers = true;
            }
            if (((options & NemoLightConstants.EXCLUDE_FOLDERS))
                == NemoLightConstants.EXCLUDE_FOLDERS) {
                excludeFolders = true;
            }

            /*
             * Check errors
             */
            if (onlyClasses && excludeClasses) {
                throw new SearchFileOptionException(
                    "Can not include and exclude classes");
            }
            if (onlyFodlers && excludeFolders) {
                throw new SearchFileOptionException(
                    "Can not include and exclude folders");
            }
            if (onlyClasses && onlyFodlers) {
                throw new SearchFileOptionException(
                    "Can not get only folders and only classes");
            }
        }
        List<String> files = new ArrayList<>();
        for (File f : folder.listFiles()) {

            if (f.isDirectory()) {
                files.addAll(getAllFilesFrom(String.format("%s%s/", basePath,
                    f.getName()), f, options));
            } else {
                String fileName = f.getName();
                if (!fileName.isEmpty() && !fileName.contains("$")) {
                    boolean get = false;
                    boolean isClass = fileName.endsWith(".class");
                    boolean isFolder = fileName.endsWith("/");
                    if (all || (onlyClasses && isClass)
                        || (onlyFodlers && isFolder)) {
                        get = true;
                    } else if (!onlyClasses && !onlyFodlers) {
                        get = true;
                        get ^= !(excludeClasses & isClass);
                        get ^= !(excludeFolders & isFolder);
                    }
                    if (get) {
                        files.add(String.format("%s%s", basePath, f.getName()));
                    }
                }
            }
        }
        return files;
    }

    public static List<String> getAllFilesFrom(JarFile jar, String resourcesPath)
        throws SearchFileOptionException {
        return getAllFilesFrom(jar, resourcesPath,
            NemoLightConstants.INCLUDE_ALL);
    }

    public static List<String> getAllFilesFrom(JarFile jar,
        String resourcesPath, int options) throws SearchFileOptionException {

        /*
         * Check options
         */
        boolean all = false;
        boolean onlyClasses = false;
        boolean excludeClasses = false;
        boolean onlyFolders = false;
        boolean excludeFolders = false;
        if (options == NemoLightConstants.INCLUDE_ALL) {
            all = true;
        } else {
            if (((options & NemoLightConstants.ONLY_CLASS_FILES))
                == NemoLightConstants.ONLY_CLASS_FILES) {
                onlyClasses = true;
            }
            if (((options & NemoLightConstants.EXCLUDE_CLASS_FILES))
                == NemoLightConstants.EXCLUDE_CLASS_FILES) {
                excludeClasses = true;
            }
            if (((options & NemoLightConstants.ONLY_FOLDERS))
                == NemoLightConstants.ONLY_FOLDERS) {
                onlyFolders = true;
            }
            if (((options & NemoLightConstants.EXCLUDE_FOLDERS))
                == NemoLightConstants.EXCLUDE_FOLDERS) {
                excludeFolders = true;
            }

            /*
             * Check errors
             */
            if (onlyClasses && excludeClasses) {
                throw new SearchFileOptionException(
                    "Can not include and exclude classes");
            }
            if (onlyFolders && excludeFolders) {
                throw new SearchFileOptionException(
                    "Can not include and exclude folders");
            }
            if (onlyClasses && onlyFolders) {
                throw new SearchFileOptionException(
                    "Can not get only folders and only classes");
            }
        }

        List<String> files = new ArrayList<>();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(resourcesPath)) {
                String entry = name.substring(resourcesPath.length());
                if (!entry.isEmpty() && !entry.contains("$")) {
                    boolean get = false;
                    boolean isClass = entry.endsWith(".class");
                    boolean isFolder = entry.endsWith("/");
                    if (all || (onlyClasses && isClass)
                        || (onlyFolders && isFolder)) {
                        get = true;
                    } else if (!onlyClasses && !onlyFolders) {
                        get = true;
                        get ^= !(excludeClasses & isClass);
                        get ^= !(excludeFolders & isFolder);
                    }
                    if (get) {
                        files.add(entry);
                    }
                }
            }
        }
        return files;
    }

    public static String getFieldName(String fieldName) {
        return fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
    }
}
