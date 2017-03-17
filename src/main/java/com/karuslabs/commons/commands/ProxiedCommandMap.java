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

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;


public class ProxiedCommandMap {
    
    private SimpleCommandMap map;
    
    
    public ProxiedCommandMap(Server server) {
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (SimpleCommandMap) field.get(server);

        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new IllegalArgumentException("Server contains an incompatible CommandMap implementation", e);
        }
    }

    
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        commands.forEach(command -> register(fallbackPrefix, command));
    }

    @Proxied
    public boolean register(String label, String fallbackPrefix, Command command) {
        return map.register(label, fallbackPrefix, command);
    }

    @Proxied
    public boolean register(String fallbackPrefix, Command command) {
        return map.register(fallbackPrefix, command);
    }
    
    
    @Proxied
    public boolean dispatch(CommandSender sender, String cmdLine) {
        return map.dispatch(sender, cmdLine);
    }

    
    @Proxied
    public void clearCommands() {
        map.clearCommands();
    }

    
    public Command getCommand(String name) {
        return map.getCommand(name) instanceof Command ? (Command) map.getCommand(name) : null;
    }
    
    public Map<String, Command> getCommands(Plugin plugin) {
        return map.getCommands().stream()
                .filter(command -> command instanceof Command && ((Command) command).getPlugin().equals(plugin))
                .collect(Collectors.toMap(command -> command.getName(), command -> (Command) command));
    }
    
    
    @Proxied
    public List<String> tabComplete(CommandSender sender, String cmdLine)  {
        return map.tabComplete(sender, cmdLine);
    }

    @Proxied
    public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) {
        return map.tabComplete(sender, cmdLine, location);
    }
    
    
    public SimpleCommandMap getProxiedMap() {
        return map;
    }
    
}
