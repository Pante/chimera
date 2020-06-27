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

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.stream.Stream;

import net.minecraft.server.v1_16_R1.*;

import org.bukkit.command.*;

import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.command.*;
import org.bukkit.craftbukkit.v1_16_R1.entity.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ExceptionsTest {
    
    static final CommandListenerWrapper LISTENER = mock(CommandListenerWrapper.class);
    
    CommandListenerWrapper listener = mock(CommandListenerWrapper.class);
    EntityPlayer player = when(mock(EntityPlayer.class).getCommandListener()).thenReturn(listener).getMock();
    CommandSender sender = when(mock(CraftPlayer.class).getHandle()).thenReturn(player).getMock();
    
    
    @Test
    void report_command_syntax_exception() {
        Exceptions.report(sender, new CommandSyntaxException(null, new LiteralMessage("test"), "abc", 1));
        
        verify(listener, times(2)).sendFailureMessage(any(ChatComponentText.class));
    }
    
    
    @Test
    void report_exception() {
        Exceptions.report(sender, new IllegalArgumentException());
        verify(listener).sendFailureMessage(any(ChatMessage.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("senders")
    void from(CommandSender sender) {
        assertEquals(LISTENER, Exceptions.from(sender));
    }
    
    static Stream<CommandSender> senders() {
        EntityPlayer player = when(mock(EntityPlayer.class).getCommandListener()).thenReturn(LISTENER).getMock();
        CommandBlockListenerAbstract commandblock = when(mock(CommandBlockListenerAbstract.class).getWrapper()).thenReturn(LISTENER).getMock();
        EntityMinecartCommandBlock minecart = when(mock(EntityMinecartCommandBlock.class).getCommandBlock()).thenReturn(commandblock).getMock();
        
        DedicatedServer server = when(mock(DedicatedServer.class).getServerCommandListener()).thenReturn(LISTENER).getMock();
        CraftServer craftserver = when(mock(CraftServer.class).getServer()).thenReturn(server).getMock();
        
        return Stream.of(
            when(mock(CraftPlayer.class).getHandle()).thenReturn(player).getMock(),
            when(mock(CraftBlockCommandSender.class).getWrapper()).thenReturn(LISTENER).getMock(),
            when(mock(CraftMinecartCommand.class).getHandle()).thenReturn(minecart).getMock(),
            when(mock(ConsoleCommandSender.class).getServer()).thenReturn(craftserver).getMock(),
            when(mock(ProxiedNativeCommandSender.class).getHandle()).thenReturn(LISTENER).getMock()
        );
    }
    
    
    @Test
    void from_exception() {
        CommandSender sender = mock(CommandSender.class);
        assertEquals(
            "Cannot make " + sender + " a vanilla command listener",
            assertThrows(IllegalArgumentException.class, () -> Exceptions.from(sender)).getMessage()
        );
    }
    

} 
