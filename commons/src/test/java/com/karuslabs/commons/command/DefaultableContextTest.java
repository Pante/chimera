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
package com.karuslabs.commons.command;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DefaultableContextTest {
    
    static final Object SOURCE = new Object();
    static final Command<Object> COMMAND = context -> 1;
    static final StringRange RANGE = new StringRange(0, 0);
    static final RedirectModifier<Object> MODIFIER = context -> List.of();
    
    
    CommandContext<Object> context = new CommandContext<>(SOURCE, "", Map.of("argument", new ParsedArgument<>(0, 1, "value")), COMMAND, null, List.of(), RANGE, null, MODIFIER, false);
    DefaultableContext<Object> defaultable = new DefaultableContext<>(context);
    
    CommandContext<Object> mock = mock(CommandContext.class);
    DefaultableContext<Object> delegate = new DefaultableContext<>(mock);
    
    
    @Test
    void getOptionalArgument() {
        assertEquals("value", defaultable.getOptionalArgument("argument", String.class));
    }
    
    
    @Test
    void getOptionalArgument_null() {
        assertNull(defaultable.getOptionalArgument("invalid", String.class));
    }
    
    
    @Test
    void getOptionalArgument_default() {
        assertEquals("value", defaultable.getOptionalArgument("argument", String.class, "value"));
    }
    
    
    @Test
    void getOptionalArgument_default_default() {
        assertEquals("default", defaultable.getOptionalArgument("invalid", String.class, "default"));
    }
    
    
    @Test
    void delegate() {
        delegate.copyFor(null);
        verify(mock).copyFor(null);
        
        delegate.getChild();
        verify(mock).getChild();
        
        delegate.getLastChild();
        verify(mock).getLastChild();
        
        delegate.getCommand();
        verify(mock).getCommand();
        
        delegate.getSource();
        verify(mock).getSource();
        
        delegate.getArgument("name", String.class);
        verify(mock).getArgument("name", String.class);
        
        delegate.getRedirectModifier();
        verify(mock).getRedirectModifier();
        
        delegate.getRange();
        verify(mock).getRange();
        
        delegate.getInput();
        verify(mock).getInput();
        
        delegate.getNodes();
        verify(mock).getNodes();
        
        delegate.isForked();
        verify(mock).isForked();
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_provider")
    void equality(Object other, boolean expected) {
        assertEquals(expected, defaultable.equals(other));
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_provider")
    void hashCode(Object other, boolean expected) {
        assertEquals(expected, defaultable.hashCode() == Objects.hashCode(other));
    }
    
    
    static Stream<Arguments> equality_provider() {
        var context = new CommandContext<>(SOURCE, "", Map.of("argument", new ParsedArgument<>(0, 1, "value")), COMMAND, null, List.of(), RANGE, null, MODIFIER, false);
        var other = new DefaultableContext<>(context);
        return Stream.of(
            of(other, true),
            of(new DefaultableContext(context) {}, true),
            of(null, false)
        );
    }

} 
