/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.core;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandMapProxy extends Observable implements CommandMap {
    
    private SimpleCommandMap commandMap;
    
    
    public CommandMapProxy(Server server) {
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            
            commandMap = (SimpleCommandMap) field.get(server);
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Server class doest not contain field: commandMap", e);
        }
        
    }

    
    @Override
    public Command getCommand(String name) {
        return commandMap.getCommand(name);
    }
    
    
    public Map<String, Command> getPluginCommands(Plugin plugin) {
        return commandMap.getCommands().stream()
                .filter(command -> command instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand) command).getPlugin().equals(plugin))
                .collect(Collectors.toMap(command -> command.getName(), command -> command));
    }
    
    
    public Map<String, XMCommand> getXMCommands(Plugin plugin) {
        return commandMap.getCommands().stream()
                .filter(command -> command instanceof XMCommand && ((XMCommand) command).getPlugin().equals(plugin))
                .collect(Collectors.toMap(command -> command.getName(), command -> (XMCommand) command));
    }
    
    
    @Override
    public boolean register(String fallbackPrefix, Command command) {
        notifyObservers(command);
        return commandMap.register(fallbackPrefix, command);
    }
    
    
    @Override
    public boolean register(String label, String fallbackPrefix, Command command) {
        notifyObservers(command);
        return commandMap.register(label, fallbackPrefix, command);
    }
    
    
    @Override
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        commandMap.registerAll(fallbackPrefix, commands);
    }
    
    
    public void unregister(String name) {
        Command command = commandMap.getCommand(name);
        
        if (command != null) {
            command.unregister(commandMap);
        }
    }
    
    @Override
    public void clearCommands() {
        commandMap.clearCommands();
    }
    

    @Override
    public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException {
        return commandMap.dispatch(sender, cmdLine);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String cmdLine) throws IllegalArgumentException {
        return commandMap.tabComplete(sender, cmdLine);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) throws IllegalArgumentException {
        return commandMap.tabComplete(sender, cmdLine, location);
    }

    
    public CommandMap getRealCommandMap() {
        return commandMap;
    }
    
}
