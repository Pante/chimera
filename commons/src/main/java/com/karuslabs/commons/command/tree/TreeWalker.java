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

/**
 * This class recursively maps a command and its children of type {@code T} to type
 * {@code R}. Traversal of the children and redirected commands is handled by this
 * {@code TreeWalker} while the mapping of individual commands is forwarded to a {@link Mapper}.
 * 
 * @param <T> the type of a given command
 * @param <R> the resultant type of a mapped command
 */
public class TreeWalker<T, R> {    
    
    private final Mapper<T, R> mapper;
    private final Map<CommandNode<T>, CommandNode<R>> mappings = new IdentityHashMap<>();
    
    /**
     * Creates a {@code TreeWalker} with the given underlying {@code Mapper}.
     * 
     * @param mapper the mapper used to map individual commands
     */
    public TreeWalker(Mapper<T, R> mapper) {
        this.mapper = mapper;
    }
    
    /**
     * Recursively maps the given commands to {@code root}. If present, the command
     * is removed from {@code root} before it is mapped. Commands that could not be
     * mapped will not be re-added. The requirement of a command in the given commands
     * is ignored.
     * 
     * @param root the root
     * @param commands the commands to be mapped
     */
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
    
    /**
     * Recursively maps the given commands that the source is permitted to use
     * and satisfies requirement to the root.
     * 
     * @param root the root
     * @param commands the commands to be mapped
     * @param source the source
     * @param requirement the requirement
     */
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

    /**
     * Recursively maps the command and its children that the caller is 
     * permitted to use. All commands are permitted if the given caller is {@code null}.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Returns {@code null} immediately if the source is not permitted to use the 
     * command. If not present, the command is then mapped and added to {@code mappings}
     * before proceeding to avoid infinite recursion. Mapping of redirected commands 
     * and the children are then forwarded to {@link #redirect(CommandNode, CommandNode, Object)} 
     * and {@link #descend(Collection, CommandNode, Object)} respectively.
     * 
     * Caution should be exercised as different {@code mapping} implementations 
     * will affect the resultant mapped command. Identity-based maps, i.e.
     * {@code IdentityHashMap} will prevent commands with conflicting names from
     * being overridden while equality-based maps, i.e. {@code HashMap} will not.
     * 
     * @param command the command to be mapped
     * @param source the source, or {@code null} if all commands are permitted
     * @return the mapped command, or {@code null} if the caller is not permitted to use
     *         the given {@code command}
     */
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
    
    /**
     * Recursively maps the destination and redirects the result to the mapped
     * command. Does nothing if either destination is {@code null} or result is not 
     * a {@link Mutable}.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the destination is forwarded to {@link #map(CommandNode, Object)}.
     * 
     * @param destination the destination of the redirected command
     * @param result the result to be redirected
     * @param source the source
     */
    protected void redirect(@Nullable CommandNode<T> destination, CommandNode<R> result, @Nullable T source) {
        if (destination != null && result instanceof Mutable<?>) {
            ((Mutable<R>) result).setRedirect(map(destination, source));
        }
    }
    
    /**
     * Recursively maps each command in children and adds them to the command if 
     * the source is permitted to use the command.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the children is forwarded to {@link #map(CommandNode, Object)}.
     * 
     * @param children the child to be mapped
     * @param command the command
     * @param source the source
     */
    protected void descend(Collection<CommandNode<T>> children, CommandNode<R> command, @Nullable T source) {
        for (var child : children) {
            var result = map(child, source);
            if (result != null) {
                command.addChild(result);
            }
        }
    }
    
}
