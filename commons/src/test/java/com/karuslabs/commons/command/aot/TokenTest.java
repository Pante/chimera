/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.aot;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TokenTest {
    
    Token argument = spy(Token.argument(mock(Element.class), "<c>", "c"));
    Token literal = Token.literal(mock(Element.class), "a|b", "a", new HashSet<>(Set.of("b")));
    Token d = Token.literal(mock(Element.class), "d", "d", Set.of());
    Environment environment = mock(Environment.class);
    
    
    @Test
    void argument() {
        assertNotNull(argument.location);
        assertEquals("<c>", argument.literal);
        assertEquals("c", argument.lexeme);
        assertEquals(Type.ARGUMENT, argument.type);
        assertTrue(argument.aliases.isEmpty());
    }
    
    
    @Test
    void literal() {
        assertNotNull(literal.location);
        assertEquals("a|b", literal.literal);
        assertEquals("a", literal.lexeme);
        assertEquals(Type.LITERAL, literal.type);
        assertEquals(Set.of("b"), literal.aliases);
    }
    
    
    @Test
    void root() {
        var root = Token.root();
        
        assertNull(root.location);
        assertEquals("", root.literal);
        assertEquals("", root.lexeme);
        assertEquals(Type.ROOT, root.type);
        assertTrue(root.aliases.isEmpty());
    }
    
    
    @Test
    void add() {
        assertEquals(argument, literal.add(environment, argument));
        assertEquals(argument, literal.children.get(argument.lexeme));
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void add_existing() {
        literal.children.put(argument.lexeme, argument);

        assertEquals(argument, literal.add(environment, argument));
        verify(argument).merge(environment, argument);
    }

    
    @Test
    void merge_duplicate_aliases() {
        assertEquals(literal, literal.merge(environment, Token.literal(mock(Element.class), "a|b", "a", Set.of("b"))));
        verify(environment).warn(literal.location, "Duplicate alias: \"b\"");
    }
    
    
    @Test
    void merge_different_type() {
        assertNull(literal.merge(environment, argument));
        verify(environment).error(literal.location, "\"a\" already exists, an argument and literal in the same scope should not have the same name");
    }
    
    
    @Test
    void bind() {
        literal.bind(environment, Binding.COMMAND, d);
        
        assertEquals(d, literal.bindings.get(Binding.COMMAND));
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void bind_existing() {
        literal.bindings.put(Binding.COMMAND, d);
        
        literal.bind(environment, Binding.COMMAND, d);
        
        verify(environment).error(d.location, "A Command<CommandSender> is already bound to \"a|b\"");
    }
    
    
    @ParameterizedTest
    @MethodSource("bind_invalid_target_parameters")
    void bind_invalid_target(Binding binding, String type) {
        literal.bind(environment, binding, d);
        
        verify(environment).error(d.location, type + " should not be bound to a literal");
    }
    
    static Stream<Arguments> bind_invalid_target_parameters() {
        return Stream.of(
            of(Binding.TYPE, "An ArgumentType<?>"),
            of(Binding.SUGGESTIONS, "A SuggestionProvider<CommandSender>")    
        );
    }
    
    @Test
    void token_toString() {
        assertEquals(literal.literal, literal.toString());
    }

} 
