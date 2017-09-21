/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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

import java.util.function.BiConsumer;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.bukkit.ChatColor.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class CommandExecutorTest {
    
    private CommandSender sender;
    private Command command;
    private Context context;
    
    
    public CommandExecutorTest() {
        sender = when(mock(CommandSender.class).hasPermission("")).thenReturn(true).getMock();
        command = when(mock(Command.class).getPermission()).thenReturn("").getMock();
        context = when(mock(Context.class).getSender()).thenReturn(sender).getMock();
        when(context.getParentCommand()).thenReturn(command);
    }
    
    
    @ParameterizedTest
    @CsvSource({"false, '', 1", "true, '', 0", "false, p, 0"})
    public void wrap(boolean isNull, String permission, int times) {
        Command command = when(mock(Command.class).getPermission()).thenReturn(permission).getMock();
        
        Context context = when(mock(Context.class).getParentCommand()).thenReturn(isNull ? null : command).getMock();
        when(context.getSender()).thenReturn(sender);
        
        BiConsumer<Command, CommandSender> consumer = mock(BiConsumer.class);
        
        assertTrue(CommandExecutor.wrap(context, null, consumer));
        verify(consumer, times(times)).accept(command, sender);
    }
    
    
    @Test
    public void aliases() {
        CommandExecutor.ALIASES.execute(context, null);
        verify(sender).sendMessage(GOLD + "Aliases: " + RED + command.getAliases().toString());
    }
    
    
    @Test
    public void description() {
        CommandExecutor.DESCRIPTION.execute(context, null);
        verify(sender).sendMessage(GOLD  + "Description: " + RED + command.getDescription() + GOLD  +"\nUsage: " + RED + command.getUsage());
    }
    
    
    @Test
    public void help() {
        Command subcommand = when(mock(Command.class).getName()).thenReturn("subcommand").getMock();
        when(subcommand.getPermission()).thenReturn("");
        command.getSubcommands().put("subcommand", subcommand);
        
        CommandExecutor.HELP.execute(context, null);
        
        verify(sender).sendMessage(any(String[].class));
    }
    
    
    @Test
    public void none() {
        assertTrue(CommandExecutor.NONE.execute(null, null));
    }
    
}
