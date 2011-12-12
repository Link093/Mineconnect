/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author passi
 */
public class MCWindowListener implements WindowListener {

    private MineconnectclientView prnt;
    
    public MCWindowListener (MineconnectclientView parent) {
        prnt = parent;
    }
    
    public void windowOpened(WindowEvent we) {        
    }

    public void windowClosing(WindowEvent we) {        
    }

    public void windowClosed(WindowEvent we) {
        Connecter c = (Connecter) we.getWindow();
        if (prnt != null) {
            prnt.initInterface(c.getServerIP(), c.getUserName(), c.getPassword());            
        } else
            System.out.println("Warning: Parent in windowClosed() is null!");
    }

    public void windowIconified(WindowEvent we) {        
    }

    public void windowDeiconified(WindowEvent we) {        
    }

    public void windowActivated(WindowEvent we) {        
    }

    public void windowDeactivated(WindowEvent we) {       
    }            
}
