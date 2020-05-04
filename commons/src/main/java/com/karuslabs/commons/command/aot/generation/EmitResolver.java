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

import com.karuslabs.annotations.processor.Filter;
import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.annotations.Emit;

import java.util.regex.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.aot.annotations.Emit.RELATIVE_PACKAGE;


public class EmitResolver {
    
    static final Matcher PACKAGE = Pattern.compile("^([a-zA-Z_]{1}[a-zA-Z]*){2,10}\\.([a-zA-Z_]{1}[a-zA-Z0-9_]*){1,30}((\\.([a-zA-Z_]{1}[a-zA-Z0-9_]*){1,61})*)?$").matcher("");
    static final Matcher FILE = Pattern.compile("([a-zA-Z_$]?)([a-zA-Z\\d_$])*(\\.java)").matcher("");
    
    
    private Environment environment;
    private @Nullable Element element;
    private @Nullable String pack;
    private @Nullable String file;
    
    
    public EmitResolver(Environment environment) {
        this.environment = environment;
    }

    
    public void resolve(Element element) {
        this.element = element;
        
        var emit = element.getAnnotation(Emit.class);
        var value = emit.pack();
        
        if (RELATIVE_PACKAGE.equals(value)) {
            pack = element.accept(Filter.PACKAGE, null).getQualifiedName().toString();
            file = "Commands";
            
        } else if (value.endsWith(".java")) {
            environment.error(element, "File ends with \".java\", should not end with file extension");
            
        }  else if (!PACKAGE.reset(value).matches()) {
            environment.error(element, "Invalid package name");
            
        } else {
            int dot = value.lastIndexOf(".");
            pack = value.substring(0, dot);
            file = value.substring(dot + 1, value.length());
        }
    }
    
    
    public @Nullable Element element() {
        return element;
    }
    
    public @Nullable String pack() {
        return pack;
    }
    
    public @Nullable String file() {
        return file;
    }

}
