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
public class Packet1Login {
    
    private static int ID = 1;
    private int myip = 0;
    private String username = "Unknown";
    private String password = "";        
    //private boolean accepted = false;
    
    public Packet1Login () {                
    }
    
    public Packet1Login (int ip, String uname, String pword) {                
        myip = ip;
        username = uname;
        password = pword;
    }
    
    public void send ( PrintWriter out ) {
        
        out.write(1);                
        
        char[] uname = new char[16]; 
        
        for ( int i=0; i < 16; i++ )
            uname[i] = ( (username.length() > i) ? username.charAt(i) : 0 );
        
        out.write(uname);
        
        char[] pword = new char[16];
        
        for ( int i=0; i < 16; i++ )
            pword[i] = ( (password.length() > i) ? password.charAt(i) : 0 );
        
        out.write(pword);
        
        out.flush();
        
    }
    
    public boolean eval ( int sID, BufferedReader s ) {
        
        MLog.d("Packet1Login got id: " + sID + " ('" + String.valueOf(new char[] { (char)sID}));
        
        if ( sID != ID )
            return false;
        
        MLog.d("Packet1Login now checking...");
        
        /*if ( (myip = s.getInt()) == 0 )
            MLog.e("Received damaged packet with id " + String.valueOf(sID) + ": No IP!");
        
        MLog.d("Got ip: " + myip); */
        
        char[] uname = new char[16];        
        try {
            s.read(uname);
        } catch (IOException ex) {
            MLog.e("IOException reading username on Packet1Login: " + ex.getMessage());
            ex.printStackTrace();
        }      
        username = String.valueOf(uname);                
        if ( username.trim().equals("") )
            MLog.e("Received damaged packet with id " + String.valueOf(sID) + ": No Username!");
        
        MLog.d("Got username: '" + username + "'");
        
        char[] pword = new char[16];
        try {
            s.read(pword);
        } catch (IOException ex) {
            MLog.e("IOException reading password on Packet1Login: " + ex.getMessage());
            ex.printStackTrace();
        }
        if ( (password = String.valueOf(pword)).trim().equals("") )
            MLog.e("Received damaged packet with id " + String.valueOf(sID) + ": No Password!");
        
        MLog.d("Got password: '" + password + "'");                                
        
        return true;
        
    }   
    
    public String getUsername () {
        return username;                
    }

    public String getPassword() {
        return password;
    }        
    
}
