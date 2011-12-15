/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.link093.mineconnect.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author prosicraft
 */
public class MConfiguration {
    private FileConfiguration   fc1 = null;
    private File                f1  = null;
    
    public MConfiguration (FileConfiguration fc, File f) {
        this.fc1 = fc;
        this.f1 = f;
    }

    public File getFile () {
        return f1;
    }        
    
    public FileConfiguration getConfig () {
        return fc1;
    }
    
    public void setFile (File f) {
        f1 = f;
    }
    
    public void setConfig (FileConfiguration fc) {
        fc1 = fc;
    }
    
    public void setProperty (String path, Object value) {
        fc1.set(path, value);
    }   
    
    public void set (String path, Object value) {
        fc1.set(path, value);
    }
    
    public boolean getBoolean (String path, boolean def) {
        return fc1.getBoolean(path, def);
    }
    
    public String getString (String path, String ref) {
        return fc1.getString(path, ref);
    }
    
    public String getString (String path) {
        return getString(path, "");
    }       
    
    public Set<String> getKeys (String path, boolean deep) {
        try {
            return fc1.getConfigurationSection(path).getKeys(deep);
        } catch (NullPointerException nex) {
            return new HashSet<String>();
        }
    }
    
    public Set<String> getKeys (String path) {
        return getKeys (path, false);
    }      
    
    public void save () {
        try {
            fc1.save(f1);
        } catch (IOException iex) {            
            MLog.e("Can't save configuration at " + ((f1 != null) ? f1.getAbsolutePath() : "not given configuration file!"));
        }
    }
    
    /**
     * Creates file if it doesn't exist
     */
    public void init () {
        try {                       
            if ( f1 != null && !f1.exists() ) {
                f1.createNewFile();
            }
        } catch (IOException ex) {
            MLog.e("Can't create not existing configuration at " + ((f1 != null) ? f1.getAbsolutePath() : "not given configuration file!"));
        }
    }
    
    public void load () {
        try {
            fc1.load(f1);
        } catch (IOException iex) {
            MLog.e("Can't load configuration at " + ((f1 != null) ? f1.getAbsolutePath() : "not given configuration file! (forgot init?)"));
        } catch (InvalidConfigurationException icex) {
            MLog.e("Loaded invalid configuration at " + ((f1 != null) ? f1.getAbsolutePath() : "not given configuration file! (forgot init?)"));
        }
    }
    
    public void clear () {        
        for ( String s1 : fc1.getKeys(false) )
            fc1.set(s1, null);        
    }
    
    public int getInt (String path, int def) {
        return fc1.getInt(path, def);
    }
    
    public long getLong (String path, long def) {
        return fc1.getLong(path, def);
    }
    
    public double getDouble (String path, double def) {
        return fc1.getDouble(path, def);
    }
    
    public List<String> getStringList (String path, List<String> def) {
        List res = fc1.getStringList(path);
        return ((res == null) ? ((def == null) ? new ArrayList() : def) : res);
    }
    
    public void removeProperty (String path) {
        fc1.set(path, null);
    }
    
    public void remove (String path) { removeProperty(path); }
    
    public static String normalizePath (String path) {
        return ((path.equals("")) ? "not given file" : path);
    }
    
    public static String normalizePath (File file) {
        if ( file == null ) return "[ERR:Got No File!]";
        return ((file.getAbsolutePath().equals("")) ? "not given file" : file.getAbsolutePath());
    }
} 
