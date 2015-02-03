/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.commands;

/**
 *
 * @author Nemolovich
 */
public class UnkownCommandException extends Exception {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = -2758797314430074333L;

	public UnkownCommandException(String command) {
		super(String.format("Unknown command '%s'", command));
	}

}
