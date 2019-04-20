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
import com.mojang.brigadier.tree.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public class CommandAssembler<T> {
    
    CommandNode<T> container;
    TokenMap<String, Object> bindings;
    
    
    public CommandAssembler(RootCommandNode<T> container, TokenMap<String, Object> bindings) {
        this.container = container;
        this.bindings = bindings;
    }
    
    
    public void assemble(Class<?> type, Literal[] literals, @Nullable Command<T> execution) {
        for (var literal : literals) {
            var namespace = literal.namespace();
            var command = descend(type, "Literal", namespace);
            
            command.addChild(literal(namespace[namespace.length - 1]).alias(literal.aliases()).executes(execution).build());
        }
    }

    
    public void assemble(Class<?> type, Argument[] arguments, @Nullable Command<T> execution) {
        for (var argument : arguments) {
            var namespace = argument.namespace();
            var command = descend(type, "Argument", namespace);
            var name = namespace[namespace.length - 1];

            command.addChild(argument(name, bindings.get(argument.type(), ArgumentType.class)).suggests(bindings.get(argument.suggestions(), SuggestionProvider.class)).executes(execution).build());
        }
    }
    
    
    protected CommandNode<T> descend(Class<?> type, String annotation, String[] namespace) {
        if (namespace.length == 0) {
            throw new IllegalArgumentException("Invalid namespace for: @" + annotation + " in " + type);
        }
        
        var command = container;
        for (int i = 0; i < namespace.length - 1; i++) {
            var child = command.getChild(namespace[i]);
            if (child == null) {
                child = literal(namespace[i]).build();
                command.addChild(child);
            }
            command = child;
        }
        
        return command;
    }

    
    private com.karuslabs.commons.command.tree.nodes.Argument.Builder<T, ?> argument(String name, ArgumentType<Object> type) {
        return com.karuslabs.commons.command.tree.nodes.Argument.<T, Object>builder(name, type);
    }
    
    private com.karuslabs.commons.command.tree.nodes.Literal.Builder<T> literal(String name) {
        return com.karuslabs.commons.command.tree.nodes.Literal.<T>builder(name);
    }
    
}
