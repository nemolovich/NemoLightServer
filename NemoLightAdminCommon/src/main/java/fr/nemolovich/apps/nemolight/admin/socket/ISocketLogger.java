/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.admin.socket;

/**
 *
 * @author Nemolovich
 */
public interface ISocketLogger {

    void fine(String message);

    void info(String message);

    void error(String message);

    void warning(String message);

    void write(String message, Level level);
}
