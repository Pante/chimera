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
     * @param scheduler the server's BukkitScheduler
     * @param plugin the plugin using the ActionBar class
     */
    public static void initialise(BukkitScheduler scheduler, Plugin plugin) {
        ActionBar.scheduler = scheduler;
        ActionBar.plugin = plugin;
    }
    
    
    private String message;
    private ChatColor color;
    private int frames;
    private int maxLength;
    
    
    /**
     * Creates a new <pre>ActionBar</pre> with the <pre>ConfigurationSection</pre> specified.
     * Reads the <pre>ConfigurationSection</pre> and retrieves the values associated with the keys
     * or their default values if the key is non-existent. Valid keys and their default values are
     * as follows: <pre>message: "", color: WHITE, frames: 4</pre>. Note that the value for
     * <pre>color</pre> must be in all capital letters.
     * 
     * @param config the ConfigurationSection containing the values
     */
    public ActionBar(ConfigurationSection config) {
        message = ChatColor.translateAlternateColorCodes('&', config.getString("message", ""));
        color = ChatColor.valueOf(config.getString("color", "WHITE"));
        frames = config.getInt("frames", 4);
        maxLength = frames * 2 + message.length();
    }
    
    /**
     * Creates a new <pre>ActionBar</pre> with the message, color and frames specified.
     * 
     * @param message the message
     * @param color the color of the arrows
     * @param frames the number of frames
     */
    public ActionBar(String message, ChatColor color, int frames) {
        this.message = message;
        this.color = color;
        this.frames = frames;
        this.maxLength = frames * 2 + message.length();
    }
    
    
    /**
     * Displays an animated message on the action bar of the <pre>Player</pre> specified.
     * 
     * @param player the player the animation is to be displayed to
     */
    public void animate(Player player) {
        animate(player, 0);
    }
    
    /**
     * Displays an animated message on the action bar of the <pre>Player</pre> after the initial delay specified.
     * 
     * @param player the player the animation is to be displayed to 
     * @param delay the initial delay in ticks before the message is displayed
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
     * @return the message to be animated
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the color of the arrows
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * @return the number of animation frames
     */
    public int getFrames() {
        return frames;
    }
    
}
