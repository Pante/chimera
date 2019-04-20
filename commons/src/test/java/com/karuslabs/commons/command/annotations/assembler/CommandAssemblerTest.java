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
package com.karuslabs.commons.command.annotations.assembler;

import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collections.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.RootCommandNode;

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
class CommandAssemblerTest {
    
    @Literal(namespace = {"a", "b", "c", "d"}, aliases = {"da"})
    @Literal(namespace = {"a", "b", "c"}, aliases = {"c1a", "c1b"})
    @Argument(namespace = {"a", "b", "c3"}, type = "bird")
    @Argument(namespace = {"a", "b", "c4"}, type = "is", suggestions = "bird")
    static class Type {
        
    }
    
    
    CommandAssembler<Object> assembler = new CommandAssembler<>(new RootCommandNode<>(), TokenMap.of());
    Command<Object> execution = context -> 0;
    
    
    @Test
    void assemble_literal() {
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Literal.class), execution);
        
        var c = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) assembler.container.getChild("a").getChild("b").getChild("c");
        var c1a = c.aliases().get(0);
        
        var d = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) c.getChild("d");
        var da = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) d.aliases().get(0);
        
        assertEquals("c", c.getName());
        assertEquals(2, c.getChildren().size());
        assertEquals(2, c.aliases().size());
        assertEquals(execution, c.getCommand());
        
        assertEquals("c1a", c1a.getName());
        assertEquals(2, c1a.getChildren().size());
        assertEquals(execution, c1a.getCommand());
        
        assertEquals("d", d.getName());
        assertEquals(1, d.aliases().size());
        
        assertSame(c1a.getChild("d"), d);
        assertSame(c1a.getChild("da"), da);
    }
    
    
    @Test
    void assemble_argument() {
        var birdType = mock(ArgumentType.class);
        var isType = mock(ArgumentType.class);
        var birdSuggestions = mock(SuggestionProvider.class);
        
        assembler.bindings.put("bird", ArgumentType.class, birdType);
        assembler.bindings.put("is", ArgumentType.class, isType);
        assembler.bindings.put("bird", SuggestionProvider.class, birdSuggestions);
        
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Argument.class), execution);
        var b = assembler.container.getChild("a").getChild("b");
        
        var c3 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) b.getChild("c3");
        
        assertEquals("c3", c3.getName());
        assertEquals(birdType, c3.getType());
        assertNull(c3.createBuilder().getSuggestionsProvider());
        assertEquals(execution, c3.getCommand());
        
        var c4 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) b.getChild("c4");
        
        assertEquals("c4", c4.getName());
        assertEquals(isType, c4.getType());
        assertEquals(birdSuggestions, c4.createBuilder().getSuggestionsProvider());
        assertEquals(execution, c3.getCommand());
    }
    
    
    @Test
    void descend() {
        assembler.container.addChild(literal("a").build());
        var b = assembler.descend(Type.class, "Literal", new String[] {"a", "b", "c"});
        
        assertNotNull(b);
        assertEquals(assembler.container.getChild("a").getChild("b"), b);
    }
    
    
    @Test
    void descend_throws_exception() {
        assertEquals("Invalid namespace for: @Test in " + Type.class,
            assertThrows(IllegalArgumentException.class, () -> assembler.descend(Type.class, "Test", new String[] {})).getMessage()
        );
    }
    
    
    com.karuslabs.commons.command.tree.nodes.Literal.Builder<Object> literal(String name) {
        return com.karuslabs.commons.command.tree.nodes.Literal.<Object>builder(name);
    }
    
} 
