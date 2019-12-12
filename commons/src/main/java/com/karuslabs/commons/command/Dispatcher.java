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

import com.karuslabs.commons.command.tree.nodes.Root;
import com.karuslabs.commons.command.synchronization.Synchronizer;
import com.karuslabs.commons.command.tree.Tree;
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


/**
 * A {@code CommandDispatcher} subclass that facilities the registration of commands
 * between a Spigot plugin and the server.
 * <br><br>
 * This dispatcher will <b>not</b> automatically synchronize newly registered commands 
 * except once when the server is started or reloaded to minimize unnecessary synchronizations. 
 * All otherwise registered commands must be manually synchronized via {@link #update()}.
 * <br><br>
 * <b>Implementation details:</b><br>
 * This dispatcher holds a reference to the internal dispatcher of the server. The 
 * internal dispatcher is flushed after all plugins are enabled when the server 
 * is either started or reloaded. In both cases, this dispatcher and the internal 
 * dispatcher are synchronized automatically. After synchronization between this 
 * dispatcher and the internal dispatcher, the internal dispatcher and clients are 
 * then also automatically synchronized via a {@link Synchronizer}.
 */
public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {    
    
    private MinecraftServer server;
    CommandDispatcher<CommandListenerWrapper> dispatcher;
    Synchronizer synchronizer;
    Tree<CommandSender, CommandListenerWrapper> tree;

    
    /**
     * Creates a {@code Dispatcher} for the given plugin.
     * 
     * @param plugin the plugin
     * @return a {@code Dispatcher}
     */
    public static Dispatcher of(Plugin plugin) {
        var server = ((CraftServer) plugin.getServer());
        var root = new Root(plugin, server.getCommandMap());
        var synchronizer = Synchronizer.of(plugin);
        
        var dispatcher = new Dispatcher(server, root, synchronizer);
        root.dispatcher(dispatcher);
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    /**
     * Creates a {@code Dispatcher} with the given parameters.
     * 
     * @see #of(Plugin) 
     * 
     * @param server the server
     * @param root the root for this dispatcher
     * @param synchronizer the synchronizer
     */
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, Synchronizer synchronizer) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.synchronizer = synchronizer;
        this.tree = new Tree<>(new DispatcherMapper(this));
    }
    
    
    /**
     * Registers the command built using the given {@code builder}.
     * 
     * @param command the builder used to build the command to be registered
     * @return the built command that was registered
     */
    public Literal<CommandSender> register(Literal.Builder<CommandSender> command) {
        var literal = command.build();
        getRoot().addChild(literal);
        return literal;
    }
      
    
    /**
     * Synchronizes this dispatcher with the server and clients.
     */
    public void update() {
        tree.truncate(getRoot(), dispatcher.getRoot());
        synchronizer.synchronize();
    }
    
    
    /**
     * Synchronizes this dispatcher with the server when the server is started or
     * reloaded.
     * 
     * @param event the event that denotes the starting and reloading of the server
     */
    @EventHandler
    protected void update(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        tree.truncate(getRoot(), dispatcher.getRoot());
    }
 
    
    /**
     * Returns a {@code Synchronizer}.
     * 
     * @return a {@code Synchronizer}
     */
    public Synchronizer synchronizer() {
        return synchronizer;
    }
    
}
