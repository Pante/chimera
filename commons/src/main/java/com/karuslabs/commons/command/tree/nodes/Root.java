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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.commons.command.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {code RootCommandNode} subclass that facilities the wrapping and registration
 * of {@code CommandNode} to the provided {@code CommandMap}.
 */
public class Root extends RootCommandNode<CommandSender> {
    
    private String prefix;
    private Plugin plugin;
    private CommandMap map;
    @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    
    /**
     * Constructs a {@code Root} with the given owning plugin name as the fallback
     * prefix, owning plugin and command map.
     * 
     * @param plugin the owning plugin
     * @param map the CommandMap
     */
    public Root(Plugin plugin, CommandMap map) {
        this(plugin.getName().toLowerCase(), plugin, map);
    }
    
    /**
     * Constructs a {@code Root} with the given fallback prefix, owning plugin and 
     * command map.
     * 
     * @param prefix the fallback prefix
     * @param plugin the owning plugin
     * @param map the CommandMap
     */
    public Root(String prefix, Plugin plugin, CommandMap map) {
        this.prefix = prefix;
        this.plugin = plugin;
        this.map = map;
    }
    
    
    /**
     * Adds the given command to this {@code Root} before wrapping and registering
     * the given command to the provided {@code CommandMap}.
     * 
     * @param command the command to be added
     */
    @Override
    public void addChild(CommandNode<CommandSender> command) {
        if (map.register(prefix, wrap(command))) {
            super.addChild(command);
        }
        
        super.addChild(Commands.alias(command, prefix + ":" + command.getName()));
    }
    
    /**
     * Wraps the given {@code CommandNode} in a Spigot {@code Command}.
     * 
     * @param command the command
     * @return the wrapped command
     */
    protected Command wrap(CommandNode<CommandSender> command) {
        return new DispatcherCommand(command.getName(), plugin, dispatcher, command.getUsageText());
    }
    
    
    /**
     * Returns the {@code CoommandDispatcher} that owns this {@code Root}.
     * 
     * @return the owning CommandDispatcher, or null if this root has no owning
     *         CommandDispatcher
     */
    public @Nullable CommandDispatcher<CommandSender> dispatcher() {
        return dispatcher;
    }
    
    /**
     * Sets the owning {@code CommandDispatcher} of this {@code Root}.
     * 
     * @param dispatcher the owning CommandDispatcher
     * @throws IllegalStateException if this root already has an owning CommandDispatcher
     */
    public void dispatcher(CommandDispatcher<CommandSender> dispatcher) {
        if (this.dispatcher == null) {
            this.dispatcher = dispatcher;
            
        } else {
            throw new IllegalStateException("CommandDispatcher is already initialized");
        }
    }
    
}
