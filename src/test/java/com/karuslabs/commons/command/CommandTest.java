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

import com.karuslabs.commons.command.arguments.Argument;
import com.karuslabs.commons.command.arguments.Arguments;
import com.karuslabs.commons.locale.Translation;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class CommandTest {
    
    private Command command = spy(new Command("", null, Translation.NONE));
    
    
    @ParameterizedTest
    @MethodSource("split_parameters")
    public void split(String[] arguments, String[] expected) {
        assertThat(Command.split(arguments), equalTo(expected));
    }
    
    static Stream<org.junit.jupiter.params.provider.Arguments> split_parameters() {
        String[] arguments = new String[] {"1", "2", "3"};
        return Stream.of(of(arguments, arguments), of(new String[] {"\"1", "2\"", "3"}, new String[] {"1 2", "3"}));
    }
    
    
    @Test
    public void execute_unwrapped() {
        doReturn(true).when(command).execute(any(Context.class), any(Arguments.class));
        
        command.execute(null, "", new String[] {});
        
        verify(command).execute(any(Context.class), any(Arguments.class));
    }
    
    
    @ParameterizedTest
    @CsvSource({"a, 1, 0", "b, 0, 1"})
    public void execute(String argument, int delegated, int executor) {
        Context context = mock(Context.class);
        Arguments arguments = when(mock(Arguments.class).getLast()).thenReturn(new Argument(argument)).getMock();
        
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
        
        command.tabComplete(null, "", new String[] {});
        
        verify(command).complete(any(), any(Arguments.class));
    }
    
    
    @Test
    public void complete_empty() {
        assertTrue(EMPTY_LIST == command.complete(null, new Arguments(new String[] {})));
    }
    
    
    @Test
    public void complete() {
        
    }
    
}
