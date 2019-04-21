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
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public @Static class Commands {

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

    
    public static <T> Literal<T> alias(LiteralCommandNode<T> command, String alias) {
        var literal = new Literal<>(alias, new ArrayList<>(0), command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());
 
        for (var child : command.getChildren()) {
            literal.addChild(child);
        }
        
        if (command instanceof Aliasable<?>) {
            ((Aliasable<T>) command).aliases().add(literal);
        }
        
        return literal;
    }
    
    
    public static <T> void executes(CommandNode<T> command, Command<T> execution) {
        COMMAND.set(command, execution);
    }
    
    
    public static <T> Map<String, CommandNode<T>> children(CommandNode<T> command) {
        return (Map<String, CommandNode<T>>) CHILDREN.get(command);
    }
    
    public static <T> void children(CommandNode<T> command, Map<String, CommandNode<T>> children) {
        CHILDREN.set(command, children);
    }
    
        
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
        
        var aliases = command instanceof Aliasable<?> ? ((Aliasable<T>) command).aliases() : null;
        if (removed instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<?>) removed).aliases()) {
                commands.remove(alias.getName());
                literals.remove(child);
                arguments.remove(child);
                
                if (aliases != null) {
                    for (var commandAlias : aliases) {
                        // TODO
                    }
                }
            }
        }

        return removed;
    }

    public static <T> boolean remove(CommandNode<T> command, String... children) {
        // TODO REWRITE METHOD
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
