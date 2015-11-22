package fr.nemolovich.apps.nemolight.constants;

import java.io.File;

public interface NemoLightConstants {

    /* ***********************
     * Deployment constants
     * 
     * ***********************
     */
    String PACKAGE_NAME = "fr/nemolovich/apps/nemolight/";
    String APPLICATION_CONTEXT = "ApplicationContext";

    /* ***********************
     * Web folders
     * 
     * ***********************
     */
    String SRC_FOLDER = "src/main/resources";
    String RESOURCES_FOLDER = "resources/";

    String CONFIG_FOLDER = String.format("%s%s",
        RESOURCES_FOLDER, "configs/");

    String TEMPLATE_FOLDER = String.format("%s%s",
        RESOURCES_FOLDER, "freemarker/");

    String DEPENDENCIES_FOLDER = "dependencies";

    String LOGGER_FILE_PATH = "log4j/log4j.properties";

    String DEPLOY_FOLDER = "deploy";

    /* ***********************
     * Protocols to read file
     * 
     * ***********************
     */
    String FILE_PROTOCOL = "file";
    String JAR_PROTOCOL = "jar";

    /* ***********************
     * JARs constants
     * 
     * ***********************
     */
    String JAR_META_INF = "META-INF";
    String JAR_MAVEN_FOLDER = String.format("%s/maven",
        JAR_META_INF);
    String JAR_DEPLOY_FILE = String.format("%s/deploy.xml",
        JAR_META_INF);

    /* ***********************
     * Folder search options
     * 
     * ***********************
     */
    int DEFAULT_SEARCH = 0;
    int RECURSIVE_SEARCH = 1;

    /* ***********************
     * Resources search options
     * 
     * ***********************
     */
    int INCLUDE_ALL = 2;
    int ONLY_CLASS_FILES = 4;
    int EXCLUDE_CLASS_FILES = 8;
    int ONLY_FOLDERS = 16;
    int EXCLUDE_FOLDERS = 32;

    /* ***********************
     * Security marshaling
     * 
     * ***********************
     */
    String SECURITY_PATH = String.format(RESOURCES_FOLDER,
        "/security/");
    File SECURITY_CONFIG_FILE = new File(String.format(
        "%s%s", SECURITY_PATH, "security.xml"));
    File SECURITY_PASSWORDS_FILE = new File(String.format(
        "%s%s", SECURITY_PATH, "passwords"));

    /* ***********************
     * Session attributes
     * 
     * ***********************
     */
    String SESSION_ATTR = "Session";
    String REQUEST_ATTR = "RequestParameters";
    String SESSION_USER = "session_user";
    String EXPECTED_PAGE_ATTR = "expected_page";
    String SESSION_COOKIE = "SESSION_COOKIE";
    int COOKIE_TIME = (int) (10 * 60);

    /* ***********************
     * AJAX constants
     * 
     * ***********************
     */
    String AJAX_BEAN_KEY = "bean";
    String AJAX_FIELDS_KEY = "fields";
    String AJAX_ACTION_KEY = "action";
    String AJAX_FUNC_NAME_KEY = "funcName";
    String AJAX_RESULT_KEY = "result";
    String AJAX_VALUE_KEY = "value";
    String AJAX_ERROR_KEY = "error";
    String AJAX_ERROR_DESC = "desc";
    String AJAX_ERROR_INVALID_REQUEST = "INVALID_REQUEST";
    String AJAX_ERROR_UNKNOWN_BEAN = "UNKNOWN_BEAN";
    String AJAX_ERROR_UNKNOWN_FUNCTION = "UNKNOWN_FUNCTION";
    String AJAX_ERROR_INVALID_SYNTAX = "INVALID_SYNTAX";
    String AJAX_ACTION_UPDATE = "update";
}
