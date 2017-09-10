/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.command;

import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static java.util.Collections.EMPTY_MAP;
import static java.util.stream.Collectors.toMap;


public class ProxiedCommandMap {
    
    private CommandMap map;
    
    
    public ProxiedCommandMap(Server server) {
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap) field.get(server);

        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new IllegalArgumentException("If you are reading this message, you're screwed.", e);
        }
    }
    
    
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        commands.forEach(command -> register(fallbackPrefix, command));
    }

    public boolean register(String label, String fallbackPrefix, Command command) {
        return map.register(label, fallbackPrefix, command);
    }

    public boolean register(String fallbackPrefix, Command command) {
        return map.register(fallbackPrefix, command);
    }
    
    
    public boolean dispatch(CommandSender sender, String cmdLine) {
        return map.dispatch(sender, cmdLine);
    }

    
    public void clearCommands() {
        map.clearCommands();
    }

    
    public @Nullable Command getCommand(String name) {
        return map.getCommand(name) instanceof Command ? (Command) map.getCommand(name) : null;
    }
    
    
    public Map<String, Command> getCommands(Plugin plugin) {
        if (map instanceof SimpleCommandMap) {
            SimpleCommandMap map = (SimpleCommandMap) this.map;
            return map.getCommands().stream()
                .filter(command -> command instanceof Command && ((Command) command).getPlugin().equals(plugin))
                .collect(toMap(command -> command.getName(), command -> (Command) command));
            
        } else {
            return EMPTY_MAP;
        }
    }
    
    
    public List<String> tabComplete(CommandSender sender, String cmdLine)  {
        return map.tabComplete(sender, cmdLine);
    }
    
    public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) {
        return map.tabComplete(sender, cmdLine, location);
    }
    
    
    public CommandMap getProxiedMap() {
        return map;
    }
    
}
