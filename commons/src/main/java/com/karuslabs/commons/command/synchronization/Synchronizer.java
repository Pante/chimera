/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.synchronization;

import com.karuslabs.commons.command.tree.TreeWalker;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.lang.ref.WeakReference;
import java.util.Collection;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.*;

import static java.util.stream.Collectors.toSet;


public class Synchronizer implements Listener {
    
    private MinecraftServer server;
    private Plugin plugin;
    CommandDispatcher<CommandListenerWrapper> dispatcher; 
    TreeWalker<CommandListenerWrapper, ICompletionProvider> walker;
    WeakReference<Synchronization> synchronization;
    
    
    public static Synchronizer of(Plugin plugin) {
        var server = ((CraftServer) plugin.getServer());
        var tree = new TreeWalker<CommandListenerWrapper, ICompletionProvider>(SynchronizationMapper.MAPPER);
        var registration = plugin.getServer().getServicesManager().getRegistration(Synchronization.class);
        
        var synchronizer = new Synchronizer(server.getServer(), plugin, tree, registration == null ? null : registration.getProvider());
        server.getPluginManager().registerEvents(synchronizer, plugin);
        
        return synchronizer;
    }
    
    
    Synchronizer(MinecraftServer server, Plugin plugin, TreeWalker<CommandListenerWrapper, ICompletionProvider> walker, Synchronization synchronization) {
        this.server = server;
        this.plugin = plugin;
        this.dispatcher = server.commandDispatcher.a();
        this.walker = walker;
        this.synchronization = new WeakReference<>(synchronization);
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
        
        walker.add(root, dispatcher.getRoot().getChildren(), entity.getCommandListener(), command -> commands.contains(command.getName()));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    @EventHandler
    protected void synchronize(PlayerCommandSendEvent event) {
        if (event instanceof SynchronizationEvent) {
            return;
        }
        
        var task = synchronization.get();
        if (task == null) {
            task = new Synchronization(this, plugin.getServer().getScheduler(), plugin);
            synchronization = new WeakReference<>(task);
            plugin.getServer().getServicesManager().register(Synchronization.class, task, plugin, ServicePriority.Low);
        }
        
        task.add(event);
    }
    
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
    }
    
}
