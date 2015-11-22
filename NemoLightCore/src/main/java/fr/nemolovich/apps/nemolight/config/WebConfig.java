/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.config;

import fr.nemolovich.apps.mavendependenciesdownloader.DependenciesDownloader;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import java.util.Map;

/**
 *
 * @author Nemolovich
 */
public final class WebConfig extends ClassConfig {

    public static final String DEPLOYMENT_CLASSLOADER
        = "server.deploy.classloader";
    public static final String JQUERY_FILE
        = "jquery.file";
    public static final String DELPOYMENT_FOLDER
        = "server.deploy.folder";
    public static final String MAVEN_REPOSITORY
        = "maven.repository.url";

    private static final WebConfig INSTANCE;

    static {
        INSTANCE = new WebConfig();
    }

    public static WebConfig getInstance() {
        return INSTANCE;
    }

    private WebConfig() {
        super();
        this.setConfig(JQUERY_FILE, "jquery-1.11.1.min.js");
        this.setConfig(DELPOYMENT_FOLDER,
            String.format("%swebapp",
                NemoLightConstants.RESOURCES_FOLDER));
        this.setConfig(MAVEN_REPOSITORY,
            DependenciesDownloader.MAVEN_REPO);
    }

    @Override
    public Map<String, Object> getConfigs() {
        return super.getConfigs();
    }

    public static final Object getValue(String name) {
        return INSTANCE.get(name);
    }

    public static final String getStringValue(String name) {
        return INSTANCE.getString(name);
    }
}
