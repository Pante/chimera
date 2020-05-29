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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.commons.command.dispatcher.*;
import com.karuslabs.commons.command.types.EnchantmentType;

import com.mojang.brigadier.tree.*;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RootTest {
    
    DispatcherMap map = mock(DispatcherMap.class);
    Root root = new Root("test", map);
    Literal<CommandSender> literal = Literal.of("a").alias("a1").build();
    
    
    @Test
    void addChild() {
        when(map.register(literal)).thenReturn(new DispatcherCommand("a", null, null, null, List.of("a1")));
        
        root.addChild(literal);
        
        assertNotNull(root.getChild("a"));
        assertNotNull(root.getChild("test:a"));
        assertNotNull(root.getChild("a1"));
        assertNotNull(root.getChild("test:a1"));
    }
    
    
    @Test
    void addChild_bukkit_contains_name() {
        var command = new DispatcherCommand("a", null, null, null, List.of("a1"));
        command.setLabel("label");
        
        when(map.register(literal)).thenReturn(command);
        
        root.addChild(literal);
        
        assertNull(root.getChild("a"));
        assertNotNull(root.getChild("test:a"));
        assertNotNull(root.getChild("a1"));
        assertNotNull(root.getChild("test:a1"));
    }
    
    
    @Test
    void addChild_bukkit_contains_alias() {
        when(map.register(literal)).thenReturn(new DispatcherCommand("a", null, null, null, List.of()));
        
        root.addChild(literal);
        
        assertNotNull(root.getChild("a"));
        assertNotNull(root.getChild("test:a"));
        assertNull(root.getChild("a1"));
        assertNull(root.getChild("test:a1"));
    }
    
    
    @Test
    void addChild_unregistered() {
        root.addChild(Literal.of("a").build());
        
        assertEquals(0, root.getChildren().size());
    }
    
    
    @Test
    void addChild_duplicate_throws_exception() {
        when(map.register(any())).thenReturn(new DispatcherCommand("", null, null, null, List.of("")));
        
        root.addChild(Literal.of("a").build());
        
        assertEquals("Invalid command: 'a', root already contains a child with the same name",
            assertThrows(IllegalArgumentException.class, () -> root.addChild(Literal.of("a").build())).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("addChild_throws_exception_parameters")
    void addChild_non_literal_throws_exception(CommandNode<CommandSender> command) {
        assertEquals("Invalid command: '" + command.getName() + "', commands registered to root must be a literal", 
            assertThrows(IllegalArgumentException.class, () -> root.addChild(command)).getMessage()
        );
    }
    
    static Stream<CommandNode<CommandSender>> addChild_throws_exception_parameters() {
        return Stream.of(new RootCommandNode<>(), Argument.of("name", new EnchantmentType()).build());
    }
    
    
    @Test
    void removeChild() {
        when(map.register(literal)).thenReturn(new DispatcherCommand("a", null, null, null, List.of("a1")));
        
        root.addChild(literal);
        
        assertEquals(4, root.getChildren().size());
        
        assertEquals(literal, root.removeChild("a"));
        assertEquals(0, root.getChildren().size());
    }
    
    
    @Test
    void getters() {
        var command = root.getCommand();
        root.setCommand(mock(com.mojang.brigadier.Command.class));
        
        assertSame(command, root.getCommand());
        
        var destination = root.getRedirect();
        root.setRedirect(literal);
        
        assertSame(destination, root.getRedirect());
        
        assertSame(map, root.getDispatcherMap());
    }

} 
