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
    
    
    public TreeWalker(Mapper<T, R> mapper) {
        this.mapper = mapper;
    }
    
    
    public void prune(RootCommandNode<R> root, Collection<? extends CommandNode<T>> commands) {
        for (var child : commands) {
            Commands.remove(root, child.getName());
            var mapped = map(child, null, new IdentityHashMap<>());
            if (mapped != null) {
                root.addChild(mapped);
            }
        }
    }
    
    public void add(RootCommandNode<R> root, Collection<? extends CommandNode<T>> commands, T source, Predicate<CommandNode<T>> requirement) {
        for (var command : commands) {
            if (requirement.test(command)) {
                var mapped = map(command, source, new IdentityHashMap<>());
                if (mapped != null) {
                    root.addChild(mapped);
                }
            }
        }
    }

    
    protected @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T source, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (source != null && command.getRequirement() != null && !command.canUse(source)) {
            return null;
        }
        
        var target = mappings.get(command);
        if (target == null) {
            target = mapper.map(command);
            mappings.put(command, target);
            
            redirect(command, target, source, mappings);
            descend(command, target, source, mappings);
        }

        return target;
    }
    
    protected void redirect(CommandNode<T> command, CommandNode<R> target, @Nullable T source, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (command.getRedirect() != null && target instanceof Mutable<?>) {
            var destination = map(command.getRedirect(), source, mappings);
            ((Mutable<R>) target).setRedirect(destination);
        }
    }
    
    protected void descend(CommandNode<T> command, CommandNode<R> target, @Nullable T source, Map<CommandNode<T>, CommandNode<R>> mappings) {
        for (var child : command.getChildren()) {
            var mapped = map(child, source, mappings);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
}
