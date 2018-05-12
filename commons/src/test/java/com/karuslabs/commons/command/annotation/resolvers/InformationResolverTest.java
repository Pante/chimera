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
import com.karuslabs.commons.command.annotation.Information;

import java.lang.reflect.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class InformationResolverTest {
    
    
    static class A {

        @Information(aliases = {"a", "b"}, description = "a description", permission = "a.permission", message = "a message", usage = "a usage")
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    static class B implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    InformationResolver resolver = new InformationResolver();
    Command command = mock(Command.class);
    
    
    @Test
    void resolve() throws ReflectiveOperationException {
        Method method = A.class.getDeclaredMethod("execute", CommandSource.class, Context.class, com.karuslabs.commons.command.arguments.Arguments.class);
        resolver.resolve(method, new A()::execute, command);
        
        command.setAliases("a", "b");
        command.setDescription("a description");
        command.setPermission("a.permission");
        command.setPermissionMessage("a message");
        command.setUsage("a usage");
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(AnnotatedElement element, boolean expected) {
        assertEquals(expected, resolver.isResolvable(element));
    }
    
    static Stream<Arguments> isResolvable_parameters() throws ReflectiveOperationException {
        Method method = A.class.getDeclaredMethod("execute", CommandSource.class, Context.class, com.karuslabs.commons.command.arguments.Arguments.class);
        return Stream.of(of(method, true), of(B.class, false));
    }
    
}
