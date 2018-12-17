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
import com.karuslabs.commons.command.tree.*;
import com.karuslabs.commons.command.tree.Trees.Functor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
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

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.tree.Trees.Functor.TRUE;
import static java.util.stream.Collectors.toSet;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    static final Functor<CommandListenerWrapper, ICompletionProvider> FUNCTOR = new Functor<>() {
        @Override
        protected SuggestionProvider<ICompletionProvider> suggestions(ArgumentCommandNode<CommandListenerWrapper, ?> command) {
            // Fucking nasty workaround using raw types which Mojang abused.
            // It only works because CommandListenerWrapper is the sole implementation of ICompleteionProvider.
            SuggestionProvider provider = command.getCustomSuggestions();
            return provider;
        } 
    };

    
    public static Dispatcher of(Plugin plugin) {
        var server = plugin.getServer();
        
        var root = new Root(plugin, ((CraftServer) server).getCommandMap());
        var task = new SynchronizationTask(server.getScheduler(), plugin, null);
        
        var dispatcher = new Dispatcher(server, root, task);
        
        root.set(dispatcher);
        task.set(dispatcher);
        
        server.getPluginManager().registerEvents(dispatcher, plugin);
        
        return dispatcher;
    }
    
    
    private MinecraftServer server;
    private CommandDispatcher<CommandListenerWrapper> dispatcher; 
    private SynchronizationTask task;
    private Functor<CommandSender, CommandListenerWrapper> functor;

    
    protected Dispatcher(Server server, RootCommandNode<CommandSender> root, SynchronizationTask task) {
        super(root);
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
        this.task = task;
        this.functor = new ReparsingFunctor(this);
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
            
            var mapped = functor.map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
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
        
        Trees.map(dispatcher.getRoot(), root, entity.getCommandListener(), FUNCTOR, command -> commands.contains(command.getName()));
        
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
    
}

class ReparsingFunctor extends Functor<CommandSender, CommandListenerWrapper> {
    
    private CommandDispatcher<CommandSender> dispatcher;
    
    
    ReparsingFunctor(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }
    

    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
    }

    @Override
    protected @Nullable SuggestionProvider<CommandListenerWrapper> suggestions(ArgumentCommandNode<CommandSender, ?> command) {
        var suggestor = command.getCustomSuggestions();
        if (suggestor == null) {
            return null;
        }

        return (context, suggestions) -> {
            var reparsed = dispatcher.parse(context.getInput(), context.getSource().getBukkitSender()).getContext().build(context.getInput());
            return suggestor.getSuggestions(reparsed, suggestions);
        };
    }

}
