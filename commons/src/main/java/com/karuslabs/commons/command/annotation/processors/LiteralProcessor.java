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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.*;
import com.karuslabs.commons.command.completion.*;

import java.util.List;


/**
 * Represents a processor for {@code Literal} annotations.
 */
public class LiteralProcessor implements Processor {

    /**
     * Processes the {@code Literal} annotations for the specified {@code CommandExecutor}.
     * 
     * @param commands the commands to which the executor is registered
     * @param executor the executor which contains the annotations to process
     */
    @Override
    public void process(List<Command> commands, CommandExecutor executor) {
        for (Literal literal : executor.getClass().getAnnotationsByType(Literal.class)) {
            process(commands, executor, literal);
        }
    }
    
    /**
     * Processes the specified {@code Literal}.
     * 
     * @param commands the commands to which the executor is registered
     * @param executor the executor which contains the annotations to process
     * @param literal the annotation
     */
    protected void process(List<Command> commands, CommandExecutor executor, Literal literal) {
        Completion completion = new CachedCompletion(literal.completions());
        for (Command command : commands) {
            command.getCompletions().put(literal.index(), completion);
        }
    }
    
    
    /**
     * Checks if the specified {@code CommandExecutor} has {@code Literal} annotations.
     * 
     * @param executor the executor
     * @return true if the specified executor has Literal annotations; else false
     */
    @Override
    public boolean hasAnnotations(CommandExecutor executor) {
        Class<? extends CommandExecutor> type = executor.getClass();
        return type.getAnnotationsByType(Literal.class).length != 0;
    }
    
}
