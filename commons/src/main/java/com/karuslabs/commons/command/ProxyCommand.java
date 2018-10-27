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

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.command.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;


public class ProxyCommand extends Command {
    
    private MinecraftServer server;
    private CommandNode<CommandListenerWrapper> command;
    
    
    public ProxyCommand(MinecraftServer server, CommandNode<CommandListenerWrapper> command, List<String> aliases, String description) {
        super(command.getName(), description, command.getUsageText(), aliases);
        this.server = server;
        this.command = command;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (testPermission(sender)) {
            var listener = listener(sender);
            server.commandDispatcher.a(listener, join(getName(), args), join(label, args));
        }
        
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {

        var listener = listener(sender);
        var parsed = server.commandDispatcher.a().parse(join(getName(), args), listener);

        var results = new ArrayList<String>();
        server.commandDispatcher.a().getCompletionSuggestions(parsed).thenAccept(suggestions -> {
            for (var suggestion : suggestions.getList()) {
                results.add(suggestion.getText());
            }
        });

        return results;
    }
    
    
    private CommandListenerWrapper listener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().getCommandListener();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock().getWrapper();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).remoteControlCommandListener.f();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().getServerCommandListener();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }
    
    private String join(String name, String[] args) {
        return "/" + name + ((args.length > 0) ? " " + String.join(" ", args) : "");
    }
    
}
