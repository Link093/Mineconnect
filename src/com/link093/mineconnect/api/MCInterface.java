/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet1Login;

/**
 *
 * @author prosicraft
 */
public class MCInterface {        
    
    private String serverip = "wkserver.dyndns.org";
    private Socket s = null;
    private BufferedReader in = null;
    private PrintWriter outp = null;    
    
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
            s = new Socket (serverip, 5100);
            
            in = new BufferedReader (new InputStreamReader (s.getInputStream()));                                     
            outp = new PrintWriter (s.getOutputStream());    
            
            /*Packet1Login pl = new Packet1Login ("username", 2, 3, 4, (byte)2, (byte)2, (byte)2, (byte)2);
            
            pl.a (new DataOutputStream ( s.getOutputStream() ) ); */
            
            if ( isConnected() )
                return MCResult.RES_SUCCESS;
            else
                return MCResult.RES_ERROR;
            
        } catch (IOException ioex) {
            System.out.println ("Error: Can't connect to Server at port 5100 and ip: " + serverip);
            ioex.printStackTrace();
            return MCResult.RES_ERROR;
        }                        
    }    
    
    public boolean isConnected () {
        return ( in != null && outp != null );
    }
    
    public MCResult disconnect () {
        
        if ( !isConnected() )
            return MCResult.RES_ALREADY;
        
        try {
            in.close();
            outp.close();
            
            in = null;
            outp = null;
            
            return MCResult.RES_SUCCESS;
        } catch (IOException ioex) {
            System.out.println ("Error: Can't disconnect from Server at port 5100 and ip: '" + serverip + "'");
            ioex.printStackTrace();
            return MCResult.RES_ERROR;
        }
                
    }            
    
    public MCResult send ( String txt ) {
        if ( !isConnected() )
            return MCResult.RES_NOTINIT;
        
        System.out.println ("Attempting to send message '" + txt + "' to server: '" + serverip + "'");
        
        try {
        
            outp.print(txt);            
            
            if ( outp.checkError() )
                throw new IOException ("Something went wrong running println ()");
            
            outp.flush();                        
            
            if ( outp.checkError() )
                throw new IOException ("Something went wrong running flush ()");
            
            System.out.println ("Sent message.");
            
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
                    return " [SYS] Read timed out.";                    
                }                
            }            
            
            Packet p = Packet.a(new DataInputStream ( new InputStream () {
                @Override
                public int read() throws IOException {
                    return in.read();
                }                               
            } ), false);
            
            if ( p == null )
                return "(server) Null";
            else
                return p.toString();            
            
        } catch (IOException ioex) {
            return " [SYS] Error while reading message.";
        }
        
    }        
    
}
