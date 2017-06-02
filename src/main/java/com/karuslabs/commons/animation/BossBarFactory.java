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

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class BossBarFactory {
    
    private static final BarFlag[] FLAGS = new BarFlag[0];
    
    private Server server;
    private String message;
    private BarColor color;
    private BarStyle style;
    private BarFlag[] flags;
    
    
    public BossBarFactory(Server server, ConfigurationSection config) {
        this(
            server,
            ChatColor.translateAlternateColorCodes('&', config.getString("message", "")),
            BarColor.valueOf(config.getString("color", "PURPLE")),
            BarStyle.valueOf(config.getString("style", "SEGMENTED_10")),
            config.getStringList("flags").stream().map(BarFlag::valueOf).collect(Collectors.toList()).toArray(new BarFlag[0])
        );
    }
    
    public BossBarFactory(Server server, String message, BarColor color, BarStyle style) {
        this(server, message, color, style, FLAGS);
    }
    
    public BossBarFactory(Server server, String message, BarColor color, BarStyle style, BarFlag[] flags) {
        this.server = server;
        this.message = message;
        this.color = color;
        this.style = style;
        this.flags = flags;
    }

    
    public BossBar newBar(Player... players) {
        BossBar bar = server.createBossBar(message, color, style, flags);
        for (Player player : players) {
            bar.addPlayer(player);
        }
        
        return bar;
    }
    
    public BossBar newBar(Collection<Player> players) {
        BossBar bar = server.createBossBar(message, color, style, flags);
        System.out.println(bar);
        players.forEach(bar::addPlayer);
        
        return bar;
    }

    
    public String getMessage() {
        return message;
    }

    public BarColor getColor() {
        return color;
    }

    public BarStyle getStyle() {
        return style;
    }
    
    public BarFlag[] getFlags() {
        return Arrays.copyOf(flags, flags.length);
    }
    
}
