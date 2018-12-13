package cc.zolux.hcf.utils;

import org.bukkit.ChatColor;

public class Color {

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
