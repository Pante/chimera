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


public class DispatcherMap {
    
    String prefix;
    Plugin plugin;
    CraftCommandMap map;
    @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    
    DispatcherMap(CraftCommandMap map) {
        this.map = map;
    }
    
    
    public @Nullable DispatcherCommand register(LiteralCommandNode<CommandSender> command) {
        if (map.getKnownCommands().containsKey(command.getName())) {
            return null;
        }
        
        var wrapped = wrap(command);
        map.register(prefix, wrapped);
        return wrapped;
    }
    
    public @Nullable DispatcherCommand unregister(String name) {
        
    }
    
    
    DispatcherCommand wrap(LiteralCommandNode<CommandSender> command) {
        var aliases = new ArrayList<String>();
        if (command instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<?>) command).aliases()) {
                aliases.add(alias.getName());
            }
        }
        
        return new DispatcherCommand(prefix, plugin, dispatcher, command.getUsageText(), aliases);
    }
    
    
    @Nullable CommandDispatcher<CommandSender> dispatcher() {
        return dispatcher;
    }
    
    void dispatcher(CommandDispatcher<CommandSender> dispatcher) {
        if (this.dispatcher == null) {
            this.dispatcher = dispatcher;
            
        } else {
            throw new IllegalStateException("CommandDispatcher is already initialized");
        }
    }
    
    
    public Map<String, Command> getKnownCommands() {
        return map.getKnownCommands();
    }
    
}
