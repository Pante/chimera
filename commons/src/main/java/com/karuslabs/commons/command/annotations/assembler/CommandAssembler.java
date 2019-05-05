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
import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * An assembler that creates {@code CommandNode}s from annotated objects.
 * 
 * @param <T> the type of the source
 */
public class CommandAssembler<T> {
    
    TokenMap<String, Object> bindings;
    Map<String, Node<T>> nodes;
    
    
    /**
     * Creates a {@code CommandAssembler} with the given bindings and nodes.
     * 
     * @param bindings the bindings
     * @param nodes the node
     */
    public CommandAssembler(TokenMap<String, Object> bindings, Map<String, Node<T>> nodes) {
        this.bindings = bindings;
        this.nodes = nodes;
    }
    
    
    /**
     * Creates {@code CommandNode}s from a previously created intermediate representation.
     * 
     * @return a map that associates the created root commands with the names of 
     *         the root commands
     * @throws IllegalArgumentException if the given object could not be resolved
     * @throws IllegalStateException if the given object could not be resolved
     * @throws RuntimeException if the given object could not be resolved
     */
    public Map<String, CommandNode<T>> assemble() {
        var commands = new HashMap<String, CommandNode<T>>();
        for (var node : nodes.values()) {
            commands.put(node.name(), map(node));
        }
        
        return commands;
    }
    
    /**
     * Creates a {@code CommandNode} from the given {@code Node}.
     * 
     * @param node the node
     * @return a {@code CommandNode}
     */
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
    
    
    /**
     * Creates a intermediate representation from the given {@code Literal}s.
     * 
     * @param type the enclosing type
     * @param literals the literals
     * @param execution the {@code Command} to be executed
     * @throws IllegalArgumentException if the namespace of an {@code Literal} is 
     *                                  empty
     */
    public void assemble(Class<?> type, Literal[] literals, @Nullable Command<T> execution) {
        for (var literal : literals) {
            var namespace = literal.namespace();
            var node = descend(type, "Literal", namespace);
            
            node.set(literal(namespace[namespace.length - 1]).alias(literal.aliases()).executes(execution).build());
        }
    }
    
    
    /**
     * Creates a intermediate representation from the given {@code Argument}s.
     * 
     * @param type the enclosing type
     * @param arguments the arguments
     * @param execution the {@code Command} to be executed
     * @throws IllegalArgumentException if the namespace of an {@code Argument} 
     *                                  is empty
     */
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
    
    
    /**
     * Traverses the intermediate representation tree following the given namespace,
     * creating {@code Node}s when necessary.
     * 
     * @param type the enclosing type
     * @param annotation the annotation
     * @param namespace the namespace
     * @return the leaf {@code Node}
     * @throws IllegalArgumentException if {@code namespace} is empty
     */
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
