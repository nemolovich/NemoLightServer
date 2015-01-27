/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.route.filter.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Nemolovich
 */
public class ServletHandler implements InvocationHandler {

	private final Object target;

	public ServletHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method,
		Object[] args) throws Throwable {
		System.out.println("Start Filter [" + method.getName() + "]!");
		boolean leave = false;
		if (leave) {
			System.out.println("Leave [" + method.getName() + "]");
		}
		Object result = method.invoke(this.target, args);
		System.out.println("End filter [" + method.getName() + "]!");
		return result;
	}

}
