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

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.*;

import static java.util.stream.Collectors.toSet;


/**
 * A {@code Synchronizer} that facilities synchronization between the internal
 * dispatcher of the server and a client.
 * <br><br>
 * <b>Implementation details:</b><br>
 * An issue in Minecraft's command mapping causes redirected commands to be incorrectly
 * mapped and sent to clients  if the name of the redirected command is lexicography 
 * less than the name of the destination. Hence, this {@code Synchronizer} remaps 
 * and resends the commands after a {@code PlayerCommanndSendEvent} is emitted.
 * <br><br>
 * A {@link Synchronization} is shared across plugins using the {@code ServiceManager}
 * to avoid duplicate remapping and resending. A {@code Sychronizer} would first
 * check if a {@code Synchronization} has been registered to the {@code ServiceManager}
 * and uses it if available. A {@code Synchronization} is otherwise created and 
 * shared until the owning plugin is unloaded.
 */
public class Synchronizer implements Listener {
    
    private MinecraftServer server;
    private Plugin plugin;
    CommandDispatcher<CommandListenerWrapper> dispatcher; 
    Tree<CommandListenerWrapper, ICompletionProvider> tree;
    WeakReference<Synchronization> synchronization;
    
    
    /**
     * Creates a {@code Synchronizer} for the given plugin.
     * 
     * @param plugin the owning plugin
     * @return a {@code Synchronizer}
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
     * Synchronizes the internal dispatcher of the server with all currently online 
     * players, ignoring commands that the given player is not permitted to use.
     */
    public void synchronize() {
        for (var player : server.server.getOnlinePlayers()) {
            synchronize(player);
        }
    }
    
    /**
     * Synchronizes the internal dispatcher of the server with the given {@code player},
     * ignoring commands that the {@code player} is not permitted to use.
     * 
     * @param player the player
     */
    public void synchronize(Player player) {
        var permitted = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(toSet());       
        server.server.getPluginManager().callEvent(new SynchronizationEvent(player, permitted));
        
        synchronize(player, permitted);
    }
    
    /**
     * Synchronizes the given {@code commands} in the internal dispatcher of the 
     * server with the given {@code player}.
     * 
     * @param player the player
     * @param commands the names of the commands
     */
    public void synchronize(Player player, Collection<String> commands) {    
        var entity = ((CraftPlayer) player).getHandle();
        var root = new RootCommandNode<ICompletionProvider>();
        
        tree.map(dispatcher.getRoot(), root, entity.getCommandListener(), command -> commands.contains(command.getName()));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    /**
     * Synchronizes the commands given by the {@code event} in the internal dispatcher
     * of the server with the player given by the {@code event} after the {@code event}
     * has been fully processed.
     * 
     * @param event the event which denotes a synchronization between the internal
     *              dispatcher of the server and a client
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
     * Sets the dispatcher when the server is started or reloaded.
     * 
     * @param event the event that denotes the starting and reloading of the server
     */
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
    }
    
}
