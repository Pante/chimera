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
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A mapper that maps a command of type {@code T} to type {@code R} except for the 
 * children of the command. If the children of a command needs to be mapped too, 
 * a {@link Tree} should be used instead.
 *
 * @param <T> the type of a command
 * @param <R> the resultant type of the mapped command
 */
public class Mapper<T, R> {
    
    /**
     * An empty command that always returns {@code 0}.
     */
    public static final Command<?> NONE = context -> 0;
    /**
     * A predicate that is always {@code true}.
     */
    public static final Predicate<?> TRUE = source -> true;
    /**
     * A {@code SuggestionProvider} that always returns an empty {@code Suggestions}.
     */
    public static final SuggestionProvider<?> EMPTY = (suggestions, builder) -> builder.buildFuture();
    
    
    /**
     * Maps the given command to the resultant type.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Mapping of the different commands are forwarded to their respective methods.
     * <ul>
     * <li>{@code ArgumentCommandNode} to {@link #argument(CommandNode)}</li>
     * <li>{@code LiteralCommandNode} to {@link #literal(CommandNode)}</li>
     * <li>{@code RootCommand} to {@link #root(CommandNode)}</li>
     * <li>All other commands to {@link #otherwise(CommandNode)}</li>
     * </ul>
     * 
     * @param command the command to be mapped
     * @return the mapped command
     */
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

    
    /**
     * Maps the given argument to the resultant type.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Mapping of the different fields are forwarded to their respective methods.
     * <ul>
     * <li>{@code ArgumentType} to {@link #type(ArgumentCommandNode)}</li>
     * <li>{@code Command} to {@link #execution(CommandNode)}</li>
     * <li>{@code Predicate} to {@link #requirement(CommandNode)}</li>
     * <li>{@code SuggestionProvider} to {@link #suggestions(ArgumentCommandNode)}</li>
     * </ul>
     * 
     * @param command the argument to be mapped
     * @return the mapped {@code Argument}
     * @throws ClassCastException if the given {@code command} does not inherit
     *                            from {@code ArgumentCommandNode}
     */
    protected CommandNode<R> argument(CommandNode<T> command) {
        var argument = (ArgumentCommandNode<T, ?>) command;
        return new Argument<>(argument.getName(), type(argument), execution(argument), requirement(argument), suggestions(argument));
    }
    
    /**
     * Maps the given literal to the resultant type.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Mapping of the different fields are forwarded to their respective methods.
     * <ul>
     * <li>{@code Command} to {@link #execution(CommandNode)}</li>
     * <li>{@code Predicate} to {@link #requirement(CommandNode)}</li>
     * </ul>
     * 
     * @param command the literal to be mapped
     * @return the mapped {@code Literal}
     * @throws ClassCastException if the given {@code command} does not inherit
     *                            from {@code LiteralCommandNode}
     */
    protected CommandNode<R> literal(CommandNode<T> command) {
        return new Literal<>(command.getName(), execution(command), requirement(command));
    }
    
    /**
     * Maps the given command to the resultant type.
     * <br><br>
     * <b>Default implementation:</b><br>
     * A {@code RootCommandNode} is always created and returned.
     * 
     * @param command the command to be mapped
     * @return a {@code RootCommandNode}
     */
    protected CommandNode<R> root(CommandNode<T> command) {
        return new RootCommandNode<>();
    }
    
    /**
     * Maps the given command to the resultant type.
     * <br><br>
     * <b>Default implementation:</b><br>
     * A {@code UnsupportedOperationException} is thrown.
     * 
     * @param command the command to mapped
     * @return a mapped command
     * @throws UnsupportedOperationException if this operation is not overridden
     */
    protected CommandNode<R> otherwise(CommandNode<T> command) {
        throw new UnsupportedOperationException("Unsupported command, '" + command.getName() + "' of type: " + command.getClass().getName());
    }
    
    
    /**
     * Maps the {@code ArgumentType} of the given command.
     * <br><br>
     * <b>Default implementations:</b><br>
     * Returns the {@code ArgumentType} of the given command.
     * 
     * @param command the command which {@code ArgumentType} is to be mapped
     * @return the mapped {@code ArgumentType}
     */
    protected ArgumentType<?> type(ArgumentCommandNode<T, ?> command) {
        return command.getType();
    }
    
    /**
     * Maps the {@code Command} of the given command.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Always returns {@link #NONE}.
     * 
     * @param command the command which {@code Command} is to be mapped
     * @return the mapped {@code Command}
     */
    protected Command<R> execution(CommandNode<T> command) {
        return (Command<R>) NONE;
    }
    
    /**
     * Maps the {@code requirement} of the given command.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Always returns {@link #TRUE}.
     * 
     * @param command the command which {@code requirement} is to be mapped
     * @return the mapped {@code requirement}
     */
    protected Predicate<R> requirement(CommandNode<T> command) {
        return (Predicate<R>) TRUE;
    }
    
    /**
     * Maps the {@code SuggestionProvider} of the given command.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Always returns {@code null}.
     * 
     * @param command the command which {@code SuggestionProvider} is to be mapped
     * @return the mapped {@code SuggestionProvider}
     */
    protected @Nullable SuggestionProvider<R> suggestions(ArgumentCommandNode<T, ?> command) {
        return null;
    }
    
}
