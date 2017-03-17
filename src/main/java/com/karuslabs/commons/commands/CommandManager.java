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
package com.karuslabs.commons.commands;

import com.karuslabs.commons.annotations.Proxied;
import com.karuslabs.commons.commands.events.RegistrationEvent;
import com.karuslabs.commons.commands.yml.Parser;

import java.util.*;

import org.bukkit.plugin.*;


public class CommandManager {
    
    private Plugin plugin;
    private PluginManager manager;
    private ProxiedCommandMap map;
    private Parser parser;
    
    
    public CommandManager(Plugin plugin) {
        this(plugin, plugin.getServer().getPluginManager(), new ProxiedCommandMap(plugin.getServer()), new Parser(new CommandBuilder(plugin)));
    }
    
    public CommandManager(Plugin plugin, PluginManager manager, ProxiedCommandMap map, Parser parser) {
        this.plugin = plugin;
        this.manager = manager;
        this.map = map;
        this.parser = parser;
    }
    
    
    public Map<String, Command> load() {
        return load("commands.yml");
    }
    
    public Map<String, Command> load(String path) {
        Map<String, Command> commands = parser.parse(path);
        map.registerAll(plugin.getName(), new ArrayList<>(commands.values()));
        manager.callEvent(new RegistrationEvent(new ArrayList<>(commands.values())));
        
        return commands;
    }
    
    
    @Proxied
    public Command getCommand(String name) {
        return map.getCommand(name);
    }
    
    
    public ProxiedCommandMap getProxiedCommandMap() {
        return map;
    }
    
}
