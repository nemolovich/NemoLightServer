package fr.nemolovich.apps.homeapp.constants;

import java.io.File;

public interface HomeAppConstants {

	/* ***********************
	 * Web folders
	 * 
	 * ***********************
	 */
	public static final String PACKAGE_NAME = "fr/nemolovich/apps/homeapp/";

	public static final String SRC_FOLDER = "src/main/resources";
	public static final String RESOURCES_FOLDER = "resources/";

	public static final String CONFIG_FOLDER = RESOURCES_FOLDER
			.concat("/configs/");

	public static final String TEMPLATE_FOLDER = RESOURCES_FOLDER
			.concat("/freemarker/");

	/* ***********************
	 * Protocols to read file
	 * 
	 * ***********************
	 */
	public static final String FILE_PROTOCOL = "file";
	public static final String JAR_PROTOCOL = "jar";

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
	public static final String SECURITY_PATH = RESOURCES_FOLDER
			.concat("/security/");
	public static final File SECURITY_CONFIG_FILE = new File(
			SECURITY_PATH.concat("security.xml"));

}
