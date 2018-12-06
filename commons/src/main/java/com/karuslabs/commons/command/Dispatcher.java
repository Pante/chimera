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

import com.karuslabs.commons.command.event.*;
import com.karuslabs.commons.command.tree.Trees;
import com.karuslabs.commons.command.tree.Trees.Functor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

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

import static com.karuslabs.commons.command.tree.Trees.Functor.TRUE;
import static java.util.stream.Collectors.toList;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    static final Functor<CommandSender, CommandListenerWrapper> FUNCTOR = new Functor<>() {
        @Override
        protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
            var requirement = command.getRequirement();
            return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
        }
    };
    
    
    private MinecraftServer server;
    private CommandDispatcher<CommandListenerWrapper> dispatcher; 
    private SynchronizationTask task;
        
    
    public static Dispatcher of(Plugin plugin) {
        var server = plugin.getServer();
        
        var root = new Root(plugin, ((CraftServer) server).getCommandMap());
        var task = new SynchronizationTask(server.getScheduler(), plugin);
        
        var dispatcher = new Dispatcher(server, root, task);
        
        root.set(dispatcher);
        task.set(dispatcher);
        
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, SynchronizationTask task) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.task = task;
    }
    
    
    public Dispatcher synchronize() {
        var target = dispatcher.getRoot();
        
        for (var child : getRoot().getChildren()) {
            Commands.remove(target, child.getName());
            
            var mapped = FUNCTOR.map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
        
        return this;
    }
    
    
    public void update() {
        for (var player : server.server.getOnlinePlayers()) {
            update(player);
        }
    }
    
    public void update(Player player) {
        var entity = ((CraftPlayer) player).getHandle();
        var root = new RootCommandNode<ICompletionProvider>();
        
        Trees.map(dispatcher.getRoot(), root, entity.getCommandListener());
        
        var commands = root.getChildren().stream().map(CommandNode::getName).collect(toList());
        server.server.getPluginManager().callEvent(new SynchronizationEvent(player, commands));
        Commands.remove(root, commands.toArray(new String[]{}));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    @EventHandler
    protected void synchronize(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        synchronize();
    }
    
    @EventHandler
    protected void update(PlayerJoinEvent event) {
        update(event.getPlayer());
    }
    
    @EventHandler
    protected void update(PlayerCommandSendEvent event) {
        if (!(event instanceof SynchronizationEvent)) {
            task.add(event.getPlayer());
        }
    }
    
}
