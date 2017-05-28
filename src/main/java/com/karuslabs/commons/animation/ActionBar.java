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


public class ActionBar {    
                
    private static BukkitScheduler scheduler;
    private static Plugin plugin;
    
    
    public static void initialise(BukkitScheduler scheduler, Plugin plugin) {
        ActionBar.scheduler = scheduler;
        ActionBar.plugin = plugin;
    }
    
    
    private String message;
    private ChatColor color;
    private int frames;
    private int maxLength;
    
    
    public ActionBar(ConfigurationSection config) {
        message = ChatColor.translateAlternateColorCodes('&', config.getString("message", ""));
        color = ChatColor.valueOf(config.getString("color", "WHITE"));
        frames = config.getInt("frames", 4);
        maxLength = frames * 2 + message.length();
    }
    
    public ActionBar(String message, ChatColor color, int frames) {
        this.message = message;
        this.color = color;
        this.frames = frames;
        this.maxLength = frames * 2 + message.length();
    }
    

    public void animate(Player player) {
        animate(player, 0);
    }
    
    public void animate(Player player, int delay) {
        for (int length = message.length(); length <= maxLength; length += 2, delay += 3) {
            int capturedLength = length;
            scheduler.scheduleSyncDelayedTask(plugin, 
                () -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color + "«" + center(message, capturedLength) + color + "»")),
                delay
            );
        }
    }

    
    public String getMessage() {
        return message;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getFrames() {
        return frames;
    }
    
}
