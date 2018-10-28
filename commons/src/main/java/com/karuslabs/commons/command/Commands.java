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

import com.karuslabs.commons.command.bridges.Bridge;

import com.mojang.brigadier.CommandDispatcher;

import java.util.List;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.MinecraftServer;

import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.plugin.Plugin;


public class Commands {
    
    private MinecraftServer server;
    private CommandMap map;
    private Dispatcher<CommandListenerWrapper> dispatcher;
    
    
    public Commands(Plugin plugin) {
        var craftserver = ((CraftServer) plugin.getServer());
        server = craftserver.getServer();
        map = craftserver.getCommandMap();
        dispatcher = new Dispatcher<>(plugin) {
            @Override
            public CommandDispatcher<CommandListenerWrapper> dispatcher() {
                return ((CraftServer) plugin.getServer()).getServer().commandDispatcher.a();
            };
        };
        craftserver.getPluginManager().registerEvents(dispatcher, plugin);
    }
    
    
    public void register(List<String> aliases, Bridge.Builder<?, ?, ?> builder) {
        var command = builder.unbridge().build();
        map.register(command.getName(), new ProxyCommand(server, command, aliases, "description"));
        dispatcher.add(command);
    }
    
}
