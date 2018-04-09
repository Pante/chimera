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
import com.karuslabs.commons.command.annotation.Namespace;

import java.util.*;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CommandResolverTest {
    
    static CommandExecutor A = new A();
    
    @Namespace({})
    static class A implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    static CommandExecutor B = new B();
    
    @Namespace({"a", "b"})
    @Namespace({"c"})
    static class B implements CommandExecutor {
        
        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    LiteralResolver literal = mock(LiteralResolver.class);
    CommandResolver resolver = spy(new CommandResolver(null, singleton(literal)));
    ProxiedCommandMap map = mock(ProxiedCommandMap.class);
    Command command = mock(Command.class);
    Command a = mock(Command.class);
    Command b = mock(Command.class);
    Command c = mock(Command.class);
    
    
    @Test
    void resolve_Annotation() {
        doReturn(new Command[] {command}).when(resolver).find(map, A);
        when(literal.isResolvable(any())).thenReturn(true);
        
        resolver.resolve(map, A);
        
        verify(literal).resolve(A, command);
        verify(command).setExecutor(A);
    }
    
    
    @Test
    void resolve_Annotation_ThrowsException() {
        assertEquals("Unresolvable CommandExecutor: " + CommandExecutor.NONE.getClass().getName(),
            assertThrows(IllegalArgumentException.class, () -> resolver.resolve(map, CommandExecutor.NONE)).getMessage()
        );
    }
    
    
    @Test
    void resolve_Varargs() {
        ArgumentCaptor<Command> captor = ArgumentCaptor.forClass(Command.class);
        resolver.resolve(map, A, "command name");
        
        verify(map).register(eq("command name"), captor.capture());
        assertEquals(A, captor.getValue().getExecutor());
    }
    
    
    @Test
    void find_Annotation() {
        when(map.getCommand("a")).thenReturn(a);
        when(a.getSubcommands()).thenReturn(new HashMap<>(1));
        a.getSubcommands().put("b", b);
        
        when(map.getCommand("c")).thenReturn(c);
        
        assertThat(resolver.find(map, new B()), equalTo(new Command[] {b, c}));
    }
    
    
    @Test
    void find_Annotation_ThrowsException() {
        assertEquals("Invalid namespace for: " + A.class.getName() + ", namespace cannot be empty",
            assertThrows(IllegalArgumentException.class, () -> resolver.find(map, new A())).getMessage()
        );
    }
    
    
    @Test
    void append() {
        Command parent = new Command("", null);
        resolver.append(parent, "a");
        
        assertTrue(parent.getSubcommands().containsKey("a"));
    }
    
    
    @Test
    void simple() {
        Plugin plugin = mock(Plugin.class);
        resolver = CommandResolver.simple(plugin, new References());
        
        assertEquals(4, resolver.resolvers.size());
    }
    
}
