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

import java.util.*;


/**
 * Represents a processor which resolves and processes {@code CommandExecutor}s.
 */
public class CommandProcessor {
    
    private Set<Processor> processors;
    private Resolver resolver;
    
    
    /**
     * Constructs a {@code CommandProcessor} with the specified {@code Processor}s and {@code Resolver}.
     * 
     * @param processors the processors to which processing is delegated
     * @param resolver the resolver to which resolving is delegated
     */
    public CommandProcessor(Set<Processor> processors, Resolver resolver) {
        this.processors = processors;
        this.resolver = resolver;
    }
    
    
    /**
     * Resolves and processes the {@code CommandExecutor} using the specified {@code ProxiedCommandMap}.
     * 
     * @param map the ProxiedCommandMap
     * @param executor the executor
     */
    public void process(ProxiedCommandMap map, CommandExecutor executor) {
        if (!resolver.isResolvable(executor)) {
            throw new IllegalArgumentException("unresolvable CommandExecutor: " + executor.getClass().getName());
        }
        
        List<Command> commands = resolver.resolve(map, executor);
        for (Processor processor : processors) {
            if (processor.hasAnnotations(executor)) {
                processor.process(commands, executor);
            }
        }
        
        for (Command command : commands) {
            command.setExecutor(executor);
        }
    }
    
}
