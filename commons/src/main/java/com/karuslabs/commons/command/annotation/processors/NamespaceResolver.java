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

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class NamespaceResolver implements Resolver {

    @Override
    public List<Command> resolve(ProxiedCommandMap map, CommandExecutor executor) {
        Namespaces namespaces = executor.getClass().getAnnotation(Namespaces.class);
        if (namespaces != null) {
            return Stream.of(namespaces.value()).map(namespace -> resolve(map , executor, namespace)).collect(toList());
            
        } else {
            return asList(resolve(map , executor, executor.getClass().getAnnotation(Namespace.class)));
        }
    } 
    
    protected Command resolve(ProxiedCommandMap map, CommandExecutor executor, Namespace namespace) {
        String[] names = namespace.value();
        if (names.length == 0) {
            throw new IllegalArgumentException("Invalid namespace for: " + executor.getClass() + ", namespace cannot be empty");
        }
        
        Command command = map.getCommand(names[0]);
        for (int i = 1; i < names.length; i++) {
            check(command, executor, names, names[i]);
            command = command.getSubcommands().get(names[i]);
        }
        
        return command;
    }
    
    protected void check(Command command, CommandExecutor executor, String[] names, String name) {
        if (command == null) {
            throw new IllegalArgumentException("Unresolvable name: \"" + name + "\" in namespace: \"" + String.join(", ", names) + "\" for " + executor.getClass());
        }
    } 

    
    @Override
    public boolean isResolvable(CommandExecutor executor) {
        Class<? extends CommandExecutor> type = executor.getClass();
        return type.getAnnotation(Namespace.class) != null || type.getAnnotation(Namespaces.class) != null;
    }
    
}
