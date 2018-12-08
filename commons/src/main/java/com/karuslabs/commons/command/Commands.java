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

import com.karuslabs.commons.command.tree.*;
import com.karuslabs.annotations.Static;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;

import java.util.Map;


public @Static class Commands {

    private static final VarHandle COMMAND = find("command", Command.class);
    private static final VarHandle CHILDREN = find("children", Map.class);
    private static final VarHandle LITERALS = find("literals", Map.class);
    private static final VarHandle ARGUMENTS = find("arguments", Map.class);
    
    
    private static VarHandle find(String name, Class<?> type) {
        try {
            return MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup()).findVarHandle(CommandNode.class, name, type);
            
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    
    public static <S> CommandNode<S> alias(CommandNode<S> command, String alias) {
        if (command instanceof Argument<?, ?>) {
            return alias((Argument<S, ?>) command, alias);
            
        } else if (command instanceof ArgumentCommandNode<?, ?>) {
            return alias((ArgumentCommandNode<S, ?>) command, alias);
            
        } else if (command instanceof Literal<?>) {
            return alias((Literal<S>) command, alias);
            
        } else if (command instanceof LiteralCommandNode<?>) {
            return alias((LiteralCommandNode<S>) command, alias);
            
        } else {
            throw new UnsupportedOperationException("Unsupported commnd, '" + command.getName() + "' of type: " + command.getClass().getName());
        }
    }
    
    
    public static <S, T> Argument<S, T> alias(Argument<S, T> command, String alias) {
        var argument = alias((ArgumentCommandNode<S, T>) command, alias);
        command.aliases().add(argument);
        
        return argument;
    }
    
    public static <S, T> Argument<S, T> alias(ArgumentCommandNode<S, T> command, String alias) {
        var argument = new Argument<>(alias, command.getType(), command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork(), command.getCustomSuggestions());
        for (var child : command.getChildren()) {
            argument.addChild(child);
        }
        
        return argument;
    }
    
    
    public static <S> Literal<S> alias(Literal<S> command, String alias) {
        var literal = alias((LiteralCommandNode<S>) command, alias);
        command.aliases().add(literal);
        
        return literal;
    }
    
    public static <S> Literal<S> alias(LiteralCommandNode<S> command, String alias) {
        var literal = new Literal<>(alias, command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());
        for (var child : command.getChildren()) {
            literal.addChild(child);
        }
        
        return literal;
    }
    
    
    public static <S> void set(CommandNode<S> command, Command<S> execution) {
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
