/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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


/**
 * Acts as a proxy for the server's internal CommandMap implementation.
 * 
 * This class does not replace the server's internal CommandMap implementation.
 * Allows plug-ins to listen for {@link com.karusmc.commons.commands.events.CommandRegistrationEvent} registered via this class.
 * {@link com.karusmc.commons.commands.events.CommandRegistrationEvent} will be called only when commands are registered through
 * this class and not if directly through the underlying CommandMap implementation.
 */
public class CommandMapProxy {
 
    private SimpleCommandMap map;
    private PluginManager manager;
    
    
    /**
     * Constructs this with the specified server's CommandMap implementation obtained via reflection.
     * 
     * @param server the server
     */
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
    
    
    /**
     * Registers all commands and notifies all {@link com.karusmc.commons.commands.events.CommandRegistrationEvent} listeners.
     * 
     * @param commands The commands to be registered
     */
    public void register(Map<String, Command> commands) {
        commands.forEach(this::register);
    }
    
    
    /**
     * Registers a command and notifies all {@link com.karusmc.commons.commands.events.CommandRegistrationEvent} listeners.
     * 
     * @param fallbackPrefix A prefix which is prepended to the command with a ':' one or more times to make the command unique
     * @param command The command to be registered
     */
    public void register(String fallbackPrefix, Command command) {
        CommandRegistrationEvent event = new CommandRegistrationEvent(command);
        
        manager.callEvent(event);
        if (!event.isCancelled()) {
            map.register(fallbackPrefix, command);
        }
    }
    
    
    /**
     * Retrieves a specified plug-in's commands.
     * 
     * @param pluginName The name of the plug-in
     * @return A map of command names and commands
     */
    public Map<String, org.bukkit.command.Command> getPluginCommands(String pluginName) {
        return map.getCommands().stream()
                .filter(command -> command instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand) command).getPlugin().getName().equals(pluginName)) 
                .collect(Collectors.toMap(command -> command.getName(), command -> command));
    }
    
    
    /**
     * 
     * @return The proxied CommandMap implementation
     */
    public SimpleCommandMap getCommandMap() {
        return map;
    }
    
}
