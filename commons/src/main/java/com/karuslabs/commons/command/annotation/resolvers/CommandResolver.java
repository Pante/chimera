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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.Namespace;

import java.util.*;
import java.util.stream.Stream;

import org.bukkit.plugin.Plugin;


public class CommandResolver {
    
    Plugin plugin;
    Set<Resolver> resolvers;
    
    
    public CommandResolver(Plugin plugin, Set<Resolver> resolvers) {
        this.plugin = plugin;
        this.resolvers = resolvers;
    }
    
    
    public void resolve(ProxiedCommandMap map, CommandExecutor executor) {
        if (executor.getClass().getAnnotationsByType(Namespace.class).length > 0) {
            resolve(executor, find(map, executor));
            
        } else {
            throw new IllegalArgumentException("Unresolvable CommandExecutor: " + executor.getClass().getName());
        }
    }
    
    public void resolve(ProxiedCommandMap map, CommandExecutor executor, String... namespace) {
        resolve(executor, find(map, executor, namespace));
    }

    public void resolve(CommandExecutor executor, Command... commands) {
        for (Resolver resolver : resolvers) {
            if (resolver.isResolvable(executor)) {
                resolver.resolve(executor, commands);
            }
        }

        for (Command command : commands) {
            command.setExecutor(executor);
        }
    }
    
    
    protected Command[] find(ProxiedCommandMap map, CommandExecutor executor) {
        return Stream.of(executor.getClass().getAnnotationsByType(Namespace.class)).map(namespace -> find(map , executor, namespace)).toArray(Command[]::new);
    } 
    
    protected Command find(ProxiedCommandMap map, CommandExecutor executor, Namespace namespace) {
        String[] names = namespace.value();
        if (names.length > 0) {
            return find(map, executor, names);
            
        } else {
            throw new IllegalArgumentException("Invalid namespace for: " + executor.getClass().getName() + ", namespace cannot be empty");
        }
    }
    
    protected Command find(ProxiedCommandMap map, CommandExecutor executor, String[] names) {
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
    
    
    public static CommandResolver simple(Plugin plugin, References register) {
        Set<Resolver> resolvers = Set.of(new InformationResolver(), new LiteralResolver(), new RegisteredResolver(register), new ResourceResolver());
        return new CommandResolver(plugin, resolvers);
    }
    
}
