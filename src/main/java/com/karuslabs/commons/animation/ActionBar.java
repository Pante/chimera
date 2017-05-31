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
 * Represents a <code>Player</code>'s action bar. 
 * <p>
 * This class is Spigot exclusive. Attempts to run it with CraftBukkit will result in undefined behaviour.
 */
public class ActionBar {    

    private BukkitScheduler scheduler;
    private Plugin plugin;
    private String message;
    private ChatColor color;
    private int frames;
    private int maxLength;
    
    
    /**
     * Constructs a <code>ActionBar</code> with the specified <code>Plugin</code> and <code>ConfigurationSection</code>.
     * Obtains the values mapped to the keys from the specified <code>ConfigurationSection</code> if present; else the default values.
     * <p>
     * Valid keys and their default values are as follows: <pre>message: "", color: WHITE, frames: 4</pre>. Note that the value for
     * <pre>color</pre> must be in all capital letters.
     * 
     * @param plugin the plugin
     * @param config the ConfigurationSection
     */
    public ActionBar(Plugin plugin, ConfigurationSection config) {
        this(
            plugin,
            ChatColor.translateAlternateColorCodes('&', config.getString("message", "")),
            ChatColor.valueOf(config.getString("color", "WHITE")),
            config.getInt("frames", 4)
        );
    }
    
    /**
     * Constructs a <code>ActionBar</code> with the specified plugin, message, color and frames.
     * 
     * @param plugin the plugin
     * @param message the message to be animated and displayed to a player
     * @param color the color of the arrows used in the animation
     * @param frames the number of frames
     */
    public ActionBar(Plugin plugin, String message, ChatColor color, int frames) {
        this.scheduler = plugin.getServer().getScheduler();
        this.plugin = plugin;
        this.message = message;
        this.color = color;
        this.frames = frames;
        this.maxLength = frames * 2 + message.length();
    }
    
    
    /**
     * Renders an animated message to the specified <code>Player</code>'s action bar.
     * 
     * @param player the player
     */
    public void animate(Player player) {
        animate(player, 0);
    }
    
    /**
     * Renders an animated message to the specified <code>Player</code>'s action bar after the specified initial delay.
     * 
     * @param player the player
     * @param delay the initial delay
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
     * @return the message to be animated and displayed
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @return the color of the arrows used in the animation
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * @return the number of frames to be rendered
     */
    public int getFrames() {
        return frames;
    }
    
}
