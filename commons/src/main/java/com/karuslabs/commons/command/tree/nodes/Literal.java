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


public class Literal<T> extends LiteralCommandNode<T> implements Aliasable<T>, Mutable<T> {

    private CommandNode<T> destination;
    private List<CommandNode<T>> aliases;
    
    
    public Literal(String name, Command<T> command, Predicate<T> requirement) {
        this(name, command, requirement, null, null, false);
    }
    
    public Literal(String name, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        this(name, new ArrayList<>(0), command, requirement, destination, modifier, fork);
    }
    
    public Literal(String name, List<CommandNode<T>> aliases, Command<T> command, Predicate<T> requirement, @Nullable CommandNode<T> destination, RedirectModifier<T> modifier, boolean fork) {
        super(name, command, requirement, destination, modifier, fork);
        this.destination = destination;
        this.aliases = aliases;
    }
    
    
     @Override
    public void addChild(CommandNode<T> child) {
        var existing = getChild(child.getName());
        if (existing == null) {
            
            
        } else {
            addExistingChild(existing, child);
        }
        super.addChild(child);
    }
    
    protected void addExistingChild(CommandNode<T> existing, CommandNode<T> replacement) {
        if (existing instanceof Aliasable<?> && replacement instanceof Aliasable<?>) {
            var existingChildAliases = ((Aliasable<T>) existing).aliases();
            var newChildAliases = ((Aliasable<T>) replacement).aliases();
            
            existingChildAliases.addAll(newChildAliases);
            for (var newChildAlias : newChildAliases) {
                super.addChild(newChildAlias);
            }
            
            for (var grandchild : replacement.getChildren()) {
                existing.addChild(grandchild);
                for (var existingChildAlias : existingChildAliases) {
                    existingChildAlias.addChild(grandchild);
                }
            }
                        
            for (var alias : aliases) {
                alias.addChild(existing);
            }
        }
    }
    
    protected addNewChild(CommandNode<T>) {
        
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
            if (alias instanceof Mutable<?>) {
                (((Mutable<T>) alias)).setRedirect(destination);
            }
        }
    }

    
    public static <T> Builder<T> builder(String name) {
        return new Builder<>(name);
    }
    
    public static Builder<CommandSender> of(String name) {
        return new Builder<>(name);
    }
    
    
    public static class Builder<T> extends ArgumentBuilder<T, Builder<T>> {
        
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
        
        public Builder<T> alias(String alias) {
            aliases.add(alias);
            return this;
        }
        
        public Builder<T> executes(Executable<T> command) {
            return executes((Command<T>) command);
        }
        
        
        public Builder<T> optionally(ArgumentBuilder<T, ?> builder) {
            return optionally(builder.build());
        }
        
        public Builder<T> optionally(CommandNode<T> node) {
            then(node);
            for (var child : node.getChildren()) {
                then(child);
            }
            
            return this;
        }
        
        
        @Override
        protected Builder<T> getThis() {
            return this;
        }

        
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
