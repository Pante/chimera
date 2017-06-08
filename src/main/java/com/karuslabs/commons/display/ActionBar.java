/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.display;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import static org.apache.commons.lang.StringUtils.center;


public class ActionBar {    
                
    private BukkitScheduler scheduler;
    private Plugin plugin;
    private String message;
    private ChatColor color;
    private int frames;
    private int maxLength;
    
    
    public ActionBar(Plugin plugin, ConfigurationSection config) {
        this(
            plugin,
            ChatColor.translateAlternateColorCodes('&', config.getString("message", "")),
            ChatColor.valueOf(config.getString("color", "WHITE")),
            config.getInt("frames", 4)
        );
    }
    
    public ActionBar(Plugin plugin, String message, ChatColor color, int frames) {
        this.scheduler = plugin.getServer().getScheduler();
        this.plugin = plugin;
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
