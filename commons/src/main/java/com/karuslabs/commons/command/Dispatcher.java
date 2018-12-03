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

import com.karuslabs.commons.command.tree.Trees;
import com.karuslabs.commons.command.tree.Trees.Functor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.util.Queue;
import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.tree.Trees.Functor.TRUE;


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
        
    
    public static Dispatcher of(Plugin plugin) {
        var server = plugin.getServer();
        
        var root = new Root(plugin, ((CraftServer) server).getCommandMap());
        var dispatcher = new Dispatcher(server, root);
        root.set(dispatcher);
        
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
    }
    
    
    public void synchronize() {
        update();
        for (var player : server.getPlayerList().players) {
            synchronize(player);
        }
    }
    
    public void synchronize(Player player) {
        synchronize(((CraftPlayer) player).getHandle());
    }

    void update() {
        var target = dispatcher.getRoot();
        
        for (var child : getRoot().getChildren()) {
            Commands.remove(target, child.getName());
            
            var mapped = FUNCTOR.map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
    void synchronize(EntityPlayer player) {
        var root = new RootCommandNode<ICompletionProvider>();
        Trees.map(dispatcher.getRoot(), root, player.getCommandListener());
        
        player.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        synchronize();
    }
    
    @EventHandler
    protected void synchronize(PlayerJoinEvent event) {
        synchronize(event.getPlayer());
    }
    
    
    public static class Task implements Runnable {
        
        private Queue<Player> players;
        private boolean running;
        
        
        public Task(Queue<Player> players) {
            this.players = players;
            this.running = false;
        } 
        
        
        
        @Override
        public void run() {
            Player player;
            do {
                
            } while (player)
        }
        
    }
    
}
