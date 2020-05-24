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
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Literal<T> extends LiteralCommandNode<T> implements Aliasable<T>, Mutable<T> {
    
    public static <T> Literal<T> alias(LiteralCommandNode<T> command, String alias) {
        var literal = new Literal<>(alias, new ArrayList<>(0), true, command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());
 
        for (var child : command.getChildren()) {
            literal.addChild(child);
        }
        
        if (command instanceof Aliasable<?>) {
            ((Aliasable<T>) command).aliases().add(literal);
        }
        
        return literal;
    }
    
    public static <T> Builder<T> builder(String name) {
        return new Builder<>(name);
    }
    
    public static Builder<CommandSender> of(String name) {
        return new Builder<>(name);
    }
    
    
    private CommandNode<T> destination;
    private Consumer<CommandNode<T>> addition;
    private List<LiteralCommandNode<T>> aliases;
    private boolean alias;
    
    
    public Literal(String name, Command<T> command, Predicate<T> requirement) {
        this(name, command, requirement, null, null, false);
    }
    
    public Literal(String name, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        this(name, new ArrayList<>(0), false, command, requirement, destination, modifier, fork);
    }
    
    public Literal(String name, List<LiteralCommandNode<T>> aliases, boolean alias, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        super(name, command, requirement, destination, modifier, fork);
        this.destination = destination;
        this.addition = super::addChild;
        this.aliases = aliases;
        this.alias = alias;
    }
    
    
    @Override
    public void addChild(CommandNode<T> child) {
        Nodes.addChild(this, child, addition);
        
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
    public List<LiteralCommandNode<T>> aliases() {
        return aliases;
    }
    
    @Override
    public boolean isAlias() {
        return alias;
    }
    
    
    @Override
    public void setCommand(Command<T> command) {
        Commands.execution(this, command);
        for (var alias : aliases) {
            Commands.execution(alias, command);
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
            if (alias instanceof Mutable<?>) {
                (((Mutable<T>) alias)).setRedirect(destination);
            }
        }
    }

    
    public static class Builder<T> extends Nodes.Builder<T, Builder<T>> {
        
        String name;
        List<String> aliases;
        
        
        protected Builder(String name) {
            this.name = name;
            this.aliases = new ArrayList<>(0);
        }
        
        
        public Builder<T> alias(String... aliases) {
            Collections.addAll(this.aliases, aliases);
            return this;
        }

        
        @Override
        public Literal<T> build() {
            var literal = new Literal<>(name, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
            for (var child : getArguments()) {
                literal.addChild(child);
            }
            
            for (var alias : aliases) {
                Literal.alias(literal, alias);
            }

            return literal;
        }
        
        
        @Override
        protected Builder<T> getThis() {
            return this;
        }

    }
    
}
