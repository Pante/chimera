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

import com.karuslabs.commons.command.tree.Tree;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.lang.ref.WeakReference;
import java.util.Collection;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.*;

import static java.util.stream.Collectors.toSet;


/**
 * A {@code Synchronizer} that facilities synchronization between the internal server
 * {@code CommandDispatcher} and a client.
 * <br><br>
 * <b>Implementation details:</b><br> 
 * Prior to synchronization between the internal server {@code CommandDispatcher} 
 * and a client, a {@link com.karuslabs.commons.command.Dispatcher} would synchronize 
 * itself with the internal server {@code CommandDispatcher}. Due to a fault in NMS's 
 * command mapping implementation, redirected commands are not mapped and sent to 
 * clients correctly under certain circumstances, hence this {@code Synchronizer} 
 * remaps and resends the commands each time after a {@code PlayerCommanndSendEvent} 
 * is emitted.
 * 
 * To avoid unnecessary remapping and resending across multiple plugins, a single
 * {@link Synchronization} task is shared between plugins. This is achieved via 
 * the {@code ServiceManager}. A plugin would first check if a synchronizer has 
 * been registered and reuses the synchronization task if available. Otherwise, 
 * a synchronizer is created and registered for shared use until the owning plugin 
 * is unloaded.
 */
public class Synchronizer implements Listener {
    
    private MinecraftServer server;
    private Plugin plugin;
    CommandDispatcher<CommandListenerWrapper> dispatcher; 
    Tree<CommandListenerWrapper, ICompletionProvider> tree;
    WeakReference<Synchronization> synchronization;
    
    
    /**
     * Returns a {@code Synchronizer} for the given plugin.
     * 
     * @param plugin the owning plugin
     * @return a synchronizer
     */
    public static Synchronizer of(Plugin plugin) {
        var server = ((CraftServer) plugin.getServer());
        var tree = new Tree<CommandListenerWrapper, ICompletionProvider>(SynchronizationMapper.MAPPER);
        var registration = plugin.getServer().getServicesManager().getRegistration(Synchronization.class);
        
        var synchronizer = new Synchronizer(server.getServer(), plugin, tree, registration == null ? null : registration.getProvider());
        server.getPluginManager().registerEvents(synchronizer, plugin);
        
        return synchronizer;
    }
    
    
    Synchronizer(MinecraftServer server, Plugin plugin, Tree<CommandListenerWrapper, ICompletionProvider> tree, Synchronization synchronization) {
        this.server = server;
        this.plugin = plugin;
        this.dispatcher = server.commandDispatcher.a();
        this.tree = tree;
        this.synchronization = new WeakReference<>(synchronization);
    }
    
    
    /**
     * Synchronizes the commands in the internal server {@code CommandDispatcher} 
     * that each player is permitted to use with all currently online players.
     */
    public void synchronize() {
        for (var player : server.server.getOnlinePlayers()) {
            synchronize(player);
        }
    }
    
    /**
     * Synchronizes the commands in the internal server {@code CommandDispatcher} 
     * that the given player is permitted to use with the player.
     * 
     * @param player the player
     */
    public void synchronize(Player player) {
        var permitted = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(toSet());       
        server.server.getPluginManager().callEvent(new SynchronizationEvent(player, permitted));
        
        synchronize(player, permitted);
    }
    
    /**
     * Synchronizes the given commands in the internal {@code CommandDispatcher}
     * with the given player.
     * 
     * @param player the player
     * @param commands the commands
     */
    public void synchronize(Player player, Collection<String> commands) {    
        var entity = ((CraftPlayer) player).getHandle();
        var root = new RootCommandNode<ICompletionProvider>();
        
        tree.map(dispatcher.getRoot(), root, entity.getCommandListener(), command -> commands.contains(command.getName()));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    /**
     * Synchronizes the commands in the internal {@code CommandDispatcher} returned 
     * by the given event with the player returned by the event after the given event
     * has been fully processed.
     * 
     * @param event the event
     */
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
    
    /**
     * Reloads the flushed internal server {@code CommandDispatcher} when the given
     * event is emitted.
     * 
     * @param event the event
     */
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
    }
    
}
