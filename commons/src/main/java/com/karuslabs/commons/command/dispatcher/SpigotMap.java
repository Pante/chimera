/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.*;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.bukkit.craftbukkit.v1_19_R1.command.CraftCommandMap;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A {@code PlatformMap} that wraps and registers {@code LiteralCommandNode}s
 * to Spigot's {@code CommandMap}.
 */
class SpigotMap implements PlatformMap {
    
    private final String prefix;
    private final Plugin plugin;
    private final CraftCommandMap map;
    @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    /**
     * Creates a {@code SpigotMap} with the given parameters.
     * 
     * @param prefix the prefix
     * @param plugin the owning plugin
     * @param map the map to which commands are registered
     */
    SpigotMap(String prefix, Plugin plugin, CraftCommandMap map) {
        this.prefix = prefix;
        this.plugin = plugin;
        this.map = map;
    }
    
    
    @Override
    public @Nullable DispatcherCommand register(LiteralCommandNode<CommandSender> command) {
        // We don't need to check if map contains "prefix:command_name" since Spigot will
        // always override it
        if (map.getKnownCommands().containsKey(command.getName())) {
            return null;
        }
        
        var wrapped = wrap(command);
        map.register(prefix, wrapped);
        return wrapped;
    }
    
    /**
     * Creates a {@code DispatcherCommand} that represents the given command.
     * 
     * @param command the command
     * @return a {@code DispatcherCommand} that represents {@code command}
     */
    DispatcherCommand wrap(LiteralCommandNode<CommandSender> command) {
        var aliases = new ArrayList<String>();
        if (command instanceof Aliasable<?> aliasable) {
            for (var alias : aliasable.aliases()) {
                aliases.add(alias.getName());
            }
        }
        
        var description = command instanceof Describable describable ? describable.description() : command.getUsageText();
        return new DispatcherCommand(command.getName(), plugin, description, dispatcher, command.getUsageText(), aliases);
    }
    
    
    @Override
    public @Nullable DispatcherCommand unregister(String name) {
        var commands = map.getKnownCommands();
        var command = commands.get(name);
        if (!(command instanceof DispatcherCommand wrapped)) {
            return null;
        }
        
        commands.remove(name, wrapped);
        commands.remove(prefix + ":" + name, wrapped);
        
        for (var alias : command.getAliases()) {
            commands.remove(alias, wrapped);
            commands.remove(prefix + ":" + alias, wrapped);
        }
        
        wrapped.unregister(map);
        
        return wrapped;
    }
    
}
