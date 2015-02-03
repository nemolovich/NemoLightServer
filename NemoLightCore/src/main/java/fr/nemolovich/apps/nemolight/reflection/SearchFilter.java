/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.reflection;

/**
 *
 * @author Nemolovich
 */
public abstract class SearchFilter {

	public abstract boolean filterMatches(Class<?> clazz);
}
