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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.tree.*;

import java.util.Map;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    private CraftServer server;
    
    
    protected Dispatcher(Server server) {
        this.server = ((CraftServer) server);
    }
    
    
    public void update() {
        var dispatcher = server.getServer().commandDispatcher;
        for (var player : server.getHandle().players) {
            dispatcher.a(player);
        }
    }
    
    public void rebuild() {
        
    }
                
    void rebuild(CommandNode<CommandSender> source, CommandNode<CommandListenerWrapper> target, Map<CommandNode<CommandSender>, CommandNode<CommandListenerWrapper>> cache) {
        for (var child : source.getChildren()) {
            var command = rebuild(child);
            rebuild(child, command, cache);
            target.addChild(command);
        }
        
        var redirected = source.getRedirect();
        if (redirected != null) {
            target.addChild(rebuild(redirected));
        }
    }
    
    CommandNode<CommandListenerWrapper> rebuild(CommandNode<CommandSender> command) {
        if (command instanceof ArgumentCommandNode) {
            var type = ((ArgumentCommandNode<CommandSender, Object>) command).getType();
            return RequiredArgumentBuilder.<CommandListenerWrapper, Object>argument(command.getName(), type).build();
            
        } else {
            return LiteralArgumentBuilder.<CommandListenerWrapper>literal(command.getName()).build();
        }
    }

}
