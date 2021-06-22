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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.*;

import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.vehicle.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.command.*;
import org.bukkit.craftbukkit.v1_17_R1.entity.*;

import org.bukkit.command.*;

/**
 * Utility methods that handle exceptions which occur when parsing and executing commands.
 * <br><br>
 * <b>Implementation details:</b><br>
 * This class was adapted from Spigot's {@code CommandDispatcher}. Each method report 
 * method represents a catch clause handling a specific exception.
 */
@Static class Exceptions {

    static final Logger LOGGER = LogManager.getLogger(Commands.class);
    
    // Source: net.minecraft.commands.CommandDispatcher #line: 276
    static void report(CommandSender sender, CommandRuntimeException exception) {
        from(sender).sendFailure(exception.getComponent());
    }
    
    // Source: net.minecraft.commands.CommandDispatcher #line: 280
    static void report(CommandSender sender, CommandSyntaxException exception) {
        var stack = from(sender);
        
        stack.sendFailure(ComponentUtils.fromMessage(exception.getRawMessage()));
        
        var input = exception.getInput();
        if (input != null && exception.getCursor() >= 0) {
            var index = Math.min(input.length(), exception.getCursor());

            var text = new TextComponent("").withStyle(ChatFormatting.GRAY).withStyle(style -> 
                style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, input))
            );
            
            if (index > 20) {
                text.append("...");
            }

            text.append(input.substring(Math.max(0, index - 20), index));
            
            if (index < input.length()) {
                var error = new TextComponent(input.substring(index)).withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.UNDERLINE});
                text.append(error);
            }
            
            var context = new TranslatableComponent("command.context.here").withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.ITALIC});
            text.append(context);
            
            stack.sendFailure(text);
        }
    }
    
    
    // Source: net.minecraft.server.CommandDispatcher #line: 305
    static void report(CommandSender sender, String command, Exception exception) {
        var stack = from(sender);
        
        var message = exception.getMessage();
        var details = new TextComponent(message == null ? exception.getClass().getName() : message);        
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("Command exception: {}", command, exception);
            var stacktrace = exception.getStackTrace();

            for (int i = 0; i < Math.min(stacktrace.length, 3); ++i) {
                details.append("\n\n").append(stacktrace[i].getMethodName())
                       .append("\n ").append(stacktrace[i].getFileName()).append(":").append(String.valueOf(stacktrace[i].getLineNumber()));
            }
        }

        stack.sendFailure((new TranslatableComponent("command.failed")).withStyle(style -> 
            style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, details))
        ));

        if (SharedConstants.IS_RUNNING_IN_IDE) {
            stack.sendFailure(new TextComponent(Util.describeError(exception)));
            LOGGER.error("'{}' threw an exception", command, exception);
        }
    }
    
    
    
    
    // Source: package org.bukkit.craftbukkit.command.VanillaCommandWrapper#getListener(CommandSender)
    // We wrote it slightly to check agaisnt NMS types rather than Spigot types to make it safer.
    static CommandSourceStack from(CommandSender sender) {
        if (sender instanceof CraftPlayer player) {
            return player.getHandle().createCommandSourceStack();
            
        } else if (sender instanceof CraftBlockCommandSender block) {
            return block.getWrapper();
            
        } else if (sender instanceof CraftMinecartCommand minecart) {
            return ((MinecartCommandBlock) minecart.getHandle()).getCommandBlock().createCommandSourceStack();
            
        } else if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).rconConsoleSource.createCommandSourceStack();
            
        } else if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().createCommandSourceStack();
            
        } else if (sender instanceof ProxiedNativeCommandSender proxied) {
            return proxied.getHandle();
            
        } else {
            throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
        }
    }
    
}
