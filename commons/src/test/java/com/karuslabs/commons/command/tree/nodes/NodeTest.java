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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;

import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NodeTest {
    
    @ParameterizedTest
    @MethodSource("node_parameters")
    void addChild_aliases(Node<CommandSender> node) {
        var child = Literal.of("child").build();
        
        node.addChild(child);
        
        assertEquals(child, ((CommandNode<?>) node).getChild("child"));
        assertEquals(child, node.aliases().get(0).getChild("child"));
    }
    
    
    @ParameterizedTest
    @MethodSource("node_parameters")
    void removeChild_aliases(Node<CommandSender> node) {
        var child = Literal.of("child").build();
        
        node.addChild(child);
        node.removeChild("child");
        
        assertEquals(2, ((CommandNode<?>) node).getChildren().size());
        assertEquals(2, node.aliases().get(0).getChildren().size());
    }
    
    
    @ParameterizedTest
    @MethodSource("node_parameters")
    void setCommand_aliases(Node<CommandSender> node) {
        Command command = val -> 0;
        
        node.setCommand(command);
        
        assertEquals(command, ((CommandNode<?>) node).getCommand());
        assertEquals(command, node.aliases().get(0).getCommand());
    }
    
    
    @ParameterizedTest
    @MethodSource("node_parameters")
    void setRedirect_aliases(Node<CommandSender> node) {
        var destination = Literal.of("child").build();
        
        node.setRedirect(destination);
        
        assertEquals(destination, ((CommandNode<?>) node).getRedirect());
        assertEquals(destination, node.aliases().get(0).getRedirect());
    }
    
    
    @ParameterizedTest
    @MethodSource("node_parameters")
    void optionally(Node<CommandSender> node) {
        var command = (CommandNode<?>) node;

        assertEquals(2, command.getChildren().size());
        assertNotNull(command.getChild("optional"));
        assertNotNull(command.getChild("optional child"));
    }
    
    
    static Stream<Node<CommandSender>> node_parameters() {
        var optional = Literal.of("optional").then(Literal.of("optional child"));
        return Stream.of(
            Literal.of("literal").executes(val -> {}).alias("literal alias").alias("a", "b").optionally(optional).build(),
            Argument.of("argument", StringArgumentType.word()).executes(val -> {}).alias("argument alias").alias("a", "b").optionally(optional).build()
        );
    }

} 
