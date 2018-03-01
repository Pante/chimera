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
import com.karuslabs.commons.command.annotation.Registered;
import com.karuslabs.commons.command.completion.Completion;

import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class RegisteredProcessorTest {
    
    @Registered(index = 0, completion = "completion")
    static class A implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    @Registered(index = 0, completion = "a")
    @Registered(index = 1, completion = "b")
    static class B implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    RegisteredProcessor processor = new RegisteredProcessor(new References().completion("completion", Completion.NONE));
    Command command = when(mock(Command.class).getCompletions()).thenReturn(new HashMap<>()).getMock();
    
    
    @Test
    void process() {
        processor.process(asList(command), new A());
        
        assertSame(Completion.NONE, command.getCompletions().get(0));
    }
    
    
    @Test
    void process_ThrowsException() {
        assertEquals(
            "Unresolvable reference: \"a\" for: " + B.class.getName(), 
            assertThrows(IllegalArgumentException.class, () -> processor.process(asList(command), new B())).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("hasAnnotations_parameters")
    void hasAnnotations(CommandExecutor executor, boolean expected) {
        assertEquals(expected, processor.hasAnnotations(executor));
    }
    
    static Stream<Arguments> hasAnnotations_parameters() {
        CommandExecutor executor = (source, context, arguments) -> true;
        return Stream.of(of(new A(), true), of(new B(), true), of(executor, false));
    }
    
    
}
