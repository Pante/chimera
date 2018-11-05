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

import com.karuslabs.commons.command.internal.ProxyCommand;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;


public class Commands {
    
    private CraftServer server;
    private Plugin plugin;
    private CommandMap map;
    private Dispatcher dispatcher;
    
    
    public Commands(Plugin plugin) {
        this.server = ((CraftServer) plugin.getServer());
        this.plugin = plugin;
        this.map = server.getCommandMap();
        this.dispatcher = Dispatcher.of(plugin);
    }
    
    
    public <Builder extends ArgumentBuilder<?, Builder>> Commands add(Builder builder) {
        return add(builder.build());
    }    
    
    public <Builder extends ArgumentBuilder<?, Builder>> Commands add(String prefix, Builder builder) {
        return add(prefix, builder.build());
    }
    
    public Commands add(CommandNode<?> command) {
        return add(plugin.getName(), command);
    }
    
    public Commands add(String prefix, CommandNode<?> command) {
        map.register(prefix, new ProxyCommand(server.getServer(), plugin, command));
        dispatcher.add(command);
        return this;
    }
    
    
    public Dispatcher dispatcher() {
        return dispatcher;
    }
    
}
