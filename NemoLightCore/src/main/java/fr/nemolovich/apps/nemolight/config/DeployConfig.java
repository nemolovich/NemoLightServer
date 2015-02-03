/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.config;

import java.util.Map;
import java.util.Map.Entry;
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

	@XmlElementWrapper(name = "configs")
	@Override
	public Map<String, Object> getConfigs() {
		return super.getConfigs();
	}

}
