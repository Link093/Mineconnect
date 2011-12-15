/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.packet;

import com.link093.mineconnect.util.MLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author prosicraft
 */
public class Packet3Chat {
    
    private String message = "";    // with a maximum of 120 characters        
    private static int maxSize = 120;
    
    public Packet3Chat () {        
    }
    
    public Packet3Chat (String message) {
        this.message = message;
    }
    
    public void send (PrintWriter out) {
        
        out.write(3);
        out.flush();
        
        char[] chars = new char[maxSize];
        
        for ( int i=0; i<chars.length; i++ ) {
            chars[i] = ( (message.length() > i) ? message.charAt(i) : 0 );
        }
        
        out.write(chars);
        out.flush();
        
    }
    
    public boolean eval ( int pID, BufferedReader in ) {
        
        if ( pID != 3 )
            return false;
        
        char[] theMessage = new char[maxSize];        
        try {
            in.read(theMessage);
        } catch (IOException ex) {
            MLog.e("Failure reading Packet3Chat: " + ex.getMessage());
            ex.printStackTrace();
        }
                
        message = new String (theMessage);
        return true;
        
    }
    
    public String getMessage () {
        return message;
    }
    
    
}
