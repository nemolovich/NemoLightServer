/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Nemolovich
 */
public class TestPattern {

    @Test
    public void testPattern() {

        Pattern hostPatern = Pattern.compile("^(?<host>("
            + "(\\d{1,3}\\.){3}\\d{1,3})|([A-Za-z0-9.-]+))"
            + "\\:(?<port>\\d+)$");

        Matcher matcher = hostPatern.matcher("localhost:8081");

        assertTrue(matcher.matches());
        
        System.out.println(matcher.group("host")+"-"+matcher.group("port"));

        matcher = hostPatern.matcher("192.168.1.69:8081");

        assertTrue(matcher.matches());
        
        System.out.println(matcher.group("host")+"-"+matcher.group("port"));

        matcher = hostPatern.matcher("nemolovich.dynamic-dns.net:8081");

        assertTrue(matcher.matches());
        
        System.out.println(matcher.group("host")+"-"+matcher.group("port"));
    }
}
