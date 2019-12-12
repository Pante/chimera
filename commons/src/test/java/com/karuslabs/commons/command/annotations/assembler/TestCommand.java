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

import com.karuslabs.commons.command.DefaultableContext;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.command.types.EnchantmentType;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;

import static org.junit.jupiter.api.Assertions.*;


@Literal(namespace = "a", aliases = "a1")
@Argument(namespace = {"a", "b"})
public class TestCommand {
    
    static @Bind ArgumentType<String> b = StringArgumentType.word();
    @Bind("argument") EnchantmentType something = new EnchantmentType();
    @Bind("argument") SuggestionProvider provider = (context, builder) -> builder.buildFuture();
    EnchantmentType argument = new EnchantmentType();
    
    
    @Literal(namespace = {"a", "b", "c1"}, aliases = "c1a")
    int c1(CommandContext<String> context) {
        return 1;
    }
    
    
    @Argument(namespace = {"a", "b", "c2"}, type = "argument", suggestions = "argument")
    void c2(DefaultableContext<String> context) {
        
    }
    
    
    int execute(CommandContext<String> context) {
        return 1;
    }
    
    
    public static void assertCommand(TestCommand annotated, CommandNode<?> command) {
        var a = (com.karuslabs.commons.command.tree.nodes.Literal<?>) command;
        
        assertEquals(1, a.getChildren().size());
        assertEquals(1, a.aliases().size());
        
        var a1 = (com.karuslabs.commons.command.tree.nodes.Literal<?>) a.aliases().get(0);
        
        assertEquals(1, a1.getChildren().size());
        assertTrue(a1.isAlias());
        assertEquals(0, a1.aliases().size());
        
        var b = (com.karuslabs.commons.command.tree.nodes.Argument<?, ?>) a.getChild("b");
        
        assertEquals(3, b.getChildren().size());
        assertSame(TestCommand.b, b.getType());
        assertNull(b.getCustomSuggestions());
        
        var c1 = (com.karuslabs.commons.command.tree.nodes.Literal<?>) b.getChild("c1");
        
        assertEquals(0, c1.getChildren().size());
        assertEquals(1, c1.aliases().size());
        
        var c1a = (com.karuslabs.commons.command.tree.nodes.Literal<?>) c1.aliases().get(0);
        
        assertEquals(0, c1a.getChildren().size());
        assertTrue(c1a.isAlias());
        assertEquals(0, c1a.aliases().size());
        
        assertSame(c1.getCommand(), c1a.getCommand());
        
        
        var c2 = (com.karuslabs.commons.command.tree.nodes.Argument<?, ?>) b.getChild("c2");
        
        assertEquals(0, c2.getChildren().size());
        assertSame(annotated.something, c2.getType());
        assertSame(annotated.provider, c2.getCustomSuggestions());
        assertNotNull(c2.getCommand());
    }
    
}
