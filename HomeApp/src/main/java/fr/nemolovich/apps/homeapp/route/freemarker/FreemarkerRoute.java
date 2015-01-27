/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.freemarker;

import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface FreemarkerRoute {

	public Object doHandle(Request request, Response response);

	public void enableSecurity();
	
}
