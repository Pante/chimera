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
import com.mojang.brigadier.tree.*;

import java.util.*;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;


public class Dispatcher extends CommandDispatcher<CommandSender> {
    
    private CraftServer server;
    
    
    public void update() {
        for (var player : server.getOnlinePlayers()) {
            update(player);
        }
    }
    
    public void update(Player player) {
        RootCommandNode<ICompletionProvider> root = new RootCommandNode<>();
        Trees.map(getRoot(), root, player);
        
        var commands = new HashSet<String>(root.getChildren().size());
        for (var command : root.getChildren()) {
            commands.add(command.getName());
        }

        var event = new PlayerCommandSendEvent(player, new HashSet<>(commands));
        server.getPluginManager().callEvent(event);
        
        commands.removeAll(event.getCommands());
        for (var command : commands) {
            root.removeCommand(command);
        }
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCommands(root));
    }
    
}
