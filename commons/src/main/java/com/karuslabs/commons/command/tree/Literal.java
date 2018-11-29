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

import com.karuslabs.commons.command.*;

import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Literal<S> extends LiteralCommandNode<S> implements Aliasable<S>, Mutable<S> {

    private CommandNode<S> destination;
    private List<CommandNode<S>> aliases;
    
    
    public Literal(String name, Command<S> command, Predicate<S> requirement) {
        this(name, command, requirement, null, null, false);
    }
    
    public Literal(String name, Command<S> command, Predicate<S> requirement, @Nullable CommandNode<S> destination, RedirectModifier<S> modifier, boolean fork) {
        super(name, command, requirement, destination, modifier, fork);
        this.destination = destination;
        this.aliases = new ArrayList<>(0);
    }
    
    
    @Override
    public void addChild(CommandNode<S> child) {
        super.addChild(child);
        for (var alias : aliases) {
            alias.addChild(alias);
        }
    }
    
    @Override
    public CommandNode<S> removeChild(String child) {
        var removed = Commands.remove(this, child);
        for (var alias : aliases) {
            Commands.remove(alias, child);
        }
        
        return removed;
    }
    
    
    @Override
    public List<CommandNode<S>> aliases() {
        return aliases;
    }
    
    
    @Override
    public void setCommand(Command<S> command) {
        Commands.set(this, command);
        for (var alias : aliases) {
            Commands.set(alias, command);
        }
    }

    
    @Override
    public @Nullable CommandNode<S> getRedirect() {
        return destination;
    }

    @Override
    public void setRedirect(CommandNode<S> destination) {
        this.destination = destination;
        for (var alias : aliases) {
            if (alias instanceof Mutable<?>) {
                (((Mutable<S>) alias)).setRedirect(destination);
            }
        }
    }
    
    
    public static <S> Builder<S> of(String name) {
        return new Builder<>(name);
    }
    
    
    public static class Builder<S> extends ArgumentBuilder<S, Builder<S>> {
        
        String name;
        
        
        protected Builder(String name) {
            this.name = name;
        }
        
        
        public Builder<S> executes(SingleCommand<S> command) {
            return executes((Command<S>) command);
        }
        
        
        @Override
        protected Builder<S> getThis() {
            return this;
        }

        
        @Override
        public Literal<S> build() {
            var literal = new Literal<>(name, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

            for (var child : getArguments()) {
                literal.addChild(child);
            }

            return literal;
        }

    }
    
}
