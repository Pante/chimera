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

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
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

    
    protected CommandNode<R> argument(CommandNode<T> command) {
        var argument = (ArgumentCommandNode<T, ?>) command;
        return new Argument<>(argument.getName(), argument.getType(), execution(argument), requirement(argument), suggestions(argument));
    }

    protected CommandNode<R> literal(CommandNode<T> command) {
        return new Literal<>(command.getName(), execution(command), requirement(command));
    }

    protected CommandNode<R> root(CommandNode<T> command) {
        return new RootCommandNode<>();
    }

    protected CommandNode<R> otherwise(CommandNode<T> command) {
        throw new UnsupportedOperationException("Unsupported command, '" + command.getName() + "' of type: " + command.getClass().getName());
    }


    protected Command<R> execution(CommandNode<T> command) {
        return (Command<R>) NONE;
    }

    protected Predicate<R> requirement(CommandNode<T> command) {
        return (Predicate<R>) TRUE;
    }

    protected @Nullable SuggestionProvider<R> suggestions(ArgumentCommandNode<T, ?> command) {
        return null;
    }
    
}
