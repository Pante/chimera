/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.animation;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import static org.apache.commons.lang.StringUtils.center;


/**
 * Represents a Player's action bar. The <pre>ActionBar</pre> class must first be 
 * initialized via {@link #initialise(BukkitScheduler, Plugin)} before calling either 
 * {@link #animate(Player)} or {@link #animate(Player, int)}.
 */
public class ActionBar {    
                
    private static BukkitScheduler scheduler;
    private static Plugin plugin;
    
    
    /**
     * Initializes the <pre>ActionBar</pre> class with the <pre>BukkitScheduler</pre> 
     * and <pre>Plugin</pre> specified. Must be called before either 
     * {@link #animate(Player)} or {@link #animate(Player, int)} may be called.
     * 
     * @param scheduler the server's <pre>BukkitScheduler</pre>
     * @param plugin the plugin using the <pre>ActionBar</pre> class.
     */
    public static void initialise(BukkitScheduler scheduler, Plugin plugin) {
        ActionBar.scheduler = scheduler;
        ActionBar.plugin = plugin;
    }
    
    
    private String message;
    private ChatColor color;
    private int maxLength;
    
    
    /**
     * 
     * 
     * @param config 
     */
    public ActionBar(ConfigurationSection config) {
        message = ChatColor.translateAlternateColorCodes('&', config.getString("message", ""));
        color = ChatColor.valueOf(config.getString("color", "WHITE"));
        maxLength = config.getInt("frames", 4) * 2 + message.length();
    }
    
    /**
     * 
     * 
     * @param message
     * @param color
     * @param frames 
     */
    public ActionBar(String message, ChatColor color, int frames) {
        this.message = message;
        this.color = color;
        this.maxLength = frames;
    }
    
    
    /**
     * 
     * 
     * @param player 
     */
    public void animate(Player player) {
        animate(player, 0);
    }
    
    /**
     * 
     * 
     * @param player
     * @param delay 
     */
    public void animate(Player player, int delay) {
        for (int length = message.length(); length <= maxLength; length += 2, delay += 3) {
            int capturedLength = length;
            scheduler.scheduleSyncDelayedTask(plugin, 
                () -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color + "«" + center(message, capturedLength) + color + "»")),
                delay
            );
        }
    }

    
    /**
     * 
     * 
     * @return 
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * 
     * @return 
     */
    public ChatColor getColor() {
        return color;
    }
    
    /**
     * 
     * 
     * @return 
     */
    public int getMaxLength() {
        return maxLength;
    }
    
}
