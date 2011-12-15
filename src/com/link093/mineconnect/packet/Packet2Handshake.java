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
 * @author passi
 */
public class Packet2Handshake {
    
    private boolean flag = false;
    
    public Packet2Handshake (boolean val) {        
        flag = val;
    }
    
    public void send (PrintWriter out) {
        
        out.write(2);        
        out.flush();
        
        out.print(flag);
        out.flush();
        
    }
    
    public boolean eval (int pID, BufferedReader in) {
        
        if ( pID != 2 )
            return false;
        
        try {
            
            flag = (in.read() == 1);
            
        } catch (IOException ioex) {
            MLog.e("Can't eval Packet2Handshake!");
            ioex.printStackTrace();
            flag = false;
        }
        
        return true;
        
    }
    
    public boolean hasAccess () {
        return flag;
    }
    
}
