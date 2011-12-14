/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.api;

import com.link093.mineconnect.packet.Packet1Login;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

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
    public MCResult connect() {
        
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
    
    public MCResult login (String username, String password) {
        
        String address = "";
        
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            System.out.println ("Can't get IP adress");
        }
        
        Packet1Login p = new Packet1Login(address, username, password);
        
        p.send(outp);             
        
        long tPre = System.currentTimeMillis();        
        while ( ( System.currentTimeMillis() - tPre ) < 5000 ) {
            try {
                if ( in.ready() )
                    break;
            } catch (IOException ex) {
                System.out.println ("Can't establish ready().");
                return MCResult.RES_ERROR;
            }
        }        
        
        try {
            if ( !in.ready() )
                return MCResult.RES_ERROR;
        } catch (IOException ex) {
            System.out.println ("Can't establish ready() (lastAttempt)");
        }
        
        if ( p.getResponse(this.readNextPacket(), this) )
            return MCResult.RES_SUCCESS;
        else
            return MCResult.RES_NOACCESS;
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
    
    public int readNextPacket () {
        int res = 0;
        if ( (res = getInt ()) == -1 ) {
            System.out.println ("There is no more packet");
        }
        return res;
    }
    
    public String getString () {
        if ( !isConnected() )        
            return "[SYS] Connection to server not established.";
        
        try {
            
            //long tPre = System.currentTimeMillis();            
            if ( in.ready() ) {
                                
                char[] cbuf = new char[200];                                
                in.read(cbuf);                
                
                return new String (cbuf);
                
            }                                                                                                      
            return "";
            
        } catch (IOException ioex) {
            return " [SYS] Error while reading String.";
        }
        
    }         
    
    public int getInt () {
        if ( !isConnected() )        
            return -1;
        
        try {
            
            //long tPre = System.currentTimeMillis();            
            if ( in.ready() ) {
                                
                int res = in.read();                                          
                return res;
                
            }                                                                                                      
            return -1;
            
        } catch (IOException ioex) {
            return -1;
        }
        
    }
    
}
