/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.aot;

import com.karuslabs.puff.type.Find;

import java.util.*;
import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Environment {
    
    public @Nullable Out out;
    private final Map<TypeElement, Map<Identity, Command>> namespaces = new HashMap<>();
    private final Map<ExecutableElement, List<Command>> methods = new HashMap<>();
    
    public Map<Identity, Command> namespace(Element element) {
        var type = element.accept(Find.TYPE, null);
        var namespace = namespaces.get(type);
        if (namespace == null) {
            namespaces.put(type, namespace = new HashMap<>());
        } 
        
        return namespace;
    }
    
    public List<Command> method(Element element) {
        var method = element.accept(Find.EXECUTABLE, null);
        var commands = methods.get(method);
        if (commands == null) {
            methods.put(method, commands = new ArrayList<>());
        }
        
        return commands;
    }
    
}
