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

import com.karuslabs.commons.command.tree.nodes.Root;
import com.karuslabs.commons.command.tree.TreeWalker;
import com.karuslabs.commons.command.tree.nodes.Literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import java.util.Map;

import net.minecraft.server.v1_16_R1.*;

import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.command.CraftCommandMap;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {    
    
    private MinecraftServer server;
    private Root root;
    CommandDispatcher<CommandListenerWrapper> dispatcher;
    TreeWalker<CommandSender, CommandListenerWrapper> walker;

    
    public static Dispatcher of(Plugin plugin) {
        var prefix = plugin.getName().toLowerCase();
        var server = ((CraftServer) plugin.getServer());
        
        var map = new SpigotMap(prefix, plugin, (CraftCommandMap) server.getCommandMap());
        var root = new Root(prefix, map);
        var dispatcher = new Dispatcher(server, root);
        map.dispatcher = dispatcher;
        
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    protected Dispatcher(Server server, Root root) {
        super(root);
        this.root = root;
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.getCommandDispatcher().a();
        this.walker = new TreeWalker<>(new SpigotMapper(this));
    }
    
    
    public void register(Map<String, CommandNode<CommandSender>> commands) {
        for (var command : commands.values()) {
            getRoot().addChild(command);
        }
    }
    
    
    public Literal<CommandSender> register(Literal.Builder<CommandSender> command) {
        var literal = command.build();
        getRoot().addChild(literal);
        return literal;
    }
      
    
    public void update() {
        walker.prune(dispatcher.getRoot(), getRoot().getChildren());
    }
    
    
    @EventHandler
    protected void update(ServerLoadEvent event) {
        dispatcher = server.getCommandDispatcher().a();
        walker.prune(dispatcher.getRoot(), getRoot().getChildren());
    }
    
    
    @Override
    public Root getRoot()  {
        return root;
    }
    
}
