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

import com.karuslabs.commons.command.parsers.*;
import com.karuslabs.commons.command.synchronization.Synchronizer;
import com.karuslabs.commons.command.tree.Tree;
import com.karuslabs.commons.command.tree.nodes.Literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.*;
import com.mojang.brigadier.tree.*;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {    
    
    private MinecraftServer server;
    private CommandDispatcher<CommandListenerWrapper> dispatcher;
    private Synchronizer synchronizer;
    private Parser parser;
    private Tree<CommandSender, CommandListenerWrapper> tree;
    
    
    public static Dispatcher of(Plugin plugin) {
        return of(plugin, new CachedParser());
    }
    
    public static Dispatcher of(Plugin plugin, Parser parser) {
        var server = ((CraftServer) plugin.getServer());
        var root = new Root(plugin, server.getCommandMap());
        var synchronizer = Synchronizer.of(plugin);
        
        var dispatcher = new Dispatcher(server, root, synchronizer, parser);
        root.set(dispatcher);
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, Synchronizer synchronizer, Parser parser) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.synchronizer = synchronizer;
        this.parser = parser;
        this.tree = new Tree<>(new DispatcherMapper(this));
    }
    
    
    public Literal<CommandSender> register(Literal.Builder<CommandSender> command) {
        var literal = command.build();
        getRoot().addChild(literal);
        return literal;
    }
    
    
    @Override
    public ParseResults<CommandSender> parse(StringReader command, CommandSender source) {
        return parser.accept(this, command, source);
    }
    
    public ParseResults<CommandSender> defaultParse(StringReader command, CommandSender source) {
        return super.parse(command, source);
    }

    
    @EventHandler
    protected void map(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        tree.replace(getRoot(), dispatcher.getRoot());
    }  
        
    
    public void update() {
        tree.replace(getRoot(), dispatcher.getRoot());
        synchronizer.synchronize();
    }
    
    
    public Synchronizer synchronizer() {
        return synchronizer;
    }
    
}
