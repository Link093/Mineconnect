/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author prosicraft
 */
public class MCInterface {        
    
    private String serverip = "wkserver.dyndns.org";
    private Socket s = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    
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
            s = new Socket (serverip, 800);
            
            in = new BufferedReader (new InputStreamReader (s.getInputStream()));
            out = new PrintWriter (s.getOutputStream(), true);                        
            
            if ( isConnected() )
                return MCResult.RES_SUCCESS;
            else
                return MCResult.RES_ERROR;
            
        } catch (IOException ioex) {
            System.out.println ("Error: Can't connect to Server at port 25565 and ip: " + serverip);
            return MCResult.RES_ERROR;
        }                        
    }    
    
    public boolean isConnected () {
        return ( in != null && out != null );
    }
    
    public MCResult disconnect () {
        
        if ( !isConnected() )
            return MCResult.RES_ALREADY;
        
        try {
            in.close();
            out.close();
            
            in = null;
            out = null;
            
            return MCResult.RES_SUCCESS;
        } catch (IOException ioex) {
            System.out.println ("Error: Can't disconnect from Server at port 25565 and ip: '" + serverip + "'");
            ioex.printStackTrace();
            return MCResult.RES_ERROR;
        }
                
    }            
    
    public MCResult send ( String txt ) {
        if ( !isConnected() )
            return MCResult.RES_NOTINIT;
        
        System.out.println ("Attempting to send message '" + txt + "' to server: '" + serverip + "'");
        
        try {
        
            out.println(txt);            
            
            if ( out.checkError() )
                throw new IOException ("Something went wrong running println ()");
            
            out.flush();                        
            
            if ( out.checkError() )
                throw new IOException ("Something went wrong running flush ()");
            
            return MCResult.RES_SUCCESS;
        } catch (IOException ioex) {
            System.out.println ("Can't send message '" + txt + "' to server: '" + serverip + "':");
            ioex.printStackTrace();
            return MCResult.RES_ERROR;
        }
    }
    
    public String getLine () {
        if ( !isConnected() )        
            return "[SYS] Connection to server not established.";
        
        try {
            
            long tPre = System.currentTimeMillis();
            while ( !in.ready() ) {                
                try {
                    Thread.sleep(1);
                    if ( (System.currentTimeMillis() - tPre) > 5000 )
                        throw new InterruptedException ("Timeout");
                } catch (InterruptedException ex) {             
                    return "[SYS] Connection timed out.";
                }                
            }                                    
            return in.readLine();    
            
        } catch (IOException ioex) {
            return "[SYS] Error while sending message.";
        }
        
    }        
    
}
