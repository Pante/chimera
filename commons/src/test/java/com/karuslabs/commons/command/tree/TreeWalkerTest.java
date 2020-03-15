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

import com.karuslabs.commons.command.tree.nodes.Literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TreeWalkerTest {
    
    static Literal<String> mapped = Literal.<String>builder("child").build();
    
    TreeWalker<String, String> tree = spy(new TreeWalker(spy(new Mapper<>())));
    RootCommandNode<String> root = new RootCommandNode<>();
    Literal<String> literal = Literal.<String>builder("parent").then(Literal.<String>builder("child").build()).build();
    List<CommandNode<String>> commands = List.of(
        Literal.<String>builder("a").build(), 
        Literal.<String>builder("b").build(), 
        Literal.<String>builder("c").requires(val -> false).build()
    );
    
    
    @Test
    void prune() {
        root.addChild(Literal.<String>builder("a").then(Literal.<String>builder("child").build()).build());
        
        tree.prune(root, commands);
        
        assertEquals(3, root.getChildren().size());
        assertNull(root.getChild("a").getChild("child"));
    }
    
    
    @Test
    void add() {
        root.addChild(Literal.<String>builder("a").then(Literal.<String>builder("child").build()).build());
        
        tree.add(root, commands, "", command -> !command.getName().equalsIgnoreCase("b"));
        
        assertEquals(1, root.getChildren().size());
        assertNotNull(root.getChild("a").getChild("child"));
    }
    
    
    @Test
    void map() {
        var a = Literal.<String>builder("a").then(Literal.<String>builder("b").requires(val -> false).build()).build();
        var c = Literal.<String>builder("c").redirect(a).build();
        var d = Literal.<String>builder("d").then(c).build();
        
        
        root.addChild(a);
        root.addChild(c);
        root.addChild(d);
        
        var mapped = tree.map(root, "", new IdentityHashMap<>());
        var empty = mapped.getChild("a");
        var same = mapped.getChild("d").getChild("c");
        
        assertEquals("", mapped.getName());
        assertEquals(0, empty.getChildren().size());
        assertSame(empty, mapped.getChild("c").getRedirect());
        assertSame(mapped.getChild("c"), same);
    }
    
    
    @ParameterizedTest
    @MethodSource("redirect_parameters")
    void redirect(CommandNode<String> destination, CommandNode<String> target, CommandNode<String> expected) {
        doReturn(destination).when(tree).map(any(CommandNode.class), any(String.class), any(Map.class));
        literal.setRedirect(destination);
        
        tree.redirect(literal, target, "", new HashMap<>());

        
        assertSame(expected, target.getRedirect());
    }
    
    static Stream<Arguments> redirect_parameters() {
        var destination = LiteralArgumentBuilder.<String>literal("a").build();
        return Stream.of(
            of(destination, Literal.builder("a").build(), destination),
            of(destination, LiteralArgumentBuilder.<String>literal("a").build(), null),
            of(null, Literal.builder("a").build(), null),
            of(null, LiteralArgumentBuilder.<String>literal("a").build(), null)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("descend_parameters")
    void descend(CommandNode<String> returned) {
        doReturn(returned).when(tree).map(any(CommandNode.class), any(String.class), any(Map.class));
        
        var target = new RootCommandNode<String>();
        
        tree.descend(literal, target, "", new HashMap<>());
        assertSame(returned, target.getChild("child"));
    }
    
    static Stream<CommandNode<String>> descend_parameters() {
        return Stream.of(mapped, null);
    }
    
} 
