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

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Argument<T, V> extends ArgumentCommandNode<T, V> implements Mutable<T> {
    
    public static <T, V> Builder<T, V> builder(String name, ArgumentType<V> type) {
        return new Builder<>(name, type);
    }
    
    public static <V> Builder<CommandSender, V> of(String name, ArgumentType<V> type) {
        return new Builder<>(name, type);
    }
    
    
    private CommandNode<T> destination;

    
    public Argument(String name, ArgumentType<V> type, Command<T> command, Predicate<T> requirement, SuggestionProvider<T> suggestions) {
        this(name, type, command, requirement, null, null, false, suggestions);
    }
    
    public Argument(String name, ArgumentType<V> type, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork, SuggestionProvider<T> suggestions) {
        super(name, type, command, requirement, destination, modifier, fork, suggestions);
        this.destination = destination;
    }
    
    
    @Override
    public void addChild(CommandNode<T> child) {
        var current = getChild(child.getName());
        var existingAliases = current instanceof Aliasable<?> ? ((Aliasable<T>) current).aliases() : null;
        var childAliases = child instanceof Aliasable<?> ? ((Aliasable<T>) child).aliases() : null;
        
        super.addChild(child); 
        
        if (childAliases != null) {
            for (var alias : childAliases) {
                super.addChild(alias);
            }
        }
        
        if (existingAliases != null) {
            if (childAliases != null) {
                existingAliases.addAll(childAliases);
                for (var alias : childAliases) {
                    for (var grandchild : current.getChildren()) {
                        alias.addChild(grandchild);
                    }
                }
            }

            for (var grandchild : child.getChildren()) {
                for (var existingChildAlias : existingAliases) {
                    existingChildAlias.addChild(grandchild);
                }
            }
        }
    }
    
    
    @Override
    public CommandNode<T> removeChild(String child) {
        return Commands.remove(this, child);
    }
    
    
    @Override
    public void setCommand(Command<T> command) {
        Commands.executes(this, command);
    }
    
    
    @Override
    public @Nullable CommandNode<T> getRedirect() {
        return destination;
    }

    @Override
    public void setRedirect(CommandNode<T> destination) {
        this.destination = destination;
    }

    
    public static class Builder<T, V> extends NodeBuilder<T, Builder<T, V>> {
        
        String name;
        ArgumentType<V> type;
        @Nullable SuggestionProvider<T> suggestions;
        
        
        protected Builder(String name, ArgumentType<V> type) {
            this.name = name;
            this.type = type;
        }

        
        public Builder<T, V> suggests(SuggestionProvider<T> suggestions) {
            this.suggestions = suggestions;
            return getThis();
        }
        
        
        @Override
        protected Builder<T, V> getThis() {
            return this;
        }

        @Override
        public Argument<T, V> build() {
            var parameter = new Argument<>(name, type, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), suggestions);
            for (var child : getArguments()) {
                parameter.addChild(child);
            }
            
            return parameter;
        }

    }
    
}
