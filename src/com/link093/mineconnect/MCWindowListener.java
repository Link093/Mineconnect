/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author prosicraft
 */
public class MCWindowListener implements WindowListener {

    private MineconnectclientView prnt;
    
    public MCWindowListener (MineconnectclientView parent) {
        System.out.println ("Initialized MCWindowListener");
        prnt = parent;
    }
    
    public void windowOpened(WindowEvent we) {        
    }

    public void windowClosing(WindowEvent we) {                
    }

    public void windowClosed(WindowEvent we) {        
        MCConnector c = null;        
        
        if ( we.getWindow() instanceof MCConnector )
            c = (MCConnector) we.getWindow();    
        
        if ( !c.clickedConnect )    // normal window-close
            return;
        
        if (prnt != null) {
            
            System.out.println (" Connecting to IP: " + c.getServerIP());               
            System.out.println (" Connecting with Username: " + c.getUserName());
            System.out.println (" Connecting with Password: " + String.valueOf(c.getPassword()));            
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
