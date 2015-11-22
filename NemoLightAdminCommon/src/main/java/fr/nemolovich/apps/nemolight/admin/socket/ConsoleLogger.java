/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.socket;

import java.io.PrintStream;

/**
 *
 * @author Nemolovich
 */
public class ConsoleLogger implements ISocketLogger {

    private static final PrintStream OUT = System.out;
    private static final PrintStream ERR = System.err;

    @Override
    public void fine(String message) {
        OUT.println("FINE: " + message);
    }

    @Override
    public void info(String message) {
        OUT.println("INFO: " + message);
    }

    @Override
    public void error(String message) {
        ERR.println("ERROR: " + message);
    }

    @Override
    public void warning(String message) {
        ERR.println("WARNING: " + message);
    }

    @Override
    public void write(String message, Level level) {
        switch (level) {
            case FINE:
                fine(message);
                break;
            case INFO:
                info(message);
                break;
            case WARNING:
                warning(message);
                break;
            case ERROR:
                error(message);
                break;
            default:
                break;
        }
    }

}
