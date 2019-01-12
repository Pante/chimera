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
    
    
    public static <T> CommandNode<T> alias(CommandNode<T> command, String alias) {
        if (command instanceof ArgumentCommandNode<?, ?>) {
            return alias((ArgumentCommandNode<T, ?>) command, alias);
            
        } if (command instanceof LiteralCommandNode<?>) {
            return alias((LiteralCommandNode<T>) command, alias);
            
        } else {
            throw new UnsupportedOperationException("Unsupported commnd, '" + command.getName() + "' of type: " + command.getClass().getName());
        }
    }
    
    public static <T, V> Argument<T, V> alias(ArgumentCommandNode<T, V> command, String alias) {
        var argument = new Argument<>(alias, command.getType(), command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork(), command.getCustomSuggestions());
        return alias(command, argument);
    }

    
    public static <T> Literal<T> alias(LiteralCommandNode<T> command, String alias) {
        var literal = new Literal<>(alias, new ArrayList<>(0), command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());
        return alias(command, literal);
    }
    
    
    static <Alias extends CommandNode<T>, T> Alias alias(CommandNode<T> command, Alias alias) {
        if (command instanceof Node<?>) {
            ((Node<T>) command).aliases().add(alias);
        }
        
        for (var child : command.getChildren()) {
            alias.addChild(child);
        }
        
        return alias;
    }
    
    
    public static <T> void executes(CommandNode<T> command, Command<T> execution) {
        COMMAND.set(command, execution);
    }
    
        
    public static <S> CommandNode<S> remove(CommandNode<S> command, String child) {
        var commands = (Map<String, CommandNode<S>>) CHILDREN.get(command);
        var literals = (Map<String, LiteralCommandNode<S>>) LITERALS.get(command);
        var arguments = (Map<String, ArgumentCommandNode<S, ?>>) ARGUMENTS.get(command);

        var removed = commands.remove(child);
        if (removed != null) {
            literals.remove(child);
            arguments.remove(child);
        }

        return removed;
    }

    public static <S> boolean remove(CommandNode<S> command, String... children) {
        var commands = (Map<String, CommandNode<S>>) CHILDREN.get(command);
        var literals = (Map<String, LiteralCommandNode<S>>) LITERALS.get(command);
        var arguments = (Map<String, ArgumentCommandNode<S, ?>>) ARGUMENTS.get(command);
        
        var all = true;
        for (var child : children) {
            var removed = commands.remove(child);
            if (removed != null) {
            literals.remove(child);
            arguments.remove(child);
                
            } else {
                all = false;
            }
        }
        
        return all;
    }
    
}
