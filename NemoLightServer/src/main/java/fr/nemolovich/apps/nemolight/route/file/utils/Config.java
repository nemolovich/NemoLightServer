package fr.nemolovich.apps.nemolight.route.file.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 *
 * @author Nemolovich
 */
@XmlRootElement(name = "deployment")
public class Config {

	public static final String DEPLOY_FILES_PATH
		= "server.deploy.filesPath";

	private final Map<String, Object> configs;
	private final List<ConfigMapEntry> entries;
	private static final JAXBContext CONTEXT;

	public static final Logger LOGGER = Logger.getLogger(
		Config.class);

	static {
		JAXBContext ctx = null;
		try {
			ctx = JAXBContext.newInstance(Config.class,
				ConfigList.class, ConfigMapEntry.class);
		} catch (JAXBException ex) {
			LOGGER.error("Can not initialize JAXB context", ex);
		}
		CONTEXT = ctx;
	}

	public Config() {
		this.configs = new HashMap<>();
		this.entries = new ArrayList<>();
	}

	public Object put(String key, List<String> value) {
		return this.put(key, (Object) new ConfigList(value));
	}

	public Object put(String key, String value) {
		return this.put(key, (Object) value);
	}

	public Object put(String key, Object value) {
		Object result;

		result = this.configs.put(key, value);

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
		this.configs.put(name, value);
	}
	
    public String getString(String key) {
        Object node = this.get(key);
        Node elm = (Node) node;

        return elm.getFirstChild().getNodeValue();
    }

	public static Config loadConfig(InputStream is) throws JAXBException {
		Unmarshaller um = CONTEXT.createUnmarshaller();
		Config config = (Config) um.unmarshal(is);
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

	@XmlElementWrapper(name = "configs")
	@XmlElement(name = "entry")
	public List<ConfigMapEntry> getConfigs() {
		return this.entries;
	}
}
