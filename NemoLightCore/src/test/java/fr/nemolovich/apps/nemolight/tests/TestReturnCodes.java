/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.tests;

import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import org.junit.Test;

/**
 *
 * @author Nemolovich
 */
public class TestReturnCodes {

	@Test
	public void test1() {
		int code = CommandConstants.EXECUTION_WARNING_CODE
			+ CommandConstants.SYNTAX_ERROR_CODE
			+ CommandConstants.GROUP_ALREADY_EXISTS_CODE;

		if ((code | CommandConstants.EXECUTION_ERROR_CODE)
			== code) {
			System.out.println("EXECUTION_ERROR_CODE");
		}
		if ((code | CommandConstants.EXECUTION_WARNING_CODE)
			== code) {
			System.out.println("EXECUTION_WARNING_CODE");
		}
		if ((code | CommandConstants.SYNTAX_ERROR_CODE)
			== code) {
			System.out.println("SYNTAX_ERROR_CODE");
		}
		if ((code | CommandConstants.GROUP_ALREADY_EXISTS_CODE)
			== code) {
			System.out.println("GROUP_ALREADY_EXISTS_CODE");
		}
		if ((code | CommandConstants.GROUP_DOESNT_EXISTS_CODE)
			== code) {
			System.out.println("GROUP_DOESNT_EXISTS_CODE");
		}
		if ((code | CommandConstants.USER_ALREADY_EXISTS_CODE)
			== code) {
			System.out.println("USER_ALREADY_EXISTS_CODE");
		}
		if ((code | CommandConstants.USER_DOESNT_EXISTS_CODE)
			== code) {
			System.out.println("USER_DOESNT_EXISTS_CODE");
		}
	}
}
