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
package com.karuslabs.commons.command.annotation.providers;

import com.karuslabs.commons.command.annotation.providers.NamespaceProvider;
import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.Namespace;

import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class NamespaceProviderTest {
    
    @Namespace({})
    static class A implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    @Namespace({"a", "b"})
    @Namespace({"c"})
    static class B implements CommandExecutor {
        
        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    NamespaceProvider resolver = new NamespaceProvider(null);
    ProxiedCommandMap map = mock(ProxiedCommandMap.class);
    Command a = mock(Command.class);
    Command b = mock(Command.class);
    Command c = mock(Command.class);
    
    
    @Test
    void resolve() {
        when(map.getCommand("a")).thenReturn(a);
        when(a.getSubcommands()).thenReturn(new HashMap<>(1));
        a.getSubcommands().put("b", b);
        
        when(map.getCommand("c")).thenReturn(c);
        
        assertEquals(asList(b, c), resolver.resolve(map, new B()));
    }
    
    
    @Test
    void resolve_empty_ThrowsException() {
        assertEquals(
            "Invalid namespace for: " + A.class.getName() + ", namespace cannot be empty",
            assertThrows(IllegalArgumentException.class, () -> resolver.resolve(map, new A())).getMessage()
        );
    }
    
    
    @Test
    void append() {
        Command parent = new Command("", null);
        resolver.append(parent, "a");
        
        assertTrue(parent.getSubcommands().containsKey("a"));
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(CommandExecutor executor, boolean expected) {
        assertEquals(expected, resolver.isResolvable(executor));
    }
    
    static Stream<Arguments> isResolvable_parameters() {
        CommandExecutor executor = (source, context, arguments) -> true;
        return Stream.of(of(new A(), true), of(new B(), true), of(executor, false));
    }
    
}
