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

import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public @Static class Trees {
            
    private static final Factory<?, ?> FACTORY = new Factory();
    
    
    public static <S, T> void map(CommandNode<S> command, CommandNode<T> target, Factory<S, T> factory) {
        map(command, target, factory, null);
    }
    
    public static <S, T> void map(CommandNode<S> command, CommandNode<T> target, @Nullable S sender) {
        map(command, target, factory(), sender);
    }
    
    public static <S, T> void map(CommandNode<S> command, CommandNode<T> target, Factory<S, T> factory, @Nullable S sender) {
        var commands = new IdentityHashMap<CommandNode<S>, CommandNode<T>>();
        commands.put(command, target);
        
        map(command, factory, commands, sender);
    }
    
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command, Factory<S, T> factory) {
        return map(command, factory, null);
    }
        
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, @Nullable S sender) {
        return map(command, factory(), sender);
    }
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, Factory<S, T> factory, @Nullable S sender) {
        return map(command, factory, new IdentityHashMap<>(), sender);
    }
    
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, Factory<S, T> factory, Map<CommandNode<S>, CommandNode<T>> commands, @Nullable S sender) {
        if (sender != null && command.getRequirement() != null && !command.canUse(sender)) {
            return null;
        }
        
        CommandNode<T> target = commands.get(command);
        
        if (command.getRedirect() == null) {
            target = target == null ? factory.from(command) : target;
            for (var child : command.getChildren()) {
                var leaf = find(child, factory, commands, sender);
                if (leaf != null) {
                    target.addChild(leaf);
                }
            }
            
        } else {
            target = target == null ? factory.from(command, find(command.getRedirect(), factory, commands, sender)) : target;
        }
        
        return target;
    }
    
    private static <S, T> @Nullable CommandNode<T> find(CommandNode<S> command, Factory<S, T> factory, Map<CommandNode<S>, CommandNode<T>> commands, @Nullable S sender) {
        var target = commands.get(command);
        if (target == null) {
            target = map(command, factory, commands, sender);
            commands.put(command, target);
        }
        
        return target;
    }
    
    
    public static <S, T> Factory<S, T> factory() {
        return (Factory<S, T>) FACTORY;
    }
    
    public static class Factory<S, T> {
        
        public static final Predicate<?> TRUE = s -> true;
        
        
        public CommandNode<T> from(CommandNode<S> command) {
            return from(command, null);
        }
        
        public CommandNode<T> from(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            if (command instanceof ArgumentCommandNode) {
                return argument(command, destination);

            } else if (command instanceof LiteralCommandNode) {
                return literal(command, destination);

            } else if (command instanceof RootCommandNode) {
                return root(command, destination);

            } else {
                return otherwise(command, destination);
            }
        }
        
        protected CommandNode<T> argument(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            var type = ((ArgumentCommandNode<?, ?>) command).getType();
            return new ArgumentCommandNode<>(command.getName(), type, null, requirement(command), destination, null, false, null);
        }

        protected CommandNode<T> literal(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            return new LiteralCommandNode<>(command.getName(), null, requirement(command), destination, null, false);
        }

        protected CommandNode<T> root(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            return new RootCommandNode<>();
        }

        protected CommandNode<T> otherwise(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            throw new UnsupportedOperationException("Unsupported node \"" + command.getName() + "\" of type: " + command.getClass().getName());
        }
        
        
        protected Predicate<T> requirement(CommandNode<S> command) {
            return (Predicate<T>) TRUE;
        }
        
    }
    
}
