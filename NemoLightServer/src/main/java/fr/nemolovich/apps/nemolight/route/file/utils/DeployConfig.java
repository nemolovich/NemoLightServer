package fr.nemolovich.apps.nemolight.route.file.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
@XmlRootElement(name = "deployment")
public class DeployConfig {

    public static final String DEPLOY_FILES_PATH = "server.deploy.filesPath";
    public static final String DEPLOY_PACKAGE = "server.deploy.packageName";
    public static final String DEPLOY_APP_NAME = "server.deploy.appName";

    private final List<ConfigMapEntry> entries;
    private static final JAXBContext CONTEXT;

    public static final Logger LOGGER = Logger.getLogger(DeployConfig.class);

    static {
        JAXBContext ctx = null;
        try {
            ctx = JAXBContext.newInstance(DeployConfig.class, ConfigList.class,
                ConfigMapEntry.class);
        } catch (JAXBException ex) {
            LOGGER.error("Can not initialize JAXB context", ex);
        }
        CONTEXT = ctx;
    }

    public DeployConfig() {
        this.entries = new ArrayList<>();
    }

    public Object put(String key, List<String> value) {
        return this.put(key, (Object) new ConfigList(value));
    }

    public Object put(String key, String value) {
        return this.put(key, (Object) value);
    }

    public Object put(String key, Object value) {
        Object result = null;

        for (ConfigMapEntry entry : this.entries) {
            if (entry.getKey().equals(key)) {
                result = entry.getValue();
                break;
            }
        }
        this.entries.add(new ConfigMapEntry(key, value));

        return result;
    }

    public void setConfig(String name, Object value) {
        this.entries.add(new ConfigMapEntry(name, value));
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    public List<String> getList(String key) {
        return new ArrayList<>((ConfigList) this.get(key));
    }

    public static DeployConfig loadConfig(InputStream is) throws JAXBException {
        Unmarshaller um = CONTEXT.createUnmarshaller();
        DeployConfig config = (DeployConfig) um.unmarshal(is);
        return config;
    }

    public void saveConfig(OutputStream os) throws JAXBException {
        Marshaller m = CONTEXT.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(this, os);
    }

    public Object get(String key) {
        Object result = null;
        for (ConfigMapEntry entry : this.entries) {
            if (entry.getKey().equals(key)) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    public int size() {
        return this.entries.size();
    }

    @XmlElementWrapper(name = "configs")
    @XmlElement(name = "entry")
    public List<ConfigMapEntry> getConfigs() {
        return this.entries;
    }
}
