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
package com.karuslabs.commons.command.annotations.old.assembler;

import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collection.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public class CommandAssembler<T> {
    
    TokenMap<String, Object> bindings;
    Map<String, Node<T>> nodes;
    
    
    public CommandAssembler(TokenMap<String, Object> bindings, Map<String, Node<T>> nodes) {
        this.bindings = bindings;
        this.nodes = nodes;
    }
    
    
    public Map<String, CommandNode<T>> assemble() {
        var commands = new HashMap<String, CommandNode<T>>();
        for (var node : nodes.values()) {
            commands.put(node.name(), map(node));
        }
        
        return commands;
    }
    
    protected CommandNode<T> map(Node<T> node) {
        var command = node.get();
        if (command == null) {
            command = literal(node.name()).build();
        }
        
        for (var child : node.children().values()) {
            command.addChild(map(child));
        }
        
        return command;
    }
    
    
    public void assemble(Class<?> type, Literal[] literals, @Nullable Command<T> execution) {
        for (var literal : literals) {
            var namespace = literal.namespace();
            var node = descend(type, "Literal", namespace);
            
            node.set(literal(namespace[namespace.length - 1]).alias(literal.aliases()).executes(execution).build());
        }
    }
    
    
    public void assemble(Class<?> type, Argument[] arguments, @Nullable Command<T> execution) {
        for (var argument : arguments) {
            var namespace = argument.namespace();
            var node = descend(type, "Argument", namespace);
            var name = namespace[namespace.length - 1];
      
            node.set(argument(name, bindings.get(argument.type().isEmpty() ? name : argument.type(), ArgumentType.class))
                    .suggests(bindings.get(argument.suggestions(), SuggestionProvider.class))
                    .executes(execution).build()
            );
        }
    }
    
    
    protected Node<T> descend(Class<?> type, String annotation, String... namespace) {
        if (namespace.length == 0) {
            throw new IllegalArgumentException("Invalid namespace for @" + annotation + " in " + type);
        }
        
        Node<T> node = null;
        var commands = this.nodes;
        
        for (var name : namespace) {
            node = commands.get(name);
            if (node == null) {
                node = new Node<>(name);
                commands.put(name, node);
            }
            
            commands = node.children();
        }
        
        return node;
    }

    
    private com.karuslabs.commons.command.tree.nodes.Literal.Builder<T> literal(String name) {
        return com.karuslabs.commons.command.tree.nodes.Literal.<T>builder(name);
    }
    
    private com.karuslabs.commons.command.tree.nodes.Argument.Builder<T, ?> argument(String name, ArgumentType<Object> type) {
        return com.karuslabs.commons.command.tree.nodes.Argument.<T, Object>builder(name, type);
    }
    
}
