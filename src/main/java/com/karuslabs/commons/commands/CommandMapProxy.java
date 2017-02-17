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

import com.karuslabs.commons.commands.events.CommandRegistrationEvent;

import java.lang.reflect.*;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Server;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;


public class CommandMapProxy {
 
    private SimpleCommandMap map;
    private PluginManager manager;
    
    
    public CommandMapProxy(Server server) {
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            
            map = (SimpleCommandMap) field.get(server);
            manager = server.getPluginManager();
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Server does not contain field: commandMap", e);
        }
    }
    
    
    public void register(Map<String, Command> commands) {
        commands.forEach(this::register);
    }
    
    
    public void register(String fallbackPrefix, Command command) {
        CommandRegistrationEvent event = new CommandRegistrationEvent(command);
        
        manager.callEvent(event);
        if (!event.isCancelled()) {
            map.register(fallbackPrefix, command);
        }
    }
    
    
    public Map<String, org.bukkit.command.Command> getPluginCommands(String pluginName) {
        return map.getCommands().stream()
                .filter(command -> command instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand) command).getPlugin().getName().equals(pluginName)) 
                .collect(Collectors.toMap(command -> command.getName(), command -> command));
    }
    
    
    public SimpleCommandMap getCommandMap() {
        return map;
    }
    
}
