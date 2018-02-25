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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.commons.command.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CommandProcessorTest {
    
    Resolver resolver = mock(Resolver.class);
    LiteralProcessor literal = mock(LiteralProcessor.class);
    CommandProcessor processor = new CommandProcessor(singleton(literal), resolver);
    ProxiedCommandMap map = mock(ProxiedCommandMap.class);
    Command command = mock(Command.class);
    
    
    @Test
    void process() {
        List<Command> commands = singletonList(command);
        when(resolver.isResolvable(any())).thenReturn(true);
        when(resolver.resolve(map, CommandExecutor.NONE)).thenReturn(commands);
        when(literal.hasAnnotations(any())).thenReturn(true);
        
        processor.process(map, CommandExecutor.NONE);
        
        verify(literal).process(commands, CommandExecutor.NONE);
    }
    
    
    @Test
    void process_ThrowsException() {
        when(resolver.isResolvable(any())).thenReturn(false);
        
        assertEquals(
            "unresolvable CommandExecutor: " + CommandExecutor.NONE.getClass().getName(),
            assertThrows(IllegalArgumentException.class, () -> processor.process(map, CommandExecutor.NONE)).getMessage()
        );
    }
    
}
