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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;


public class ActionBar {
    
    private BukkitScheduler scheduler;
    private Plugin plugin;
    
    
    public ActionBar(BukkitScheduler scheduler, Plugin plugin) {
        this.scheduler = scheduler;
        this.plugin = plugin;
    }
    
    
    public void animate(Player player, String... frames) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(frames[0]));
        for (int i = 1, ticks = 3; i < frames.length; i++, ticks += 2) {
            String frame = frames[i];
            scheduler.scheduleSyncDelayedTask(plugin, () -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(frame)), ticks);
        }
    }
    
}
