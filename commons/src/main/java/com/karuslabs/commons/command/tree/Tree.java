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
 * {@code Tree} while the mapping of individual commands is forwarded to a {@link Mapper}.
 * 
 * @param <T> the type of a given command
 * @param <R> the resultant type of a mapped command
 */
public class Tree<T, R> {    
    
    private Mapper<T, R> mapper;
    
    
    /**
     * Creates a {@code Tree} with the given underlying {@code Mapper}.
     * 
     * @param mapper the mapper that maps individual commands
     */
    public Tree(Mapper<T, R> mapper) {
        this.mapper = mapper;
    }
    
    
    /**
     * Maps the children of the {@code source} to the {@code target}, removing the
     * child from the {@code target} before mapping if present. The {@code requirement}
     * of a child is ignored.
     * 
     * @param source the source that contains the children to be mapped
     * @param target the target that the children of the {@code source} are to be mapped
     */
    public void truncate(RootCommandNode<T> source, RootCommandNode<R> target) {
        for (var child : source.getChildren()) {
            Commands.remove(target, child.getName());
            var mapped = map(child);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
    /**
     * Maps each child of the {@code source} to the {@code target} if the {@code requirement} 
     * for the child is satisfied and the {@code caller} is permitted to use the
     * child.
     * 
     * @param source the source that contains the children to be mapped
     * @param target the target that the children of the {@code source} are to be mapped
     * @param caller the caller of the commands
     * @param requirement a requirement that must be satisfied for a child to
     *                    be mapped
     */
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
    
    
    /**
     * Recursively maps the command and its children using a {@code IdentityHashMap}
     * to lookup previously mapped commands.
     * 
     * @param command the command to be mapped
     * @return the mapped command
     */
    public @Nullable CommandNode<R> map(CommandNode<T> command) {
        return map(command, null);
    }
    
    /**
     * Recursively maps the command and its children that the {@code caller} is 
     * permitted to use, using a {@code IdentityHashMap} to lookup previously mapped
     * commands. All commands are permitted if {@code caller} is {@code null}.
     * 
     * @param command the command to be mapped
     * @param caller the caller, or {@code null} if all commands are permitted
     * @return the mapped command, or {@code null} if the caller is not permitted to use
     *         the given {@code command}
     */
    public @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T caller) {
        return map(command, caller, new IdentityHashMap<>());
    }
    
    /**
     * Recursively maps the command and its children that the {@code caller} is 
     * permitted to use, using {@code mappings} to lookup previously mapped commands. 
     * All commands are permitted if {@code caller} is {@code null}.
     * <br><br>
     * <b>Note:</b><br>
     * Caution should be exercised as different {@code mapping} implementations 
     * will affect the resultant mapped command. Identity-based maps, i.e.
     * {@code IdentityHashMap} will prevent commands with conflicting names from
     * being overridden while equality-based maps, i.e. {@code HashMap} will not.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Returns {@code null} immediately if the {@code caller} is not permitted to
     * use the command. If not present, the command is then mapped and added to 
     * {@code mappings} before proceeding to avoid infinite recursion. Mapping of
     * redirected commands and the children are then forwarded to 
     * {@link #redirect(CommandNode, CommandNode, Object, Map)} and 
     * {@link #descend(CommandNode, CommandNode, Object, Map)} respectively.
     * 
     * @param command the command to be mapped
     * @param caller the caller, or {@code null} if all commands are permitted
     * @param mappings the commands that have been mapped prior to the given command
     * @return the mapped command, or {@code null} if the caller is not permitted to use
     *         the given {@code command}
     */
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
    
    /**
     * Maps and sets the destination of the redirected {@code command} as the destination
     * of the {@code target}. Does nothing if the {@code command} is not redirected
     * or the {@code target} does not implement {@link Mutable}.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the destination is forwarded to {@link #map(CommandNode, Object, Map)}.
     * 
     * @param command the redirected command which destination to be mapped 
     * @param target the target for which the destination is to be set
     * @param caller the caller, or {@code null} if all commands are permitted
     * @param mappings the commands that have been mapped prior to the given command
     */
    protected void redirect(CommandNode<T> command, CommandNode<R> target, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (command.getRedirect() != null && target instanceof Mutable<?>) {
            var destination = map(command.getRedirect(), caller, mappings);
            ((Mutable<R>) target).setRedirect(destination);
        }
    }
    
    /**
     * Maps each child of the {@code command} to the {@code target} if the {@code caller} 
     * is permitted to use the command.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the children is forwarded to {@link #map(CommandNode, Object, Map)}.
     * 
     * @param command the command which children are to be mapped
     * @param target the target that the children of the {@code source} are to be mapped
     * @param caller the caller, or {@code null} if all commands are permitted
     * @param mappings the commands that have been mapped prior to the given command
     */
    protected void descend(CommandNode<T> command, CommandNode<R> target, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        for (var child : command.getChildren()) {
            var mapped = map(child, caller, mappings);
            if (mapped != null) {
                target.addChild(mapped);
            }
        }
    }
    
}
