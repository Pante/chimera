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

import com.karuslabs.commons.command.caches.ResultCache;
import com.karuslabs.commons.command.event.*;
import com.karuslabs.commons.command.tree.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.*;
import com.mojang.brigadier.tree.*;

import java.util.*;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.stream.Collectors.toSet;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {    
    
    private MinecraftServer server;
    private CommandDispatcher<CommandListenerWrapper> dispatcher; 
    private SynchronizationTask task;
    private Trees.Builder<CommandSender, CommandListenerWrapper> builder;
    private ResultCache cache;

    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, SynchronizationTask task, ResultCache cache) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.task = task;
        this.builder = new CommandSenderBuilder(this);
        this.cache = cache;
    }
    
    
    public Literal<CommandSender> register(Literal.Builder<CommandSender> command) {
        var literal = command.build();
        getRoot().addChild(literal);
        return literal;
    }
    
    
    public void update() {
        map();
        synchronize();
    }  
        
    protected void map() {
        var target = dispatcher.getRoot();
        
        for (var child : getRoot().getChildren()) {
            Commands.remove(target, child.getName());
            
            var mapped = builder.map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
    
    @Override
    public ParseResults<CommandSender> parse(StringReader command, CommandSender source) {
        var results = cache.get(command.getString());
        if (results == null) {
            results = super.parse(command, source);
            cache.put(command.getString(), results);
        }
        
        return results;
    }
    
    
    public void synchronize() {
        for (var player : server.server.getOnlinePlayers()) {
            synchronize(player);
        }
    }
    
    public void synchronize(Player player) {
        var permitted = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(toSet());       
        server.server.getPluginManager().callEvent(new SynchronizationEvent(player, permitted));
        
        synchronize(player, permitted);
    }
    
    public void synchronize(Player player, Collection<String> commands) {    
        var entity = ((CraftPlayer) player).getHandle();
        var root = new RootCommandNode<ICompletionProvider>();
        
        Trees.map(dispatcher.getRoot(), root, entity.getCommandListener(), CommandListenerBuilder.BUILDER, command -> commands.contains(command.getName()));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    @EventHandler
    protected void update(PlayerCommandSendEvent event) {
        if (!(event instanceof SynchronizationEvent)) {
            task.add(event);
        }
    }
    
    @EventHandler
    protected void map(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        map();
    }
    
    
    public ResultCache cache() {
        return cache;
    }    
        
            
    public static Builder of(Plugin plugin) {
        return new Builder(plugin);
    }
    
    public static class Builder  {
        
        private Plugin plugin;
        private ResultCache cache;
        private @Nullable Root root;
        private @Nullable SynchronizationTask task;

        
        protected Builder(Plugin plugin) {
            this.plugin = plugin;
            this.cache = ResultCache.NONE;
        }
        
        
        public Builder cache(ResultCache cache) {
            this.cache = cache;
            return this;
        }
        
        public Builder root(Root root) {
            this.root = root;
            return this;
        }
        
        public Builder task(SynchronizationTask task) {
            this.task = task;
            return this;
        }
        
        public Dispatcher build() {
            var server = plugin.getServer();
            if (root == null) {
                root = new Root(plugin, ((CraftServer) server).getCommandMap());
            }

            if (task == null) {
                task = new SynchronizationTask(server.getScheduler(), plugin, null);
            }

            var dispatcher = new Dispatcher(server, root, task, cache);

            root.set(dispatcher);
            task.set(dispatcher);

            server.getPluginManager().registerEvents(dispatcher, plugin);
            
            return dispatcher;
        }

    }
    
}
