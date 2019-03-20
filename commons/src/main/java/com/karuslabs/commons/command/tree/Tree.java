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
import com.karuslabs.commons.command.tree.nodes.Node;

import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A tree that recursively maps a given command and its children of type T to type
 * R. Traversal of the command trees and redirections is handled by this tree while
 * the mapping of each individual command is delegated to a underlying {@link Mapper}.
 * 
 * @param <T> the type of a given command
 * @param <R> the resultant type of a mapped command
 */
public class Tree<T, R> {    
    
    private Mapper<T, R> mapper;
    
    
    /**
     * Constructs a {@code Tree} with the given {@code Mapper} to map each individual
     * command.
     * 
     * @param mapper the mapper to map each individual command
     */
    public Tree(Mapper<T, R> mapper) {
        this.mapper = mapper;
    }
    
    
    /**
     * Removes the children of the target if the source contains the children with
     * the same names before mapping the children of the source to the target; ignoring
     * the requirements of the commands.
     * 
     * @param source the source that contains the commands to be mapped
     * @param target the target that the source is to be mapped
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
     * Maps the children of the source to the target if the given requirement is
     * satisfied and the given caller is permitted to use the command.
     * 
     * @param source the source that contains the commands to be mapped
     * @param target the target that the source is to be mapped
     * @param caller the caller of the commands
     * @param requirement a requirement that must be satisfied for a command to
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
     * Returns the recursively mapped command which includes the children and redirections
     * using a {@code IdentityHashMap} to store prior mapped commands.
     * 
     * @param command the command to be mapped
     * @return the mapped command
     */
    public @Nullable CommandNode<R> map(CommandNode<T> command) {
        return map(command, null);
    }
    
    /**
     * Returns the recursively mapped command which includes the children and redirections
     * using a {@code IdentityHashMap} to store prior mapped commands, or null if 
     * the given caller is not permitted to use the command.
     * 
     * @param command the command to be mapped
     * @param caller the caller
     * @return the mapped command, or null if the caller is not permitted to use
     *         the command
     */
    public @Nullable CommandNode<R> map(CommandNode<T> command, @Nullable T caller) {
        return map(command, caller, new IdentityHashMap<>());
    }
    
    /**
     * Returns the recursively mapped command which includes the children and redirections,
     * or null if the given caller is not permitted to use the command.
     * <br><br>
     * Caution should be exercised when choosing the {@code Map} implementation to
     * store the mappings as the resultant mapped command will differ depending on
     * the chosen implementation. To avoid commands with conflicting names from being
     * overridden, a map implementation that does identity-based comparisons instead
     * of equality-based comparison, i.e. {@code IdentityHashMap} should be used.
     * Otherwise, if conflicting names are of no concern, a equality-based comparison
     * map implementation, i.e. {@code HashMap} should be used.
     * <br><br>
     * <b>Implementation details:</b><br>
     * This method first checks if the given caller is permitted to use the command,
     * and immediately returns null if not permitted. Subsequently if the given mappings
     * contains no mapping for the command, the command is mapped immediately and
     * added to the mappings before proceeding. This is done to prevent infinite
     * recursion. After which, mapping of redirected commands are delegated to {@link #redirect(CommandNode, CommandNode, Object, Map)}
     * and children of the given command delegated to {@link #descend(CommandNode, CommandNode, Object, Map)}.
     * 
     * @param command the command to be mapped
     * @param caller the caller
     * @param mappings the mappings of commands that have been mapped prior to the 
     *                 given command
     * @return the mapped command, or null if the caller is not permitted to use
     *         the command
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
     * Sets the destination of the given target to the mapped destination of the 
     * given redirected command; ignoring the given command if is not redirected 
     * or if target does not implement the {@link Node} interface.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the destination is delegated to {@link #map(CommandNode, Object, Map)}.
     * 
     * @param command the redirected command which destination to be mapped
     * @param target the target for which the destination is to be mapped
     * @param caller the caller
     * @param mappings the mappings of commands that have been mapped prior to the 
     *                 given command
     */
    protected void redirect(CommandNode<T> command, CommandNode<R> target, @Nullable T caller, Map<CommandNode<T>, CommandNode<R>> mappings) {
        if (command.getRedirect() != null && target instanceof Node<?>) {
            var destination = map(command.getRedirect(), caller, mappings);
            ((Node<R>) target).setRedirect(destination);
        }
    }
    
    /**
     * Maps the children of the source to the target if the given caller is permitted 
     * to use the command.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Mapping of the children are delegated to {@link #map(CommandNode, Object, Map)}.
     * 
     * @param command the command which children are to be mapped
     * @param target the target for which the children are to be mapped
     * @param caller the caller
     * @param mappings the mappings of commands that have been mapped prior to the 
     *                 given command
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
