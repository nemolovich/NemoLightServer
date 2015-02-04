package fr.nemolovich.apps.nemolight.constants;

import java.io.File;

public interface NemoLightConstants {

	/* ***********************
	 * Web folders
	 * 
	 * ***********************
	 */
	public static final String PACKAGE_NAME = "fr/nemolovich/apps/nemolight/";

	public static final String SRC_FOLDER = "src/main/resources";
	public static final String RESOURCES_FOLDER = "resources/";

	public static final String CONFIG_FOLDER = String.format("%s%s",
		RESOURCES_FOLDER, "/configs/");

	public static final String TEMPLATE_FOLDER = String.format("%s%s",
		RESOURCES_FOLDER, "/freemarker/");

	public static final String DEPENDENCIES_FOLDER = "dependencies";

	public static final String LOGGER_FILE_PATH = "log4j/log4j.properties";

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
	public static String JAR_META_INF = "META-INF";
	public static String JAR_MAVEN_FOLDER
		= String.format("%s/maven", JAR_META_INF);
	public static String JAR_DEPLOY_FILE
		= String.format("%s/deploy.xml", JAR_META_INF);

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
	public static final String SECURITY_PATH = String.format(
		RESOURCES_FOLDER, "/security/");
	public static final File SECURITY_CONFIG_FILE = new File(String.format(
		"%s%s", SECURITY_PATH, "security.xml"));
	public static final File SECURITY_PASSWORDS_FILE = new File(String.format(
		"%s%s", SECURITY_PATH, "passwords"));
	public static final String USER_ATTR = "session_user_attr";

	/* ***********************
	 * Session attributes
	 * 
	 * ***********************
	 */
	public static final String SESSION_USER = "session_user";
	public static final String EXPECTED_PAGE_ATTR = "expected_page";
	public static final String SESSION_COOKIE = "SESSION_COOKIE";
	public static final int COOKIE_TIME = (int) (10 * 60);

}
