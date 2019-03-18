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
 * Represents a command that supports optional arguments when executed.
 * 
 * @param <S> the type of the source
 */
@FunctionalInterface
public interface Executable<S> extends Command<S> {
    
    /**
     * Executes this command in the given context.
     * 
     * @param context the context
     * @throws CommandSyntaxException if the command could not be executed
     */
    public void execute(DefaultableContext<S> context) throws CommandSyntaxException;
    
    
    /**
     * Delegates execution to {@link #execute(DefaultableContext)} with the given 
     * context before returning a {@code SINGLE_SUCCESS}.
     * 
     * @param context the context
     * @return a single success
     * @throws CommandSyntaxException if the command could not be executed
     */
    @Override
    public default int run(CommandContext<S> context) throws CommandSyntaxException {
        execute(new DefaultableContext<>(context));
        return SINGLE_SUCCESS;
    }
    
}
