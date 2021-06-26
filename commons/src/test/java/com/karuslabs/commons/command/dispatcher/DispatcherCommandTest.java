/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;

import net.minecraft.commands.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DispatcherCommandTest {
    
    CommandSourceStack stack = mock(CommandSourceStack.class);
    ServerPlayer player = when(mock(ServerPlayer.class).createCommandSourceStack()).thenReturn(stack).getMock();
    CommandSender sender = when(mock(CraftPlayer.class).getHandle()).thenReturn(player).getMock();
    
    Plugin plugin = mock(Plugin.class);
    CommandDispatcher<CommandSender> dispatcher = mock(CommandDispatcher.class);
    DispatcherCommand command = spy(new DispatcherCommand("name", plugin, "desc", dispatcher, "", List.of()));
    
    
    @Test
    void execute_testPermission() {
        doReturn(false).when(command).testPermission(sender);
        
        assertTrue(command.execute(sender, "command", "a", "b"));
        verifyNoMoreInteractions(dispatcher);
    }
    
    
    @Test
    void execute() throws CommandSyntaxException {
        doReturn(true).when(command).testPermission(sender);
        var captor = ArgumentCaptor.forClass(StringReader.class);
        
        assertTrue(command.execute(sender, "command", "a", "b"));
        verify(dispatcher).execute(captor.capture(), eq(sender));
        
        var reader = captor.getValue();
        
        assertEquals("/command a b", reader.getString());
        assertEquals("command a b", reader.getRemaining());
    }
    
    
    @Test
    void execute_CommandRuntimeException() throws CommandSyntaxException {
        doReturn(true).when(command).testPermission(sender);
        doThrow(new CommandRuntimeException(new TextComponent("message"))).when(dispatcher).execute(any(StringReader.class), any(CommandSender.class));
        
        assertTrue(command.execute(sender, "command", "a", "b"));
        verify(stack, times(1)).sendFailure(any());
    }
    
    
    @Test
    void execute_CommandSyntaxException() throws CommandSyntaxException {
        doReturn(true).when(command).testPermission(sender);
        doThrow(new CommandSyntaxException(null, new LiteralMessage("message"), "abc", 2)).when(dispatcher).execute(any(StringReader.class), any(CommandSender.class));
        
        assertTrue(command.execute(sender, "command", "a", "b"));
        verify(stack, times(2)).sendFailure(any());
    }
    
    
    @Test
    void execute_Exception() throws CommandSyntaxException {
        doReturn(true).when(command).testPermission(sender);
        doThrow(RuntimeException.class).when(dispatcher).execute(any(StringReader.class), any(CommandSender.class));
        
        assertTrue(command.execute(sender, "command", "a", "b"));
        verify(stack, times(1)).sendFailure(any());
    }
    
    
    @Test
    void getDescription() {
        assertEquals("desc", command.getDescription());
    }
    
    
    @Test
    void getPlugin() {
        assertSame(plugin, command.getPlugin());
    }

} 
