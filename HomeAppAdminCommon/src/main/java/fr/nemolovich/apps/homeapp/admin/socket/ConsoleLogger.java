/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.socket;

/**
 *
 * @author Nemolovich
 */
public class ConsoleLogger implements ISocketLogger {

    @Override
    public void fine(String message) {
        System.out.println("FINE: " + message);
    }
    
    @Override
    public void info(String message) {
        System.out.println("INFO: " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("ERROR: " + message);
    }

    @Override
    public void warning(String message) {
        System.err.println("WARNING: " + message);
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
