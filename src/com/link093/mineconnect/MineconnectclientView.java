/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect;

import com.link093.mineconnect.api.MCInterface;
import com.link093.mineconnect.api.MCResult;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author prosicraft
 */
public class MineconnectclientView extends JFrame implements Runnable {
    
    // GUI
    private JButton OKButton;
    private JMenuItem jMenuItem1;
    private JScrollPane jScrollPane1;
    private JTextArea console;
    private JTextField statusBar;
    private JTextField inputField;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    
    private MCInterface theInterface;
    
    private boolean running = false;
    private Thread thread = null;
    
    public MineconnectclientView () {
        super ("Mineconnect");
        initComponents();
    }
    
    public synchronized void start() {        
        if (running) return;
        //initComponents();
        startReadThread();
        System.out.println("Thread started.");
        running = true;        
        //thread = new Thread (this);
        //thread.start();        
    }
    
    public synchronized void stop () {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }        
    
    public synchronized void run() {
        //while (running) {            
        //}
    }
    
    public void initInterface ( String sip, String uname, String pwd ) {
        System.out.println("In initInterface()");
        writeLine (" [SYS] Trying to connect...");           
        System.out.println ("Now connecting...");
        final String serverip = sip;
        final String username = uname;
        final String password = pwd;
        
        (new Thread () {                       
            @Override
            public void run() {
                theInterface = new MCInterface (serverip);   
                MCResult res;
                if ((res = theInterface.connect(username, password)) == MCResult.RES_SUCCESS) {
                    startReadThread();
                    writeLine (" [SYS] Connected.");                
                }
                else
                    writeLine (" [SYS] Can't connect: " + res.toString());
            }                 
        }).start();
    }
    
    public void startReadThread () {        
        (new Thread () {

            @Override
            public void run() {
                while (true) {
                    try {
                        this.sleep(25);
                    } catch (InterruptedException ex) {                        
                    }
                    
                    if ( theInterface == null )
                        continue;
                    
                    try {
                        if ( !theInterface.isConnected() )
                            break;
                        
                        String res = "No response";
                        res = theInterface.getLine();
                        writeLine (res);
                        Thread.sleep(250);                        
                    } catch (Exception ex) {
                        writeLine (" [SYS] Can't read from server.");
                        theInterface.disconnect();
                        writeLine (" [SYS] Disconnected. ");
                        break;
                    }
                }
            }
        }).start();
    }
    
    public synchronized void writeLine (String txt) {
        final String thetext = txt;
        (new Thread () {            

            @Override
            public void run() {                
                console.append("\n" + thetext);                               
            }           
        }).start();        
    }
    
    public static void main(String[] args) {        
        MineconnectclientView theView = new MineconnectclientView();        
        theView.start();
    }
    
    
    private void doConnect (ActionEvent evt) {
        MCConnector c = new MCConnector ();
        c.setLocation(this.getX() + 25, this.getY() +25);
        c.setVisible(true);    
        c.addWindowListener(new MCWindowListener(this));
    }
        
    private void evalOKOnEnter (KeyEvent evt) {
        
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            
            if ( inputField.getText().trim().equalsIgnoreCase("/disconnect") ) {
                
                if ( this.theInterface != null && this.theInterface.isConnected() ) {
                    MCResult res;
                    if ( ( res = theInterface.disconnect() ) == MCResult.RES_SUCCESS )
                        writeLine (" [SYS] Disconnected from server on demand.");
                    else
                        writeLine (" [SYS] Can't disconnect from server: " + String.valueOf(res));
                } else {
                    writeLine (" [SYS] There's no connection to any server.");                                        
                }                               
                                                
            } else {
                
                String prefix = "(not sent) ";
                
                if ( this.theInterface != null && this.theInterface.isConnected() ) {
                    theInterface.send(inputField.getText().trim());
                    prefix = "(sent) ";
                }
                
                writeLine (prefix + "YOU: " + inputField.getText().trim());                
                
            }
            
            inputField.setText("");
            
        }
        
    }        
    
    private void initComponents() {               
        
        mainPanel = new javax.swing.JPanel();
        statusBar = new javax.swing.JTextField();
        inputField = new javax.swing.JTextField();
        OKButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setMaximumSize(new java.awt.Dimension(500, 300));
        //mainPanel.setPreferredSize(new java.awt.Dimension(500, 300));        
        //mainPanel.setSize(500, 500);
        mainPanel.setName("mainPanel"); 
        
        statusBar.setBackground(Color.GRAY);       
        statusBar.setText("This is the status bar");
        statusBar.setName("statusBar"); 

        inputField.setText(""); 
        inputField.setName("inputField"); 
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                evalOKOnEnter(evt);
            }
        });

        OKButton.setText("OK"); 
        OKButton.setName("OKButton"); 

        jScrollPane1.setName("jScrollPane1"); 

        console.setColumns(50);
        console.setEditable(true);
        console.setRows(30);
        console.setText("This is the console output"); 
        console.setName("console");        
        //console.setSize(500, 500);
        jScrollPane1.setViewportView(console);
        
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        //mainPanel.add(console, BorderLayout.CENTER);    
        //mainPanel.add(inputField, BorderLayout.PAGE_END); 
        mainPanelLayout.setHorizontalGroup(                
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)            
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OKButton)
                .addContainerGap())
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OKButton)))                
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText("File"); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Connect..."); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doConnect(evt);
            }
        });
        fileMenu.add(jMenuItem1);
                
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help"); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        //aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);                                                             
                
        setSize (400,300); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                        
        setLayout (new BorderLayout ());
                              
        //mainPanel.add(console);
        //mainPanel.setLayout(new GridLayout (2,1))
        add (menuBar, BorderLayout.PAGE_START);
        getContentPane().add(mainPanel);                   
        //getContentPane().add(inputField);
        
        pack ();
        setVisible(true);                      
        //setJMenuBar(menuBar);                
        
        inputField.requestFocusInWindow();
        
        //mainPanel.revalidate();
        //mainPanel.repaint();  
        //mainPanel.setVisible(true);
        //this.repaint();
        
    }
           
}
