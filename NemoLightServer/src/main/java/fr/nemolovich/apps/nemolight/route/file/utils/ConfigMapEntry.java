package fr.nemolovich.apps.nemolight.route.file.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 *
 * @author Nemolovich
 */
class ConfigMapEntry {

    private String key;
    private Object value;

    public ConfigMapEntry() {
    }

    public ConfigMapEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlElement
    public String getKey() {
        return this.key;
    }

    @XmlElements({
        @XmlElement(name = "list", type = ConfigList.class),
        @XmlElement(name = "value", type = String.class)})
    public Object getValue() {
        return this.value;
    }
}
