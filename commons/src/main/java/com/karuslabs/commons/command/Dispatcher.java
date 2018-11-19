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

import com.karuslabs.commons.command.Trees.Factory;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;


public abstract class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    private static final Factory<CommandSender, CommandListenerWrapper> FACTORY = new RequirementFactory();
        
    
    private MinecraftServer server;
    private CommandDispatcher<CommandListenerWrapper> dispatcher;
    
    
    public Dispatcher(Server server) {
        this.server = ((CraftServer) server).getServer();
        this.dispatcher = this.server.commandDispatcher.a();
    }
    
    
    @EventHandler
    protected void load(ServerLoadEvent event) {
        dispatcher = server.commandDispatcher.a();
        synchronize();
        update();
    }
    
    @EventHandler
    protected void join(PlayerJoinEvent event) {
        update(event.getPlayer());
    }
    
    
    public void synchronize() {
        Trees.map(getRoot(), dispatcher.getRoot(), FACTORY);
    }
    
    
    public abstract void update();
    
    public abstract void update(Player player);
    
}


class RequirementFactory extends Factory<CommandSender, CommandListenerWrapper> {
    
    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : s -> requirement.test(s.getBukkitSender());
    }
    
}
