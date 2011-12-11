/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author prosicraft
 */
public class MCInterface {        
    
    private String serverip = "wkserver.dyndns.org";
    private Socket s = null;
    private BufferedReader in = null;
    private OutputStream out = null;
    
    public MCInterface ( String serverip ) {
        this.serverip = serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getServerip() {
        return serverip;
    }
    
    
    // Connect to the server
    public MCResult connect(String username, String password) {
        
        if ( serverip.equals("") )
            return MCResult.RES_NOTINIT;
        
        try {
            s = new Socket (serverip, 25565);
            
            in = new BufferedReader (new InputStreamReader (s.getInputStream()));
            
            
            // TODO: out
            
        } catch (IOException ioex) {
            System.out.println ("Error: Can't connect to Server at port 25565 and ip: " + serverip);
            return MCResult.RES_ERROR;
        }
                
        
    }        
    
}
