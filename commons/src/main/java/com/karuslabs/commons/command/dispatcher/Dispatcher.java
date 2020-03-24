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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.synchronization.Synchronizer;
import com.karuslabs.commons.command.tree.nodes.Root;
import com.karuslabs.commons.command.tree.TreeWalker;
import com.karuslabs.commons.command.tree.nodes.Literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import net.minecraft.server.v1_15_R1.*;

import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {    
    
    private MinecraftServer server;
    CommandDispatcher<CommandListenerWrapper> dispatcher;
    Synchronizer synchronizer;
    TreeWalker<CommandSender, CommandListenerWrapper> tree;

    
    public static Dispatcher of(Plugin plugin) {
        var server = ((CraftServer) plugin.getServer());
        var root = new Root(plugin, server.getCommandMap());
        var synchronizer = Synchronizer.of(plugin);
        
        var dispatcher = new Dispatcher(server, root, synchronizer);
        root.dispatcher(dispatcher);
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, Synchronizer synchronizer) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.synchronizer = synchronizer;
        this.tree = new TreeWalker<>(new NativeMapper(this));
    }
    
    
    public Literal<CommandSender> register(Literal.Builder<CommandSender> command) {
        var literal = command.build();
        getRoot().addChild(literal);
        return literal;
    }
      
    
    public void update() {
        tree.prune(dispatcher.getRoot(), getRoot().getChildren());
        synchronizer.synchronize();
    }
    
    
    @EventHandler
    protected void update(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        tree.prune(dispatcher.getRoot(), getRoot().getChildren());
    }
 
    
    public Synchronizer synchronizer() {
        return synchronizer;
    }
    
}
