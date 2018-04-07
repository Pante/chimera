/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.providers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.plugin.Plugin;

import static java.util.stream.Collectors.toList;


public class NamespaceProvider implements Resolver {
    
    private Plugin plugin;
    
    
    public NamespaceProvider(Plugin plugin) {
        this.plugin = plugin;
    }
    
    
    @Override
    public List<Command> resolve(ProxiedCommandMap map, CommandExecutor executor) {
        return Stream.of(executor.getClass().getAnnotationsByType(Namespace.class)).map(namespace -> resolve(map , executor, namespace)).collect(toList());
    } 
    
    protected Command resolve(ProxiedCommandMap map, CommandExecutor executor, Namespace namespace) {
        String[] names = namespace.value();
        if (names.length == 0) {
            throw new IllegalArgumentException("Invalid namespace for: " + executor.getClass().getName() + ", namespace cannot be empty");
        }
        
        Command command = map.getCommand(names[0]);
        if (command == null) {
            map.register(names[0], command = new Command(names[0], plugin));
        }
        
        for (int i = 1; i < names.length; i++) {
            command = append(command, names[i]);
        }
        
        return command;
    }
    
    protected Command append(Command parent, String name) {
        Command command = parent.getSubcommands().get(name);
        if (command == null) {
            parent.getSubcommands().put(name, command = new Command(name, plugin));
        }
        
        return command;
    } 

    
    @Override
    public boolean isResolvable(CommandExecutor executor) {
        return executor.getClass().getAnnotationsByType(Namespace.class).length != 0;
    }
    
}
