package fr.nemolovich.apps.nemolight.config;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nemolovich
 */
public class ClassConfig {

    private final Map<String, Object> configs;

    public ClassConfig() {
        this.configs = new HashMap<>();
    }

    public Map<String, Object> getConfigs() {
        return this.configs;
    }

    public void setConfig(String name, Object value) {
        this.configs.put(name, value);
    }

    public final Object get(String name) {
        return this.configs.get(name);
    }

    public String getString(String name) {
        Object value = get(name);
        return value == null ? null : (String) value;
    }

}
