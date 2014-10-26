/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.admin.gui;

import fr.nemolovich.apps.homeapp.admin.socket.ISocketLogger;
import fr.nemolovich.apps.homeapp.admin.socket.Level;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Nemolovich
 */
public class SocketLoggerArea extends JTextPane implements ISocketLogger {

    private static final StyleContext sc = new StyleContext();
    private static final Style DEFAULT_STYLE
        = sc.getStyle(StyleContext.DEFAULT_STYLE);
    private static final Style ERROR_STYLE
        = sc.addStyle("ERROR_STYLE", DEFAULT_STYLE);
    private static final Style WARNING_STYLE
        = sc.addStyle("WARNING_STYLE", DEFAULT_STYLE);

    static {
        StyleConstants.setForeground(ERROR_STYLE, Color.decode("#8A0808"));
        StyleConstants.setForeground(WARNING_STYLE, Color.decode("#8A4B08"));
    }

    @Override
    public void info(String message) {
        this.write(message, Level.INFO);
    }

    @Override
    public void error(String message) {
        this.write(message, Level.ERROR);
    }

    @Override
    public void warning(String message) {
        this.write(message, Level.WARNING);
    }

    @Override
    public void write(String message, Level level) {

        Style style;
        switch (level) {
            case INFO:
                style = DEFAULT_STYLE;
                break;
            case WARNING:
                style = WARNING_STYLE;
                break;
            case ERROR:
                style = ERROR_STYLE;
                break;
            default:
                style = DEFAULT_STYLE;
                break;
        }

        try {
            this.getDocument().insertString(this.getDocument().getLength(),
                message, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(SocketLoggerArea.class.getName()).log(
                java.util.logging.Level.SEVERE, null, ex);
        }

    }

}
