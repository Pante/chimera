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

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;


/**
 * A {@code CommandDispatcher} subclass that facilities the registration of commands
 * between a Spigot plugin and the server.
 * <br><br>
 * <b>Implementation details:</b> This dispatcher holds a reference to the dispatcher,
 * s-dispatcher, that is used internally by the server. The s-dispatcher is flushed 
 * each time the server is restarted or reloaded. In both cases, the flush occurs 
 * after all plugins are enabled. This dispatcher and s-dispatcher are synchronized 
 * when a flush occurs via a {@link com.karuslabs.commons.command.synchronization.Synchronizer}.
 * 
 * To avoid a performance penalty from unnecessary synchronizations, commands registered
 * to this dispatcher are not immediately synchronized. The changes to this dispatcher
 * will be reflected only after all plugins are enabled during the server start-up 
 * and reload, or when {@link #update()} is called.
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
     * @return a Dispatcher
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
     * Constructs a {@code Dispatcher} with the given server, root and synchronizer.
     * 
     * @see #of(Plugin) 
     * 
     * @param server the server
     * @param root the root
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
     * Registers the command built using the given builder. Synchronization of
     * registered commands will occur after all plugins are enabled during the server 
     * start-up and reload, or when {@link #update()} is called.
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
     * Synchronizes this dispatcher with the server.
     */
    public void update() {
        tree.truncate(getRoot(), dispatcher.getRoot());
        synchronizer.synchronize();
    }
    
    
    /**
     * Synchronizes this dispatcher with the server when a {@code ServerLoadEvent}
     * occurs.
     * 
     * @param event an event that denotes either a server reload or restart
     */
    @EventHandler
    protected void update(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        tree.truncate(getRoot(), dispatcher.getRoot());
    }
 
    
    /**
     * Returns a {@code Synchronizer}.
     * 
     * @return a synchronizer
     */
    public Synchronizer synchronizer() {
        return synchronizer;
    }
    
}
