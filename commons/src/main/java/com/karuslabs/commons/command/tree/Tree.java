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

import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;
import com.karuslabs.commons.command.tree.nodes.Mutable;


public class Tree<T, R> {    
    
    private Mapper<T, R> mapper;
    
    
    public Tree(Mapper<T, R> mapper) {
        this.mapper = mapper;
    }
    
    
    public void truncate(RootCommandNode<T> source, RootCommandNode<R> target) {
        for (var child : source.getChildren()) {
            Commands.remove(target, child.getName());
            var mapped = map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
    public void map(RootCommandNode<T> source, RootCommandNode<R> target, T caller, Predicate<CommandNode<T>> requirement) {
        for (var child : source.getChildren()) {
            if (requirement.test(child)) {
                var mapped = map(child, caller);
                if (mapped != null) {
                    target.addChild(mapped);
                }
            }
        }
    }
    
    
    public @Nullable CommandNode<R> map(CommandNode<T> command) {
        return map(command, null);
    }
    
    public @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T caller) {
        return map(command, caller, new IdentityHashMap<>());
    }
    
    public @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (caller != null && command.getRequirement() != null && !command.canUse(caller)) {
            return null;
        }
        
        var target = mappings.get(command);
        if (target == null) {
            target = mapper.map(command);
            mappings.put(command, target);
            
            redirect(command, target, caller, mappings);
            descend(command, target, caller, mappings);
        }

        return target;
    }
    
    protected void redirect(CommandNode<T> command, CommandNode<R> target, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (command.getRedirect() != null && target instanceof Mutable<?>) {
            var destination = map(command.getRedirect(), caller, mappings);
            ((Mutable<R>) target).setRedirect(destination);
        }
    }
    
    protected void descend(CommandNode<T> command, CommandNode<R> target, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        for (var child : command.getChildren()) {
            var mapped = map(child, caller, mappings);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
}
