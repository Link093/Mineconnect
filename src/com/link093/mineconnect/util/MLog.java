package com.link093.mineconnect.util;



import org.bukkit.ChatColor;



public class MLog {	
	
	public static void i (String txt, ChatColor col) {
		System.out.println(ChatColor.stripColor(col + MConst._OUT_PREFIX + "::INFO] " + txt));
	}
	
	public static void i (String txt) {
		i (txt, ChatColor.WHITE);
	}
	
	public static void s (String txt, ChatColor col) {
		System.out.println(ChatColor.stripColor(col + MConst._OUT_PREFIX + "::SUCCESS] " + txt));
	}
	
	public static void s (String txt) {
		i (txt, ChatColor.GREEN);
	}
	
	public static void e (String txt, ChatColor col) {
		System.out.println(ChatColor.stripColor(col + MConst._OUT_PREFIX + "::ERROR] " + txt));
	}
	
	public static void e (String txt) {
		e (txt, ChatColor.RED);
	}
	
	public static void w (String txt, ChatColor col) {
		System.out.println(ChatColor.stripColor(col + MConst._OUT_PREFIX + "::WARNING] " + txt));
	}
	
	public static void w (String txt) {
		w (txt, ChatColor.GOLD);
	}
	
	public static void d (String txt, ChatColor col) {
            if ( MConst._DEBUG_ENABLED )
                System.out.println(ChatColor.stripColor(col + MConst._OUT_PREFIX + "::DEBUG] " + txt));
	}
	
	public static void d (String txt) {
		d (txt, ChatColor.GRAY);
	}
        
        public static String real(String src) {        
            if(src == null) return null;
            return src.replaceAll("&([0-9a-f])", "\u00A7$1");   
        }
	
}
