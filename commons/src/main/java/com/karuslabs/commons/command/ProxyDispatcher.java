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
import java.util.function.Supplier;

import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;


public class ProxyDispatcher<T> implements Listener {
    
    public static <T> ProxyDispatcher<T> of(Plugin plugin, Supplier<CommandDispatcher<T>> source) {
        var dispatcher = new ProxyDispatcher<>(source);
        plugin.getServer().getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    private Supplier<CommandDispatcher<T>> source;
    private CommandDispatcher<T> dispatcher;
    private List<CommandNode<T>> commands;

    
    protected ProxyDispatcher(Supplier<CommandDispatcher<T>> source) {
        this(source, source.get(), new ArrayList<>());
    }
    
    protected ProxyDispatcher(Supplier<CommandDispatcher<T>> source, CommandDispatcher<T> dispatcher, List<CommandNode<T>> commands) {
        this.source = source;
        this.dispatcher = dispatcher;
        this.commands = commands;
    }
    
    
    @EventHandler
    public void load(ServerLoadEvent event) {
        dispatcher = source.get();
        for (var command : commands) {
            dispatcher.getRoot().addChild(command);
        }
    }
    
    
    public <Builder extends ArgumentBuilder<T, Builder>> ProxyDispatcher<T> add(ArgumentBuilder<T, Builder> command) {
        return add(command.build());
    }
    
    public ProxyDispatcher<T> add(CommandNode<T> command) {
        dispatcher.getRoot().addChild(command);
        commands.add(command);
        return this;
    }
    
}
