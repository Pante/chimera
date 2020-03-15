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

import com.karuslabs.annotations.VisibleForOverride;
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Mapper<T, R> {
    
    public static final Command<?> NONE = context -> 0;
    public static final Predicate<?> TRUE = source -> true;
    public static final SuggestionProvider<?> EMPTY = (suggestions, builder) -> builder.buildFuture();
    
    
    public CommandNode<R> map(CommandNode<T> command) {
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

    
    @VisibleForOverride
    protected CommandNode<R> argument(CommandNode<T> command) {
        var parameter = (ArgumentCommandNode<T, ?>) command;
        return new Argument<>(parameter.getName(), type(parameter), execution(parameter), requirement(parameter), suggestions(parameter));
    }

    @VisibleForOverride
    protected CommandNode<R> literal(CommandNode<T> command) {
        return new Literal<>(command.getName(), execution(command), requirement(command));
    }

    @VisibleForOverride
    protected CommandNode<R> root(CommandNode<T> command) {
        return new RootCommandNode<>();
    }

    @VisibleForOverride
    protected CommandNode<R> otherwise(CommandNode<T> command) {
        throw new IllegalArgumentException("Unsupported command, '" + command.getName() + "' of type: " + command.getClass().getName());
    }
    
    
    @VisibleForOverride
    protected ArgumentType<?> type(ArgumentCommandNode<T, ?> command) {
        return command.getType();
    }
    
    @VisibleForOverride
    protected Command<R> execution(CommandNode<T> command) {
        return (Command<R>) NONE;
    }

    @VisibleForOverride
    protected Predicate<R> requirement(CommandNode<T> command) {
        return (Predicate<R>) TRUE;
    }

    @VisibleForOverride
    protected @Nullable SuggestionProvider<R> suggestions(ArgumentCommandNode<T, ?> command) {
        return null;
    }
    
}
