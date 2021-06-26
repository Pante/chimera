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
package com.karuslabs.commons.command.tree;

import com.karuslabs.commons.command.tree.nodes.Argument;
import com.karuslabs.commons.command.tree.nodes.Literal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MapperTest {
    
    Mapper<String, String> mapper = new Mapper<>();
    
    @Test
    void map_argument() {
        var unmapped = Argument.<String, String>builder("b", StringArgumentType.word()).description("desc").build();
        var argument = (Argument<String, String>) mapper.map(unmapped);
        
        assertEquals("b", argument.getName());
        assertSame(unmapped.getType(), argument.getType());
        assertEquals("desc", argument.description());
        assertSame(Mapper.NONE, argument.getCommand());
        assertSame(Mapper.TRUE, argument.getRequirement());
        assertNull(argument.getCustomSuggestions());
    }
    
    
    @Test
    void map_literal() {
        var literal = (Literal<String>) mapper.map(Literal.<String>builder("a").description("desc").build());
        
        assertEquals("a", literal.getName());
        assertEquals("desc", literal.description());
        assertSame(Mapper.NONE, literal.getCommand());
        assertSame(Mapper.TRUE, literal.getRequirement());
    }
    
    
    @Test
    void map_root() {
        assertEquals(RootCommandNode.class, mapper.map(new RootCommandNode<>()).getClass());
    }
    
    
    @Test
    void map_otherwise() {
        var command = mock(CommandNode.class);
        assertEquals(
            "Unsupported command, '" + command.getName() + "' of type: " + command.getClass().getName(), 
            assertThrows(IllegalArgumentException.class, () -> mapper.map(command)).getMessage()
        );
    }
    
    
    @Test
    void type() {
        var argument = Argument.<String, String>builder("", StringArgumentType.word()).build();
        assertEquals(argument.getType(), mapper.type(argument));
    }
    
    
    @Test
    void execution() throws CommandSyntaxException {
        assertEquals(0, mapper.execution(null).run(null));
    }
    
    
    @Test
    void predicate() {
        assertTrue(mapper.requirement(null).test(null));
    }
    
    
    @Test
    void suggestions() {
        assertNull(mapper.suggestions(null));
    }

} 
