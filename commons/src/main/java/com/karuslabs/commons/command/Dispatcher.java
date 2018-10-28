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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


public abstract class Dispatcher<T> implements Listener {
    
    protected Plugin plugin;
    private @Nullable CommandDispatcher<T> dispatcher;
    private List<CommandNode<T>> commands;

    
    protected Dispatcher(Plugin plugin) {
        this(plugin, new ArrayList<>());
    }
    
    protected Dispatcher(Plugin plugin, List<CommandNode<T>> commands) {
        this.plugin = plugin;
        this.commands = commands;
    }
    
    
    @EventHandler
    public void load(ServerLoadEvent event) {
        dispatcher = dispatcher();
        for (var command : commands) {
            dispatcher.getRoot().addChild(command);
        }
    }
    
    public abstract CommandDispatcher<T> dispatcher();
    
    
    public <Builder extends ArgumentBuilder<T, Builder>> Dispatcher<T> add(ArgumentBuilder<T, Builder> command) {
        return add(command.build());
    }
    
    public Dispatcher<T> add(CommandNode<T> command) {
        if (dispatcher != null) {
            dispatcher.getRoot().addChild(command);
        }
        commands.add(command);
        return this;
    }
    
}
