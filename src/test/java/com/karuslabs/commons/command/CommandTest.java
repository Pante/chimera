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

import com.karuslabs.commons.command.arguments.Arguments;
import com.karuslabs.commons.command.completion.Completion;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class CommandTest {
    
    private Command command = spy(new Command("", null));
    private CommandSender sender = when(mock(CommandSender.class).hasPermission("permission")).thenReturn(true).getMock();
    
    
    @Test
    public void execute_unwrapped() {
        doReturn(true).when(command).execute(any(Context.class), any(Arguments.class));
        
        command.execute(sender, "", new String[] {});
        
        verify(command).execute(any(Context.class), any(Arguments.class));
    }
    
    
    @ParameterizedTest
    @CsvSource({"a, 1, 0", "b, 0, 1"})
    public void execute(String argument, int delegated, int executor) {
        Context context = mock(Context.class);
        Arguments arguments = spy(new Arguments(argument));
        
        Command subcommand = mock(Command.class);
        command.getSubcommands().put("a", subcommand);
        
        CommandExecutor stub = mock(CommandExecutor.class);
        command.setExecutor(stub);
        
        command.execute(context, arguments);
        
        verify(arguments, times(delegated)).trim();
        verify(context, times(delegated)).update("a", subcommand);
        verify(subcommand, times(delegated)).execute(context, arguments);
        
        verify(stub, times(executor)).execute(context, arguments);
    }
    
    
    @Test
    public void tabComplete() {
        doReturn(EMPTY_LIST).when(command).complete(any(), any(Arguments.class));
        
        command.tabComplete(sender, "", new String[] {});
        
        verify(command).complete(any(CommandSender.class), any(Arguments.class));
    }
    
    
    @Test
    public void complete_empty() {
        assertTrue(EMPTY_LIST == command.complete(sender, new Arguments()));
    }
    
    
    @Test
    public void complete_delegate() {
        Arguments arguments = spy(new Arguments("subcommand"));
        Command subcommand = when(mock(Command.class).complete(sender, arguments)).thenReturn(singletonList("a")).getMock();
        
        command.getSubcommands().put("subcommand", subcommand);
        assertEquals(singletonList("a"), command.complete(sender, arguments));
        verify(arguments).trim();
        verify(subcommand).complete(sender, arguments);
    }
    
    
    @ParameterizedTest
    @MethodSource("complete_parameters")
    public void complete_subcommands(String name, String permission, List<String> expected) {
        Arguments arguments = new Arguments("subcommand");
        
        Command subcommand = new Command(name, null);
        subcommand.setPermission(permission);
        command.getSubcommands().put(name, subcommand);
        
        assertEquals(expected, command.complete(sender, arguments));
    }
    
    static Stream<org.junit.jupiter.params.provider.Arguments> complete_parameters() {
        return Stream.of(
            of("subcommand1", "permission", asList("subcommand1")),
            of("subcommand", "", EMPTY_LIST),
            of("sub", "permission", EMPTY_LIST)
        );
    }
    
    
    @Test
    public void complete_completions() {
        Arguments arguments = new Arguments("argument");
        Completion completion = when(mock(Completion.class).complete(sender, "argument")).thenReturn(singletonList("a")).getMock();
        command.getCompletions().put(0, completion);
        
        assertEquals(singletonList("a"), command.complete(sender, arguments));
        verify(completion).complete(sender, "argument");
    }
    
}
