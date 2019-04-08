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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.commons.command.Commands;

import com.karuslabs.commons.command.Executable;

import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code LiteralCommandNode} subclass that provides additional convenience methods 
 * and support for aliases.
 * 
 * @param <T> the type of the source
 */
public class Literal<T> extends LiteralCommandNode<T> implements Node<T> {

    private CommandNode<T> destination;
    private List<CommandNode<T>> aliases;
    
    
    /**
     * Creates a {@code Literal} with the given parameters.
     * 
     * @param name the name of the command
     * @param command the command to be executed
     * @param requirement the requirement
     */
    public Literal(String name, Command<T> command, Predicate<T> requirement) {
        this(name, command, requirement, null, null, false);
    }
    
    /**
     * Creates a {@code Literal} with the given parameters.
     * 
     * @param name the name of the command
     * @param command the command to be executed
     * @param requirement the requirement
     * @param destination the destination to which this literal is redirected
     * @param modifier the redirection modifier
     * @param fork the fork
     */
    public Literal(String name, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        this(name, new ArrayList<>(0), command, requirement, destination, modifier, fork);
    }
    
    /**
     * Creates a {@code Literal} with the given parameters.
     * 
     * @param name the name of the command
     * @param aliases the aliases of this literal
     * @param command the command to be executed
     * @param requirement the requirement
     * @param destination the destination to which this literal is redirected
     * @param modifier the redirection modifier
     * @param fork the fork
     */
    public Literal(String name, List<CommandNode<T>> aliases, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        super(name, command, requirement, destination, modifier, fork);
        this.destination = destination;
        this.aliases = aliases;
    }
    
    
    @Override
    public void addChild(CommandNode<T> child) {
        super.addChild(child);
        for (var alias : aliases) {
            alias.addChild(child);
        }
    }
    
    @Override
    public CommandNode<T> removeChild(String child) {
        var removed = Commands.remove(this, child);
        for (var alias : aliases) {
            Commands.remove(alias, child);
        }
        
        return removed;
    }
    
    
    @Override
    public List<CommandNode<T>> aliases() {
        return aliases;
    }
    
    
    @Override
    public void setCommand(Command<T> command) {
        Commands.executes(this, command);
        for (var alias : aliases) {
            Commands.executes(alias, command);
        }
    }

    
    @Override
    public @Nullable CommandNode<T> getRedirect() {
        return destination;
    }

    @Override
    public void setRedirect(CommandNode<T> destination) {
        this.destination = destination;
        for (var alias : aliases) {
            if (alias instanceof Node<?>) {
                (((Node<T>) alias)).setRedirect(destination);
            }
        }
    }

    
    /**
     * Creates a {@code Literal} builder with the given name.
     * 
     * @param <T> the type of the source
     * @param name the name
     * @return a {@code Builder}
     */
    public static <T> Builder<T> builder(String name) {
        return new Builder<>(name);
    }
    
    /**
     * Creates a {@code Literal} builder with {@code CommandSender} as the source
     * type and the given name.
     * 
     * @param name the name
     * @return a {@code Builder}
     */
    public static Builder<CommandSender> of(String name) {
        return new Builder<>(name);
    }
    
    
    /**
     * A {@code Literal} builder.
     * 
     * @param <T> the type of the source
     */
    public static class Builder<T> extends ArgumentBuilder<T, Builder<T>> {
        
        String name;
        List<String> aliases;
        
        
        /**
         * Creates a {@code Builder} with the given name.
         * 
         * @see #builder(String) 
         * @see #of(String) 
         * 
         * @param name the name
         */
        protected Builder(String name) {
            this.name = name;
            this.aliases = new ArrayList<>(0);
        }
        
        
        /**
         * Adds an alias.
         * 
         * @param alias the alias
         * @return {@code this}
         */
        public Builder<T> alias(String alias) {
            aliases.add(alias);
            return this;
        }
        
        /**
         * Sets the {@code Command} to be executed.
         * 
         * @param command the command to be executed
         * @return {@code this}
         */
        public Builder<T> executes(Executable<T> command) {
            return executes((Command<T>) command);
        }
        
        
        /**
         * Adds an optional child built using the given builder. Children of the
         * optional child are also added to this builder.
         * <br><br>
         * <b>Note:</b><br>
         * An issue with the client processing children nodes may cause suggestions
         * from custom {@code SuggestionProvider}s and {@code Type}s to not be displayed.
         * 
         * @param builder the builder which is to build the optional child
         * @return {@code this}
         */
        public Builder<T> optionally(ArgumentBuilder<T, ?> builder) {
            return optionally(builder.build());
        }
        
        /**
         * Adds an optional child. Children of the optional child are also added 
         * to this builder.
         * <br><br>
         * <b>Note:</b><br>
         * An issue with the client processing children nodes may cause suggestions
         * from custom {@code SuggestionProvider}s and {@code Type}s to not be displayed.
         * 
         * @param node the optional child
         * @return {@code this}
         */
        public Builder<T> optionally(CommandNode<T> node) {
            then(node);
            for (var child : node.getChildren()) {
                then(child);
            }
            
            return this;
        }
        
        
        /**
         * @return {@code this}
         */
        @Override
        protected Builder<T> getThis() {
            return this;
        }

        
        /**
         * Builds the {@code Literal}.
         * 
         * @return the {@code Literal}
         */
        @Override
        public Literal<T> build() {
            var literal = new Literal<>(name, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
            for (var child : getArguments()) {
                literal.addChild(child);
            }
            
            for (var alias : aliases) {
                Commands.alias(literal, alias);
            }

            return literal;
        }

    }
    
}
