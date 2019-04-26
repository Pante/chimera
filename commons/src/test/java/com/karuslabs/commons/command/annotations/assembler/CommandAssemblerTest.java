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
import com.karuslabs.commons.command.tree.nodes.Aliasable;
import com.karuslabs.commons.util.collections.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class CommandAssemblerTest {
    
    @Literal(namespace = {"a", "b", "c", "d"}, aliases = {"da"})
    @Literal(namespace = {"a", "b", "c"}, aliases = {"c1a", "c1b"})
    @Argument(namespace = {"a", "b", "c3"}, type = "bird")
    @Argument(namespace = {"a", "b", "c4"}, type = "is", suggestions = "bird")
    @Argument(namespace = {"a", "b", "c5"})
    static class Type {
        
    }
    
    
    TokenMap<String, Object> bindings = TokenMap.of();
    Map<String, Node<Object>> nodes = new HashMap<>();
    CommandAssembler<Object> assembler = new CommandAssembler<>(bindings, nodes);
    Command<Object> execution = context -> 0;
    
    
    @Test
    void assemble() {   
        var birdType = mock(ArgumentType.class);
        var isType = mock(ArgumentType.class);
        var birdSuggestions = mock(SuggestionProvider.class);
        var c5Type = mock(ArgumentType.class);
        
        assembler.bindings.put("bird", ArgumentType.class, birdType);
        assembler.bindings.put("is", ArgumentType.class, isType);
        assembler.bindings.put("bird", SuggestionProvider.class, birdSuggestions);
        assembler.bindings.put("c5", ArgumentType.class, c5Type);
        
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Literal.class), execution);
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Argument.class), execution);
        
        var commands = assembler.assemble();
        assertEquals(1, commands.size());
        
        var b =  commands.get("a").getChild("b");
        
        assertNull(b.getCommand());
        assertEquals(6, b.getChildren().size());
        
        var c = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) b.getChild("c");
        
        assertEquals(execution, c.getCommand());
        assertEquals(2, c.aliases().size());
        assertEquals(2, c.getChildren().size());
        
        var c1a = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) c.aliases().get(0);
        
        assertEquals(execution, c1a.getCommand());
        assertTrue(c1a.isAlias());
        assertTrue(c1a.aliases().isEmpty());
        assertEquals(2, c1a.getChildren().size());
        
        var d = (com.karuslabs.commons.command.tree.nodes.Literal<Object>) c.getChild("d");
        
        assertEquals(execution, d.getCommand());
        assertEquals(1, d.aliases().size());
        assertTrue(d.getChildren().isEmpty());
        
        
        var c3 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) b.getChild("c3");
        assertSame(birdType, c3.getType());
        assertNull(c3.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
                
        var c4 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) b.getChild("c4");
        assertSame(isType, c4.getType());
        assertSame(birdSuggestions, c4.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
        
        var c5 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) b.getChild("c5");
        assertSame(c5Type, c5.getType());
        assertNull(c5.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
    }
    
    
    @Test
    void assemble_literals() {
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Literal.class), execution);
        
        var c = assembler.nodes.get("a").children().get("b").children().get("c");
        var cAliases = ((Aliasable<?>) c.get()).aliases();
        
        assertEquals("c", c.name());
        assertEquals(execution, c.get().getCommand());
        assertTrue(((Aliasable<?>) cAliases.get(0)).isAlias());
        assertTrue(((Aliasable<?>) cAliases.get(1)).isAlias());
                
        var d = c.children().get("d");
        var dAliases = ((Aliasable<?>) d.get()).aliases();
        
        assertEquals("d", d.name());
        assertEquals(execution, d.get().getCommand());
        assertTrue(((Aliasable<?>) dAliases.get(0)).isAlias());
    }
    
    
    @Test
    void assemble_arguments() {
        var birdType = mock(ArgumentType.class);
        var isType = mock(ArgumentType.class);
        var birdSuggestions = mock(SuggestionProvider.class);
        var c5Type = mock(ArgumentType.class);
        
        assembler.bindings.put("bird", ArgumentType.class, birdType);
        assembler.bindings.put("is", ArgumentType.class, isType);
        assembler.bindings.put("bird", SuggestionProvider.class, birdSuggestions);
        assembler.bindings.put("c5", ArgumentType.class, c5Type);
        
        assembler.assemble(Type.class, Type.class.getAnnotationsByType(Argument.class), execution);
        var nodes = assembler.nodes.get("a").children().get("b").children();
        
        var c3 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) nodes.get("c3").get();        
        assertSame(birdType, c3.getType());
        assertNull(c3.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
                
        var c4 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) nodes.get("c4").get();
        assertSame(isType, c4.getType());
        assertSame(birdSuggestions, c4.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
        
        var c5 = (com.karuslabs.commons.command.tree.nodes.Argument<Object, ?>) nodes.get("c5").get();
        assertSame(c5Type, c5.getType());
        assertNull(c5.getCustomSuggestions());
        assertEquals(execution, c3.getCommand());
    }
    
    
    @Test
    void descend() {
        assembler.descend(Type.class, "Literal", new String[] {"a", "b", "c"});
        
        assertEquals("c", assembler.nodes.get("a").children().get("b").children().get("c").name());
    }
    
    
    @Test
    void descend_throws_exception() {
        assertEquals("Invalid namespace for @Test in " + Type.class,
            assertThrows(IllegalArgumentException.class, () -> assembler.descend(Type.class, "Test", new String[] {})).getMessage()
        );
    }
    
    
    com.karuslabs.commons.command.tree.nodes.Literal.Builder<Object> literal(String name) {
        return com.karuslabs.commons.command.tree.nodes.Literal.<Object>builder(name);
    }
    
    com.karuslabs.commons.command.tree.nodes.Argument.Builder<Object, ?> argument(String name, ArgumentType<Object> type) {
        return com.karuslabs.commons.command.tree.nodes.Argument.<Object, Object>builder(name, type);
    }
    
} 
