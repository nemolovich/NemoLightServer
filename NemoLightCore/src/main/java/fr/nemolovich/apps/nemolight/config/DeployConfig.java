package fr.nemolovich.apps.nemolight.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Node;

/**
 *
 * @author Nemolovich
 */
@XmlRootElement(name = "deployment")
public class DeployConfig extends ClassConfig {

    public static final String DEPLOY_PACKAGE
        = "server.deploy.packageName";

    public DeployConfig() {
        super();
    }

    @Override
    public String getString(String key) {
        Object node = this.get(key);
        Node elm = (Node) node;

        return elm.getFirstChild().getNodeValue();
    }

    public void setConfigs(Map<String, Object> configs) {
        for (Entry entry : configs.entrySet()) {
            this.setConfig((String) entry.getKey(),
                entry.getValue());
        }
    }

    public static DeployConfig loadConfig(InputStream is) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(DeployConfig.class);
        Unmarshaller um = context.createUnmarshaller();
        DeployConfig config = (DeployConfig) um.unmarshal(is);
        return config;
    }

    @XmlElementWrapper(name = "configs")
    @Override
    public Map<String, Object> getConfigs() {
        return super.getConfigs();
    }

}
