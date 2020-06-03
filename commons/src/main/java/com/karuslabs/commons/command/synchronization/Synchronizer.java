/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import java.util.Collection;

import net.minecraft.server.v1_15_R1.*;

import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.*;

import static java.util.stream.Collectors.toSet;


/**
 * A {@code Synchronizer} that facilities synchronization between the server's internal
 * dispatcher and a client.
 */
public class Synchronizer implements Listener {
    
    private MinecraftServer server;
    private Plugin plugin;
    CommandDispatcher<CommandListenerWrapper> dispatcher; 
    TreeWalker<CommandListenerWrapper, ICompletionProvider> walker;
    
    
    /**
     * Creates a {@code Synchronizer} for the given plugin.
     * 
     * @param plugin the owning plugin
     * @return a {@code Synchronizer}
     */
    public static Synchronizer of(Plugin plugin) {
        var server = ((CraftServer) plugin.getServer());
        var tree = new TreeWalker<CommandListenerWrapper, ICompletionProvider>(SynchronizationMapper.MAPPER);
        
        var synchronizer = new Synchronizer(server.getServer(), plugin, tree);
        server.getPluginManager().registerEvents(synchronizer, plugin);
        
        return synchronizer;
    }
    
    
    /**
     * Creates a {@code Synchronizer} with the given parameters.
     * 
     * @param server the server
     * @param plugin the owning plugin
     * @param walker the walker used to map {@code CommandNode<CommandListenerWrapper>}s
     *               to {@code CommandNode<ICompletionProivder>}s
     */
    Synchronizer(MinecraftServer server, Plugin plugin, TreeWalker<CommandListenerWrapper, ICompletionProvider> walker) {
        this.server = server;
        this.plugin = plugin;
        this.dispatcher = server.commandDispatcher.a();
        this.walker = walker;
    }
    
    
    /**
     * Synchronizes the internal dispatcher of the server with all currently online 
     * players. Commands are ignored if a player is not permitted to use them.
     */
    public void synchronize() {
        for (var player : server.server.getOnlinePlayers()) {
            synchronize(player);
        }
    }
    
    /**
     * Synchronizes the server's internal dispatcher with the given {@code player}.
     * Commands are ignored if the given player is not permitted to use them.
     * 
     * @param player the player
     */
    public void synchronize(Player player) {
        var permitted = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(toSet());       
        server.server.getPluginManager().callEvent(new SynchronizationEvent(player, permitted));
        
        synchronize(player, permitted);
    }
    
    /**
     * Synchronizes the given {@code commands} in the server's internal dispatcher
     * with the given {@code player}.
     * 
     * @param player the player
     * @param commands the names of the commands
     */
    public void synchronize(Player player, Collection<String> commands) {    
        var entity = ((CraftPlayer) player).getHandle();
        var root = new RootCommandNode<ICompletionProvider>();
        
        walker.add(root, dispatcher.getRoot().getChildren(), entity.getCommandListener(), command -> commands.contains(command.getName()));
        
        entity.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    /**
     * Reloads this {@code Synchronizer}'s dispatcher each time the server is
     * reloaded.
     * 
     * @param event the event
     */
    @EventHandler
    void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
    }
    
}
