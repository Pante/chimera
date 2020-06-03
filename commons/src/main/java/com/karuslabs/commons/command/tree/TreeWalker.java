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

import com.karuslabs.commons.command.Commands;
import com.karuslabs.commons.command.tree.nodes.Mutable;

import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public class TreeWalker<T, R> {    
    
    private final Mapper<T, R> mapper;
    protected final Map<CommandNode<T>, CommandNode<R>> mappings;
    
    
    public TreeWalker(Mapper<T, R> mapper) {
        this.mapper = mapper;
        this.mappings = new IdentityHashMap<>();
    }
    
    
    public void prune(RootCommandNode<R> root, Collection<? extends CommandNode<T>> commands) {
        for (var child : commands) {
            Commands.remove(root, child.getName());
            var result = map(child, null);
            if (result != null) {
                root.addChild(result);
            }
        }
        
        mappings.clear();
    }
    
    public void add(RootCommandNode<R> root, Collection<? extends CommandNode<T>> commands, T source, Predicate<CommandNode<T>> requirement) {
        for (var command : commands) {
            if (requirement.test(command)) {
                var result = map(command, source);
                if (result != null) {
                    root.addChild(result);
                }
            }
        }
        
        mappings.clear();
    }

    
    protected @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T source) {
        if (source != null && command.getRequirement() != null && !command.canUse(source)) {
            return null;
        }
        
        var result = mappings.get(command);
        if (result == null) {
            result = mapper.map(command);
            mappings.put(command, result);
            
            redirect(command.getRedirect(), result, source);
            descend(command.getChildren(), result, source);
        }

        return result;
    }
    
    protected void redirect(@Nullable CommandNode<T> destination, CommandNode<R> result, @Nullable T source) {
        if (destination != null && result instanceof Mutable<?>) {
            ((Mutable<R>) result).setRedirect(map(destination, source));
        }
    }
    
    protected void descend(Collection<CommandNode<T>> children, CommandNode<R> command, @Nullable T source) {
        for (var child : children) {
            var result = map(child, source);
            if (result != null) {
                command.addChild(result);
            }
        }
    }
    
}
