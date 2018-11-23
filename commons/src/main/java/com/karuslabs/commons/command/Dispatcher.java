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

import com.karuslabs.commons.command.tree.*;
import com.karuslabs.commons.command.tree.Trees.Visitor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    private static final Visitor<CommandSender, CommandListenerWrapper> LISTENER_VISITOR = new ListenerVisitor();
    private static final Visitor<CommandListenerWrapper, ICompletionProvider> COMPLETION_VISITOR = new CompletionVisitor();
    
    
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
        Trees.map(getRoot(), dispatcher.getRoot(), LISTENER_VISITOR);
    } 
        
    
    public void update() {
        for (var player : server.getPlayerList().players) {
            update(player);
        }
    }
    
    public void update(Player player) {
        update(((CraftPlayer) player).getHandle());
    }
    
    private void update(EntityPlayer player) {
        var root = new RootCommandNode<ICompletionProvider>();
        Trees.map(dispatcher.getRoot(), root, COMPLETION_VISITOR, player.getCommandListener());
        
        player.playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
    
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        synchronize();
        update();
    }
    
    @EventHandler
    protected void update(PlayerJoinEvent event) {
        update(event.getPlayer());
    }

        
    public static class Root extends RootCommandNode<CommandSender> {

        private Plugin plugin;
        private CommandMap map;
        private @Nullable CommandDispatcher<CommandSender> dispatcher;
        

        public Root(Plugin plugin, CommandMap map) {
            this.plugin = plugin;
            this.map = map;
        }

        
        @Override
        public void addChild(CommandNode<CommandSender> command) {
            super.addChild(command);
            map.register(plugin.getName(), new DispatcherCommand(command.getName(), plugin, dispatcher, command.getUsageText()));
        }
        
        
        protected void set(CommandDispatcher<CommandSender> dispatcher) {
            this.dispatcher = dispatcher;
        }

    }
    
    
    static class ListenerVisitor extends Visitor<CommandSender, CommandListenerWrapper> {
        
        @Override
        protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
            var requirement = command.getRequirement();
            return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
        }
        
    }
    
    static class CompletionVisitor extends Visitor<CommandListenerWrapper, ICompletionProvider> {
        
        @Override
        public @Nullable CommandNode<ICompletionProvider> map(CommandNode<CommandListenerWrapper> command, Map<CommandNode<CommandListenerWrapper>, CommandNode<ICompletionProvider>> commands, @Nullable CommandListenerWrapper sender) {
            if (command instanceof ArgumentCommandNode && ((ArgumentCommandNode<?, ?>) command).getCustomSuggestions() instanceof BukkitCommandWrapper) {
                return null;
                
            }
            
            return super.map(command, commands, sender);
        }
        
    }
    
}
