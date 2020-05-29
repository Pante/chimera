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
package com.karuslabs.commons.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;


/**
 * A {@code Command} that supports optional arguments.
 * 
 * @param <T> the type of the source
 */
@FunctionalInterface
public interface Execution<T> extends Command<T> {
    
    /**
     * Executes this {@code Executable} in the given {@code context}.
     * 
     * @param source the source
     * @param context the context
     * @throws CommandSyntaxException if this {@code Executable} could not be executed
     */
    public void execute(T source, OptionalContext<T> context) throws CommandSyntaxException;
    
    
    /**
     * Forwards execution to {@link #execute(Object, OptionalContext)} with the given 
     * {@code context}.
     * 
     * @param context the context
     * @return {@link SINGLE_SUCCESS}
     * @throws CommandSyntaxException if this {@code Executable} could not be executed
     */
    @Override
    public default int run(CommandContext<T> context) throws CommandSyntaxException {
        execute(context.getSource(), new OptionalContext<>(context));
        return SINGLE_SUCCESS;
    }
    
}
