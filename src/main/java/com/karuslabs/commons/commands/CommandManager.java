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

import com.karuslabs.commons.commands.events.RegistrationEvent;
import com.karuslabs.commons.commands.yml.Parser;
import com.karuslabs.commons.configuration.Configurations;

import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.*;


public class CommandManager {
    
    private Plugin plugin;
    private PluginManager manager;
    private ProxiedCommandMap map;
    private Parser parser;

    
    public CommandManager(Plugin plugin) {
        this(plugin, plugin.getServer().getPluginManager(), new ProxiedCommandMap(plugin.getServer()), new Parser(plugin));
    }
    
    public CommandManager(Plugin plugin, PluginManager manager, ProxiedCommandMap map, Parser parser) {
        this.plugin = plugin;
        this.manager = manager;
        this.map = map;
        this.parser = parser;
    }
    
    
    public List<Command> load() {
        return load("commands.yml");
    }
    
    public List<Command> load(String path) {
        List<Command> commands = parser.parse(Configurations.from(getClass().getClassLoader().getResourceAsStream(path)));
        map.registerAll(plugin.getName(), commands);
        manager.callEvent(new RegistrationEvent(commands));
        
        return commands;
    }
    
    
    public Command getCommand(String name) {
        return map.getCommand(name);
    }
    
    
    public ProxiedCommandMap getProxiedCommandMap() {
        return map;
    }

    public Parser getParser() {
        return parser;
    }
    
}
