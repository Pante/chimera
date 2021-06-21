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

import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;

import org.bukkit.command.*;

import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.command.*;
import org.bukkit.craftbukkit.v1_17_R1.entity.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionsTest {
    
    static final CommandSourceStack LISTENER = mock(CommandSourceStack.class);
    
    CommandSourceStack stack = mock(CommandSourceStack.class);
    ServerPlayer player = when(mock(ServerPlayer.class).createCommandSourceStack()).thenReturn(stack).getMock();
    CommandSender sender = when(mock(CraftPlayer.class).getHandle()).thenReturn(player).getMock();
    
    @Test
    void report_command_runtime_exception() {
        Exceptions.report(sender, new CommandRuntimeException(new TextComponent("message")));
        
        verify(stack, times(1)).sendFailure(any(TextComponent.class));
    }
    
    
    @Test
    void report_command_syntax_exception() {
        Exceptions.report(sender, new CommandSyntaxException(null, new LiteralMessage("test"), "abc", 1));
        
        verify(stack, times(2)).sendFailure(any(TextComponent.class));
    }
    
    
    @Test
    void report_exception() {
        Exceptions.report(sender, "/command", new IllegalArgumentException());
        verify(stack).sendFailure(any(TranslatableComponent.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("senders")
    void from(CommandSender sender) {
        assertEquals(LISTENER, Exceptions.from(sender));
    }
    
    static Stream<CommandSender> senders() {
        ServerPlayer player = when(mock(ServerPlayer.class).createCommandSourceStack()).thenReturn(LISTENER).getMock();
        BaseCommandBlock commandblock = when(mock(BaseCommandBlock.class).createCommandSourceStack()).thenReturn(LISTENER).getMock();
        MinecartCommandBlock minecart = when(mock(MinecartCommandBlock.class).getCommandBlock()).thenReturn(commandblock).getMock();
        
        DedicatedServer server = when(mock(DedicatedServer.class).createCommandSourceStack()).thenReturn(LISTENER).getMock();
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
