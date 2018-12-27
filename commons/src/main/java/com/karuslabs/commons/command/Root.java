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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.Commands;
import com.karuslabs.commons.command.DispatcherCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Root extends RootCommandNode<CommandSender> {
    
    private String prefix;
    private Plugin plugin;
    private CommandMap map;
    private @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    
    public Root(Plugin plugin, CommandMap map) {
        this(plugin.getName().toLowerCase(), plugin, map);
    }
    
    public Root(String prefix, Plugin plugin, CommandMap map) {
        this.prefix = prefix;
        this.plugin = plugin;
        this.map = map;
    }
    
    
    @Override
    public void addChild(CommandNode<CommandSender> command) {
        if (map.register(prefix, new DispatcherCommand(command.getName(), plugin, dispatcher, command.getUsageText()))) {
            super.addChild(command);
        }
        
        super.addChild(Commands.alias(command, prefix + ":" + command.getName()));
    }
    
    
    public void set(CommandDispatcher<CommandSender> dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
            
        } else {
            throw new IllegalStateException("Dispatcher is already initialized");
        }
    }
    
}
