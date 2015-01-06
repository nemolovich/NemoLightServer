/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;

/**
 *
 * @author Nemolovich
 */
public class TestPattern {

	@Test
	public void testPattern() {

		Pattern hostPattern = Pattern.compile("^(?<host>("
				+ "(\\d{1,3}\\.){3}\\d{1,3})|([A-Za-z0-9.-]+))"
				+ "\\:(?<port>\\d+)$");

		Matcher matcher = hostPattern.matcher("localhost:8081");

		assertTrue(matcher.matches());

		System.out.println(matcher.group("host") + "-" + matcher.group("port"));

		matcher = hostPattern.matcher("192.168.1.69:8081");

		assertTrue(matcher.matches());

		System.out.println(matcher.group("host") + "-" + matcher.group("port"));

		matcher = hostPattern.matcher("nemolovich.dynamic-dns.net:8081");

		assertTrue(matcher.matches());

		System.out.println(matcher.group("host") + "-" + matcher.group("port"));
	}

	@Test
	public void test2Pattern() {
		String msg = "Available commands:\n"
				+ "/group_add          	Add a group to security       \n"
				+ "/hello              	Be polite! Say Hello!        \n"
				+ "/help               	Display command list or give help on command\n"
				+ "/shutdown_server    	Stop the web server                     \n"
				+ "/quit               	Leave admin console                    \n";

		Pattern cmdPattern = Pattern.compile(String.format("%s(?<cmd>\\w+)",
				CommandConstants.COMMAND_START));

		List<String> commands = new ArrayList();

		Matcher matcher = cmdPattern.matcher(msg);

		while (matcher.find()) {
			commands.add(matcher.group("cmd"));
		}
		assertEquals(commands.size(), 5);

		System.out.println(Arrays.toString(commands.toArray()));

	}
}
