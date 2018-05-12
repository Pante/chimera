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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.Registered;
import com.karuslabs.commons.command.completion.Completion;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class RegisteredResolverTest {
    
    
    static class A {

        @Registered(index = 0, completion = "completion")
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    @Registered(index = 0, completion = "a")
    @Registered(index = 1, completion = "b")
    static class B implements CommandExecutor {

        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    RegisteredResolver resolver = new RegisteredResolver(new References().completion("completion", Completion.NONE));
    Command command = when(mock(Command.class).getCompletions()).thenReturn(new HashMap<>()).getMock();
    
    
    @Test
    void resolve() throws ReflectiveOperationException {
        Method method = A.class.getMethod("execute", CommandSource.class, Context.class, com.karuslabs.commons.command.arguments.Arguments.class);
        resolver.resolve(method, new A()::execute, command);
        
        assertSame(Completion.NONE, command.getCompletions().get(0));
    }
    
    
    @Test
    void resolve_ThrowsException() {
        assertEquals("Unresolvable reference: \"a\" for: " + B.class.getName(), 
            assertThrows(IllegalArgumentException.class, () -> resolver.resolve(B.class, new B(), command)).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(AnnotatedElement element, boolean expected) {
        assertEquals(expected, resolver.isResolvable(element));
    }
    
    static Stream<Arguments> isResolvable_parameters() throws ReflectiveOperationException {
        Method method = A.class.getMethod("execute", CommandSource.class, Context.class, com.karuslabs.commons.command.arguments.Arguments.class);
        return Stream.of(of(method, true), of(B.class, true), of(CommandExecutor.NONE.getClass(), false));
    }
    
    
}
