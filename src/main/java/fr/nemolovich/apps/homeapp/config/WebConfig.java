/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.config;

import java.util.Map;

/**
 *
 * @author Nemolovich
 */
public class WebConfig extends ClassConfig {

    private static final WebConfig INSTANCE;

    static {
        INSTANCE = new WebConfig();
    }

    public static WebConfig getInstance() {
        return INSTANCE;
    }

    private WebConfig() {
        super();
        this.setConfig("page.error", "/error");
        this.setConfig("page.error.template", "error.ftl");
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
