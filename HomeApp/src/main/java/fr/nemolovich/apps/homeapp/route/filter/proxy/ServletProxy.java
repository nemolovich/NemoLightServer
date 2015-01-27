/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.filter.proxy;

import java.lang.reflect.Proxy;

/**
 *
 * @author Nemolovich
 */
public class ServletProxy extends Proxy {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 4617670658465141497L;

	public ServletProxy(ServletHandler handler) {
		super(handler);
	}

}
