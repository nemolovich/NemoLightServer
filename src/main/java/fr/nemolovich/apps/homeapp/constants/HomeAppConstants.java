package fr.nemolovich.apps.homeapp.constants;


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

}
