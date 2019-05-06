/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command;

import com.karuslabs.annotations.Static;
import com.karuslabs.commons.command.annotations.assembler.Assembler;
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * This class consists exclusively of static methods that modify {@code CommandNode}s.
 * <br><br>
 * <b>Implementation details:</b><br>
 * {@code VarHandle}s are used to manipulate the fields in a {@code CommandNode}.
 * Hence, an {@code ExceptionInInitializerError} will be thrown if strong module
 * encapsulation is enabled.
 */
public @Static class Commands {
    
    static final Assembler<?> ASSEMBLER = new Assembler<>();
    static final VarHandle COMMAND = field("command", Command.class);
    static final VarHandle CHILDREN = field("children", Map.class);
    static final VarHandle LITERALS = field("literals", Map.class);
    static final VarHandle ARGUMENTS = field("arguments", Map.class);
    
    
    static VarHandle field(String name, Class<?> type) {
        try {
            return MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup()).findVarHandle(CommandNode.class, name, type);
            
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    
    /**
     * Returns a {@code CommandNode} with the given name, created from the annotated
     * object.
     * 
     * @see com.karuslabs.commons.command.annotations
     * 
     * @param <T> the type of the source
     * @param annotated the annotated object
     * @param name the name of the command
     * @return a {@code CommandNode} with given name, created from the annotated
     *         object
     */
    public static <T> CommandNode<T> from(Object annotated, String name) {
        return Commands.<T>from(annotated).get(name);
    }
    
    
    /**
     * Returns a map that associates root commands created from the annotated object
     * with their names.
     * 
     * @see com.karuslabs.commons.command.annotations
     * 
     * @param <T> the type of the source
     * @param annotated the annotated object
     * @return a map that associates the created root commands with their names
     */
    public static <T> Map<String, CommandNode<T>> from(Object annotated) {
        return (Map<String, CommandNode<T>>) ASSEMBLER.assemble(annotated);
    }

    
    /**
     * Copies the given {@code command} with {@code alias} as the name. In addition,
     * the copy will be added as an alias to the given {@code command} if it implements 
     * {@link Aliasable}.
     * 
     * @param <T> the type of the source
     * @param command the command to be copied
     * @param alias the name of the copy
     * @return a copy of {@code command} with {@code alias} as the name
     */
    public static <T> Literal<T> alias(LiteralCommandNode<T> command, String alias) {
        var literal = new Literal<>(alias, new ArrayList<>(0), true, command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());
 
        for (var child : command.getChildren()) {
            literal.addChild(child);
        }
        
        if (command instanceof Aliasable<?>) {
            ((Aliasable<T>) command).aliases().add(literal);
        }
        
        return literal;
    }
    
    
    /**
     * Sets the {@code execution} which the given {@code command} is to execute.
     * 
     * @param <T> the type of the source
     * @param command the command
     * @param execution the {@code execution} which {@code command} is to execute
     */
    public static <T> void executes(CommandNode<T> command, Command<T> execution) {
        COMMAND.set(command, execution);
    }
    
    
    
    /**
     * Removes the child from the given {@code command} if present. In addition,
     * the aliases of the child will also be removed if it implements {@link Aliasable}.
     * 
     * @param <T> the type of the source
     * @param command the command which child is to be removed
     * @param child the name of the child to be removed
     * @return the child that was removed, or {@code null} if no child with the 
     *         given name exists
     */
    public static <T> @Nullable CommandNode<T> remove(CommandNode<T> command, String child) {
        var commands = (Map<String, CommandNode<T>>) CHILDREN.get(command);
        var literals = (Map<String, LiteralCommandNode<T>>) LITERALS.get(command);
        var arguments = (Map<String, ArgumentCommandNode<T, ?>>) ARGUMENTS.get(command);

        
        var removed = commands.remove(child);
        if (removed == null) {
            return null;
        }
        
        literals.remove(child);
        arguments.remove(child);
        
        if (removed instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<?>) removed).aliases()) {
                commands.remove(alias.getName());
                literals.remove(child);
                arguments.remove(child);
            }
        }

        return removed;
    }

    /**
     * Remove the children from the given {@code command} if present. In addition,
     * the aliases of the children will also be removed if it implements {@link Aliasable}.
     * 
     * @param <T> the type of the source
     * @param command the command which children are to be removed
     * @param children the names of the children to be removed
     * @return {@code true} if all children with the given names were removed; otherwise
     *         {@code false}
     */
    public static <T> boolean remove(CommandNode<T> command, String... children) {
        var commands = (Map<String, CommandNode<T>>) CHILDREN.get(command);
        var literals = (Map<String, LiteralCommandNode<T>>) LITERALS.get(command);
        var arguments = (Map<String, ArgumentCommandNode<T, ?>>) ARGUMENTS.get(command);
        
        var all = true;
        for (var child : children) {
            var removed = commands.remove(child);
            if (removed == null) {
                all = false;
                continue;
            }
            
            literals.remove(child);
            arguments.remove(child);
            
            if (removed instanceof Aliasable<?>) {
                for (var alias : ((Aliasable<?>) removed).aliases()) {
                    commands.remove(alias.getName());
                    literals.remove(child);
                    arguments.remove(child);
                }
            }
        }
        
        return all;
    }
    
}
