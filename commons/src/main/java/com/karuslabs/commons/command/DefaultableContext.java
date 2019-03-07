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
package com.karuslabs.commons.command;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.tree.CommandNode;

import java.lang.invoke.*;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;


public class DefaultableContext<S> extends CommandContext<S> {
    
    static final VarHandle ARGUMENTS;
    
    static {
        try {
            ARGUMENTS = MethodHandles.privateLookupIn(CommandContext.class, MethodHandles.lookup())
                                     .findVarHandle(CommandContext.class, "arguments", Map.class);
            
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    
    private CommandContext<S> context;
    private Map<String, ParsedArgument<S, ?>> arguments;
    
    
    public DefaultableContext(CommandContext<S> context) {
        super(null, null, null, null, null, null, null, null, false);
        this.context = context;
        this.arguments = (Map<String, ParsedArgument<S, ?>>) ARGUMENTS.get(context);
    }
    
    
    public <V> @Nullable V getOptionalArgument(String name, Class<V> type) {
        return getOptionalArgument(name, type, null);
    }
    
    public <V> V getOptionalArgument(String name, Class<V> type, V value) {
        var argument = arguments.get(name);
        if (argument != null) {
            return getArgument(name, type);
            
        } else {
            return value;
        }
    }
    
    
    @Override
    public DefaultableContext<S> copyFor(final S source) {
        return new DefaultableContext(context.copyFor(source));
    }

    @Override
    public CommandContext<S> getChild() {
        return context.getChild();
    }

    @Override
    public CommandContext<S> getLastChild() {
        return context.getLastChild();
    }

    @Override
    public Command<S> getCommand() {
        return context.getCommand();
    }

    @Override
    public S getSource() {
        return context.getSource();
    }

    @Override
    public <V> V getArgument(String name, Class<V> type) {
        return context.getArgument(name, type);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DefaultableContext)) return false;

        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode();
    }

    @Override
    public RedirectModifier<S> getRedirectModifier() {
        return context.getRedirectModifier();
    }

    @Override
    public StringRange getRange() {
        return context.getRange();
    }

    @Override
    public String getInput() {
        return context.getInput();
    }

    @Override
    public Map<CommandNode<S>, StringRange> getNodes() {
        return context.getNodes();
    }

    @Override
    public boolean isForked() {
        return context.isForked();
    }
    
}
