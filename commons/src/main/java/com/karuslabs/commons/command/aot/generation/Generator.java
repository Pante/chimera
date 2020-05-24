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
package com.karuslabs.commons.command.aot.generation;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.generation.blocks.*;

import java.io.*;
import java.util.*;
import javax.annotation.processing.FilerException;
import javax.lang.model.element.*;


public class Generator {
    
    private Environment environment;
    private SourceResolver resolver;
    private TypeBlock type;
    private MethodBlock method;
    
    
    public Generator(Environment environment, SourceResolver resolver, TypeBlock type, MethodBlock method) {
        this.environment = environment;
        this.resolver = resolver;
        this.type = type;
        this.method = method;
    }
    
    
    public void generate() {
        var file = resolver.folder().isEmpty() ? resolver.file() : resolver.folder() + "." + resolver.file();
        var elements = environment.scopes.keySet().toArray(new Element[0]);
        
        try (var writer = environment.filer.createSourceFile(file, elements).openWriter()) {
            type.start(resolver.folder(), resolver.file());
        
            for (var entry : environment.scopes.entrySet()) {
                method.start(((TypeElement) entry.getKey()).getQualifiedName());
                type.method(descend(entry.getValue()));
            }

            writer.write(type.end());
            
        } catch (FilerException ignored) {
            environment.error(resolver.element(), "\"" + file + "\" already exists");
            
        } catch (IOException ignored) {
            environment.error(resolver.element(), "Failed to create file: \"" + file + "\"");
        }
    }
    
    
    public String descend(Token token) {
        var values = token.children.values();
        var children = new ArrayList<String>(values.size());
        
        for (var child : values) {
            children.add(descend(child));
        }
        
        if (token.type != Type.ROOT) {
            var variable = method.command(token);
            for (var child : children) {
                method.addChild(variable, child);
            }
            
            method.newLine();
            return variable;
            
        } else {
            return method.end(children);
        }
    }
    
}
