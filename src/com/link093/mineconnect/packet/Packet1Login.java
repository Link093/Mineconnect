/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.packet;

import com.link093.mineconnect.api.MCInterface;
import java.io.PrintWriter;

/**
 *
 * @author passi
 */
public class Packet1Login {
    
    private static int ID = 1;
    private String myip = "";  
    private String username = "Unknown";
    private String password = "";        
    private boolean accepted = false;
    
    public Packet1Login () {                
    }
    
    public Packet1Login (String ip, String uname, String pword) {                
        myip = ip;
        username = uname;
        password = pword;
    }
    
    public void send (PrintWriter out) {
        out.print(ID);
        out.print(myip);
        out.print(username);
        out.print(password);
        out.flush();
    }
    
    public boolean getResponse ( int ID, MCInterface i ) {   
        if ( ID != this.ID )                
            return false;     
        
        int res = i.getInt();
        
        if ( res == 1 ) {   // accepted
            accepted = true;
            return true;
        } else {    // not accepted, or error            
            accepted = false;
            return true;
        }                
    } 
    
    public boolean isAccepted () {
        return accepted;
    }
    
}
