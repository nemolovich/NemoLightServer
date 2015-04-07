package fr.nemolovich.apps.nemolight.constants;

import java.io.File;

public interface NemoLightConstants {

	/* ***********************
	 * Deployment constants
	 * 
	 * ***********************
	 */
	public static final String PACKAGE_NAME = "fr/nemolovich/apps/nemolight/";
	public static final String APP_IDENTIFIER = "APP_IDENTIFIER";
	public static final String APP_NAME = "APP_NAME";
	public static final String APP_PACKAGE = "APP_PACKAGE";

	/* ***********************
	 * Web folders
	 * 
	 * ***********************
	 */
	public static final String SRC_FOLDER = "src/main/resources";
	public static final String RESOURCES_FOLDER = "resources/";

	public static final String CONFIG_FOLDER = String.format("%s%s",
		RESOURCES_FOLDER, "/configs/");

	public static final String TEMPLATE_FOLDER = String.format("%s%s",
		RESOURCES_FOLDER, "/freemarker/");

	public static final String DEPENDENCIES_FOLDER = "dependencies";

	public static final String LOGGER_FILE_PATH = "log4j/log4j.properties";

	public static final String DEPLOY_FOLDER = "deploy";

	/* ***********************
	 * Protocols to read file
	 * 
	 * ***********************
	 */
	public static final String FILE_PROTOCOL = "file";
	public static final String JAR_PROTOCOL = "jar";

	/* ***********************
	 * JARs constants
	 * 
	 * ***********************
	 */
	public static final String JAR_META_INF = "META-INF";
	public static final String JAR_MAVEN_FOLDER = String.format("%s/maven",
		JAR_META_INF);
	public static final String JAR_DEPLOY_FILE = String.format("%s/deploy.xml",
		JAR_META_INF);

	/* ***********************
	 * Folder search options
	 * 
	 * ***********************
	 */
	public static final int DEFAULT_SEARCH = 0;
	public static final int RECURSIVE_SEARCH = 1;

	/* ***********************
	 * Resources search options
	 * 
	 * ***********************
	 */
	public static final int INCLUDE_ALL = 2;
	public static final int ONLY_CLASS_FILES = 4;
	public static final int EXCLUDE_CLASS_FILES = 8;
	public static final int ONLY_FOLDERS = 16;
	public static final int EXCLUDE_FOLDERS = 32;

	/* ***********************
	 * Security marshaling
	 * 
	 * ***********************
	 */
	public static final String SECURITY_PATH = String.format(RESOURCES_FOLDER,
		"/security/");
	public static final File SECURITY_CONFIG_FILE = new File(String.format(
		"%s%s", SECURITY_PATH, "security.xml"));
	public static final File SECURITY_PASSWORDS_FILE = new File(String.format(
		"%s%s", SECURITY_PATH, "passwords"));

	/* ***********************
	 * Session attributes
	 * 
	 * ***********************
	 */
	public static final String SESSION_ATTR = "Session";
	public static final String REQUEST_ATTR = "RequestParameters";
	public static final String SESSION_USER = "session_user";
	public static final String EXPECTED_PAGE_ATTR = "expected_page";
	public static final String SESSION_COOKIE = "SESSION_COOKIE";
	public static final int COOKIE_TIME = (int) (10 * 60);

	/* ***********************
	 * AJAX constants
	 * 
	 * ***********************
	 */
	public static final String AJAX_BEAN_KEY = "bean";
	public static final String AJAX_FIELDS_KEY = "fields";
	public static final String AJAX_ACTION_KEY = "action";
	public static final String AJAX_FUNC_NAME_KEY = "funcName";
	public static final String AJAX_RESULT_KEY = "result";
	public static final String AJAX_VALUE_KEY = "value";
	public static final String AJAX_ERROR_KEY = "error";
	public static final String AJAX_ERROR_DESC = "desc";
	public static final String AJAX_ERROR_INVALID_REQUEST = "INVALID_REQUEST";
	public static final String AJAX_ERROR_UNKNOWN_BEAN = "UNKNOWN_BEAN";
	public static final String AJAX_ERROR_UNKNOWN_FUNCTION = "UNKNOWN_FUNCTION";
	public static final String AJAX_ERROR_INVALID_SYNTAX = "INVALID_SYNTAX";
	public static final String AJAX_ACTION_UPDATE = "update";
}
