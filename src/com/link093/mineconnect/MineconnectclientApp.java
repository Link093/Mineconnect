/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect;

import java.applet.Applet;
import java.awt.BorderLayout;

/**
 *
 * @author prosicraft
 */
public class MineconnectclientApp extends Applet {
    private static final long serialVersionUID = 1L;
    
    private MineconnectclientView view = new MineconnectclientView();
    
    @Override
    public void init () {
        setLayout (new BorderLayout());
        add (view, BorderLayout.CENTER);
    }
    
    @Override
    public void start () {
        view.start();
    }
    
    @Override
    public void stop () {
        view.stop();
    }
}
