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
package com.karuslabs.commons.command.annotations.old;

import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collections.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public class CommandAssembler<T> {
    
    public void assemble(Object object, RootCommandNode<T> root, Literal[] literals, @Nullable Command<T> execution) {
        for (var literal : literals) {
            var namespace = literal.namespace();
            var leaf = traverse(object, root, namespace, "Literal");
            
            leaf.addChild(literal(namespace[namespace.length - 1]).alias(literal.aliases()).executes(execution).build());
        }
    }  
    
    private com.karuslabs.commons.command.tree.nodes.Literal.Builder<T> literal(String name) {
        return com.karuslabs.commons.command.tree.nodes.Literal.<T>builder(name);
    }
    
    
    public void assemble(Object object, TokenMap<String, Object> bindings, RootCommandNode<T> root, Argument[] arguments, @Nullable Command<T> execution) {
        for (var literal : arguments) {
            var namespace = literal.namespace();
            var leaf = traverse(object, root, namespace, "Argument");
            
            var name = namespace[namespace.length - 1];
            var type = bindings.get(name, ArgumentType.class);
                    
            leaf.addChild(argument(name, type).alias(literal.aliases()).suggests(bindings.get(name, SuggestionProvider.class)).executes(execution).build());
        }
    }    
    
    private com.karuslabs.commons.command.tree.nodes.Argument.Builder<T, ?> argument(String name, ArgumentType<Object> type) {
        return com.karuslabs.commons.command.tree.nodes.Argument.<T, Object>builder(name, type);
    }
    
    
    protected CommandNode<T> traverse(Object object, RootCommandNode<T> root, String[] namespace, String annotation) {
        if (namespace.length == 0) {
            throw new IllegalArgumentException("@" + annotation + " namespace in " + object.getClass() + " cannot be empty");
        }
        
        CommandNode<T> command = root;
        for (int i = 0; i <= namespace.length; i++) {
            var child = command.getChild(namespace[i]);
            if (child == null) {
                child = literal(namespace[i]).build();
                command.addChild(child);
            }
            
            command = child;
        }
        
        return command;
    }

}
