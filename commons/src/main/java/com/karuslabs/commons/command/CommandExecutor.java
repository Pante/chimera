/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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

import com.karuslabs.commons.command.arguments.Arguments;


/**
 * Represents an executor which executes {@code Command}s.
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executes the {@code Command} with the specified {@code CommandSource} in the specified {@code Context} with the specified {@code Arguments}.
     * 
     * @param source the source which is executing the command
     * @param context the context which the command is executed in
     * @param arguments the arguments passed to the command
     * @return true if the execution was successful; else false
     */
    public boolean execute(CommandSource source, Context context, Arguments arguments);

    
    /**
     * A {@code CommandExecutor} which displays a translatable message to the {@code CommandSender} 
     * and returns {@code true} on executed.
     */
    public static final CommandExecutor NONE = (source, context, arguments) -> {
        source.sendColouredTranslation("Empty command");
        return true;
    };

}
