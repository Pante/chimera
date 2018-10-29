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
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.MinecraftServer;


import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


public abstract class Dispatcher<T> implements Listener {
    
    protected MinecraftServer server;
    protected Plugin plugin;
    private @Nullable CommandDispatcher<CommandListenerWrapper> dispatcher;

    
    protected Dispatcher(MinecraftServer server, Plugin plugin) {
        this.plugin = plugin;
        this.server = server;
        this.dispatcher = server.commandDispatcher.a();
    }

    
    public abstract CommandDispatcher<T> dispatcher();
    
    
    public <Builder extends ArgumentBuilder<CommandListenerWrapper, Builder>> Dispatcher<T> add(ArgumentBuilder<CommandListenerWrapper, Builder> command) {
        return add(command.build());
    }
    
    public Dispatcher<T> add(CommandNode<CommandListenerWrapper> command) {
        if (dispatcher != null) {
            dispatcher.getRoot().addChild(command);
        }
        return this;
    }
    
}
