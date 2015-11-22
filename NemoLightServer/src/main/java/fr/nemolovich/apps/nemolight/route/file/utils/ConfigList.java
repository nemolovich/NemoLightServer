package fr.nemolovich.apps.nemolight.route.file.utils;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Nemolovich
 */
@XmlType
class ConfigList extends ArrayList<String> {

    private static final long serialVersionUID = -1739796273906584252L;

    public ConfigList() {
        super();
    }

    public ConfigList(Collection<? extends String> c) {
        super(c);
    }

    @XmlElement(name = "value")
    public ConfigList getFilesPath() {
        return this;
    }
}
