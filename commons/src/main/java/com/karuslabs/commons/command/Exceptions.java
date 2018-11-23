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

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.command.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.*;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;


public @Static class Exceptions {
    
    private static final Object[] EMPTY = new Object[0];
    private static final String CONTEXT = "command.context.here";
    private static final String FAILURE = "command.failed";
    
    public static void report(CommandSender sender, CommandSyntaxException exception) {
        var listener = from(sender);
        
        listener.sendFailureMessage(ChatComponentUtils.a(exception.getRawMessage()));
        
        var input = exception.getInput();
        if (input != null && exception.getCursor() >= 0) {
            var index = Math.min(input.length(), exception.getCursor());

            var text = new ChatComponentText("");
            if (index > 10) {
                text.a("...");
            }

            text.a(input.substring(Math.max(0, index - 10), index));
            
            if (index < input.length()) {
                var error = new ChatComponentText(input.substring(index));
                error.getChatModifier().setColor(EnumChatFormat.RED);
                error.getChatModifier().setUnderline(true);
                
                text.addSibling(error);
            }

            var context = new ChatMessage(CONTEXT, EMPTY);
            context.getChatModifier().setItalic(true);
            context.getChatModifier().setColor(EnumChatFormat.RED);
            
            text.addSibling(context);
            text.getChatModifier().setColor(EnumChatFormat.GRAY);
            text.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, input));
            
            listener.sendFailureMessage(text);
        }
    }
    
    
    public static void report(CommandSender sender, Exception exception) {
        var listener = from(sender);
        
        var message = exception.getMessage();
        var details = new ChatComponentText(message == null ? exception.getClass().getName() : message);
        
        var stacktrace = exception.getStackTrace();
        for (int i = 0; i < Math.min(stacktrace.length, 3); i++) {
            details.a("\n\n" + stacktrace[i].getMethodName() + "\n " + stacktrace[i].getFileName() + ":" + stacktrace[i].getLineNumber());
        }
                
        var failure = new ChatMessage(FAILURE, EMPTY);
        failure.getChatModifier().setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, details));
        
        listener.sendFailureMessage(failure);
    }
    
    
    static CommandListenerWrapper from(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().getCommandListener();
            
        } else if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
            
        } else if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock().getWrapper();
            
        } else if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).remoteControlCommandListener.f();
        } else if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().getServerCommandListener();
            
        } else if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }
    
}
