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

import com.karuslabs.annotations.Delegate;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.*;

import java.lang.invoke.*;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code CommandContext} that supports optional arguments. All operations are 
 * forwarded to an underlying {@code CommandContext}.
 * <br><br>
 * <b>Implementation details:</b><br>
 * {@code VarHandle}s are used to manipulate the arguments of a {@code CommandContext}.
 * Hence, an {@code ExceptionInInitializerError} will be thrown if strong module
 * encapsulation is enabled.
 * 
 * @param <T> the type of the source
 */
public @Delegate class OptionalContext<T> extends CommandContext<T> {
    
    static final VarHandle ARGUMENTS;
    static {
        try {
            ARGUMENTS = MethodHandles.privateLookupIn(CommandContext.class, MethodHandles.lookup())
                                     .findVarHandle(CommandContext.class, "arguments", Map.class);
            
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    
    private CommandContext<T> context;
    private @Nullable Map<String, ParsedArgument<T, ?>> arguments;
    
    
    /**
     * Creates a {@code OptionalContext} with the given underlying {@code context}.
     * 
     * @param context the underlying context to which operations are forwarded
     */
    public OptionalContext(CommandContext<T> context) {
        super(null, null, null, null, null, null, null, null, null, false);
        this.context = context;
        this.arguments = null;
    }
    
    
    /**
     * Returns an argument with the given name and type if present.
     * 
     * @param <V> the type of the argument
     * @param name the name of the argument
     * @param type the the type of the argument
     * @return the argument if present; otherwise {@code null}
     */
    public <V> @Nullable V getOptionalArgument(String name, Class<V> type) {
        return getOptionalArgument(name, type, null);
    }
    
    /**
     * Returns an argument with the given name and type, or {@code value} if the 
     * argument does not exist.
     * 
     * @param <V> the type of the argument
     * @param name the name of the argument
     * @param type the type of the argument
     * @param value the default value
     * @return the argument if present; otherwise {@code value}
     */
    public <V> V getOptionalArgument(String name, Class<V> type, V value) {
        if (arguments == null) {
            arguments = (Map<String, ParsedArgument<T, ?>>) ARGUMENTS.get(context);
        }  
        
        var argument = arguments.get(name);
        if (argument != null) {
            return getArgument(name, type);
            
        } else {
            return value;
        }
    }
    
    
    @Override
    public OptionalContext<T> copyFor(T source) {
        return new OptionalContext(context.copyFor(source));
    }

    @Override
    public CommandContext<T> getChild() {
        return context.getChild();
    }

    @Override
    public CommandContext<T> getLastChild() {
        return context.getLastChild();
    }

    @Override
    public Command<T> getCommand() {
        return context.getCommand();
    }

    @Override
    public T getSource() {
        return context.getSource();
    }

    @Override
    public <V> V getArgument(String name, Class<V> type) {
        return context.getArgument(name, type);
    }

    @Override
    public RedirectModifier<T> getRedirectModifier() {
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
    public List<ParsedCommandNode<T>> getNodes() {
        return context.getNodes();
    }

    @Override
    public boolean isForked() {
        return context.isForked();
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
            
        } else  if (!(other instanceof OptionalContext)) {
            return false;
        }

        return context.equals(((OptionalContext) other).context);
    }

    @Override
    public int hashCode() {
        return 31 * context.hashCode();
    }
    
}
