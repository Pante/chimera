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
package com.karuslabs.commons.command.tree;

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.tree.Trees.Functor.TRUE;


public @Static class Trees {
    
    private static final Functor<?, ?> FUNCTOR = new Functor<>();
    
    
    public static <S, T> void map(CommandNode<S> source, CommandNode<T> target) {
        map(source, target, null);
    }
    
    public static <S, T> void map(CommandNode<S> source, CommandNode<T> target, @Nullable S caller) {
        map(source, target, caller, functor());
    }
    
    public static <S, T> void map(CommandNode<S> source, CommandNode<T> target, @Nullable S caller, Functor<S, T> functor) {
        map(source, target, caller, functor(), (Predicate<CommandNode<S>>) TRUE);
    }
    
    public static <S, T> void map(CommandNode<S> source, CommandNode<T> target, @Nullable S caller, Functor<S, T> functor, Predicate<CommandNode<S>> requirement) {
        for (var child : source.getChildren()) {
            if (requirement.test(child)) {
                var mapped = functor.map(child, caller);
                if (mapped != null) {
                    target.addChild(mapped);
                }
            }
        }
    }
    
    
    public static <S, T> Functor<S, T> functor() {
        return (Functor<S, T>) FUNCTOR;
    }
    
    
    public static class Functor<S, T> {

        public static final Command<?> NONE = context -> 0;
        public static final Predicate<?> TRUE = source -> true;
        public static final SuggestionProvider<?> EMPTY = (suggestions, builder) -> builder.buildFuture();

        
        public @Nullable CommandNode<T> map(CommandNode<S> command) {
            return map(command, null);
        }

        public @Nullable CommandNode<T> map(CommandNode<S> command, @Nullable S caller) {
            return map(command, caller, new IdentityHashMap<>());
        }

        public @Nullable CommandNode<T> map(CommandNode<S> command, @Nullable S caller, Map<CommandNode<S>, CommandNode<T>> commands) {
            if (caller != null && command.getRequirement() != null && !command.canUse(caller)) {
                return null;
            }

            var target = commands.get(command);
            if (target != null) {
                return target;
            }

            target = copy(command);
            commands.put(command, target);

            if (command.getRedirect() != null) {
                var destination = map(command.getRedirect(), caller, commands);
                if (target instanceof Mutable<?>) {
                    ((Mutable<T>) target).setRedirect(destination);
                }
            }

            for (var child : command.getChildren()) {
                var leaf = map(child, caller, commands);
                if (leaf != null) {
                    target.addChild(leaf);
                }
            }

            return target;
        }

        
        public CommandNode<T> copy(CommandNode<S> command) {
            if (command instanceof ArgumentCommandNode<?, ?>) {
                return argument(command);

            } else if (command instanceof LiteralCommandNode<?>) {
                return literal(command);

            } else if (command instanceof RootCommandNode<?>) {
                return root(command);

            } else {
                return otherwise(command);
            }
        }

        protected CommandNode<T> argument(CommandNode<S> command) {
            var argument = (ArgumentCommandNode<S, ?>) command;
            return new Argument<>(argument.getName(), argument.getType(), execution(argument), requirement(argument), null);
        }

        protected CommandNode<T> literal(CommandNode<S> command) {
            return new Literal<>(command.getName(), execution(command), requirement(command));
        }

        protected CommandNode<T> root(CommandNode<S> command) {
            return new RootCommandNode<>();
        }

        protected CommandNode<T> otherwise(CommandNode<S> command) {
            throw new UnsupportedOperationException("Unsupported command, '" + command.getName() + "' of type: " + command.getClass().getName());
        }

        
        protected Command<T> execution(CommandNode<S> command) {
            return (Command<T>) NONE;
        }

        protected Predicate<T> requirement(CommandNode<S> command) {
            return (Predicate<T>) TRUE;
        }

        protected SuggestionProvider<T> suggestions(ArgumentCommandNode<S, ?> command) {
            return (SuggestionProvider<T>) EMPTY;
        }

    }
    
}
