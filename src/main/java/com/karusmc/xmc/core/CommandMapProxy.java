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

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandMapProxy implements CommandMap {
    
    private SimpleCommandMap commandMap;
    private Set<CommandMapObserver> observers;
    
    
    public CommandMapProxy(Server server) {
        observers = new HashSet<>();
        
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            
            commandMap = (SimpleCommandMap) field.get(server);
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Server instance does not contain field: commandMap", e);
        }
        
    }

    
    @Override
    public Command getCommand(String name) {
        return commandMap.getCommand(name);
    }
    
    
    public <T extends Command> Map<String, T> getPluginCommands(String pluginName, Class<T> commandType) {
        return commandMap.getCommands().stream()
                .filter(command -> command instanceof PluginIdentifiableCommand 
                        && ((PluginIdentifiableCommand) command).getPlugin().getName().equals(pluginName) 
                        && commandType.isInstance(command))
                .collect(Collectors.toMap(Command::getName, command -> commandType.cast(command)));
    }
    
    
    @Override
    public boolean register(String fallbackPrefix, Command command) {
        observers.forEach(observer -> observer.register(command));
        return commandMap.register(fallbackPrefix, command);
    }
    
    
    @Override
    public boolean register(String label, String fallbackPrefix, Command command) {
        observers.forEach(observer -> observer.register(command));
        return commandMap.register(label, fallbackPrefix, command);
    }
    
    
    @Override
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        observers.forEach(observer -> observer.registerAll(commands));
        commandMap.registerAll(fallbackPrefix, commands);
    }
    
    
    public void registerAll(Map<String, XMCommand> commands) {
        observers.forEach(observer -> observer.registerAll(commands.values()));
        commands.values().forEach(command -> register(command.getPlugin().getName(), command));
    }
    
    
    public void unregister(String name) {
        Command command = commandMap.getCommand(name);
        
        if (command != null) {
            observers.forEach(observer -> observer.unregister(name));
            command.unregister(commandMap);
        }
    }
    
    @Override
    public void clearCommands() {
        observers.forEach(observer -> observer.unregisterAll());
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
    
    
    public Set<CommandMapObserver> getObservers() {
        return observers;
    }

    
    public CommandMap getUnderlyingCommandMap() {
        return commandMap;
    }
    
}
