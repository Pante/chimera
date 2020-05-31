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

import com.karuslabs.commons.command.tree.nodes.Aliasable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.*;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.bukkit.craftbukkit.v1_15_R1.command.CraftCommandMap;

import org.checkerframework.checker.nullness.qual.Nullable;


class SpigotMap implements PlatformMap {
    
    String prefix;
    Plugin plugin;
    CraftCommandMap map;
    @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    
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
    
    
    @Override
    public @Nullable DispatcherCommand unregister(String name) {
        var commands = map.getKnownCommands();
        var command = commands.get(name);
        if (!(command instanceof DispatcherCommand)) {
            return null;
        }
        
        commands.remove(name, command);
        commands.remove(prefix + ":" + name, command);
        
        for (var alias : command.getAliases()) {
            commands.remove(alias, command);
            commands.remove(prefix + ":" + alias, command);
        }
        
        command.unregister(map);
        
        return (DispatcherCommand) command;
    }
    
    
    DispatcherCommand wrap(LiteralCommandNode<CommandSender> command) {
        var aliases = new ArrayList<String>();
        if (command instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<?>) command).aliases()) {
                aliases.add(alias.getName());
            }
        }
        
        return new DispatcherCommand(command.getName(), plugin, dispatcher, command.getUsageText(), aliases);
    }
    
}
