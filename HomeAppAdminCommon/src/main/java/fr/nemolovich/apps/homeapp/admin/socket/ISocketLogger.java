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
public interface ISocketLogger {

    public void info(String message);

    public void error(String message);

    public void warning(String message);

    public void write(String message, Level level);
}
