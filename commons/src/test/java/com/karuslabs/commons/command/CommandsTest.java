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

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.*;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class CommandsTest {
    
    Command<Object> command = val -> 1;
    Argument<Object, String> argument = Argument.builder("b", word()).executes(command).then(Argument.builder("b1", word())).build();
    Literal<Object> literal = Literal.builder("name").executes(command).then(Literal.builder("a").alias("a1", "a2")).then(argument).build();

    
    
    @Test
    void alias() {
        var alias = Commands.alias(literal, "alias");
        
        assertEquals("alias", alias.getName());
        assertEquals(literal.getCommand(), alias.getCommand());
        assertEquals(4, alias.getChildren().size());
        assertTrue(literal.aliases().contains(alias));
    }
    
    
    @Test
    void executes() {
        Command<Object> command = val -> 1;
        Commands.executes(literal, command);
        assertSame(command, literal.getCommand());
    }
    
    
    @Test
    void children_get() {
        assertTrue(Commands.children(literal).containsKey("a"));
    }
    
    
    @Test
    void children_set() {
        Commands.children(literal, Map.of());
        assertTrue(literal.getChildren().isEmpty());
    }
    
    
    @ParameterizedTest
    @MethodSource("child")
    void remove_child(String child, boolean removed, int size) {
        assertEquals(removed, Commands.remove(literal, child) != null);
        assertEquals(size, literal.getChildren().size());
    }
    
    static Stream<Arguments> child() {
        return Stream.of(
            of("a", true, 1),
            of("c", false, 4)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("remove_children_parameters")
    void remove_children(String[] children, boolean all, int size) {
        assertEquals(all, Commands.remove(literal, children));
        assertEquals(size, literal.getChildren().size());
    }
    
    static Stream<Arguments> remove_children_parameters() {
        return Stream.of(
            of(new String[] {"a"}, true, 1),
            of(new String[] {"a", "b"}, true, 0),
            of(new String[] {"a", "b", "c"}, false, 0),
            of(new String[] {"other"}, false, 4)
        );
    }
    
} 
