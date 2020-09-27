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
package com.karuslabs.commons.command.aot.old.generation;

import com.karuslabs.annotations.processor.Filter;
import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.annotations.Source;

import java.util.regex.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.annotations.processor.Messages.quote;
import static com.karuslabs.commons.command.aot.annotations.Source.RELATIVE_PACKAGE;


public class SourceResolver {
    
    static final Matcher PACKAGE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])(\\.(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$]))*").matcher("");
    
    
    private Environment environment;
    private @Nullable Element element;
    private String folder;
    private String file;
    
    
    public SourceResolver(Environment environment) {
        this.environment = environment;
        this.folder = "";
        this.file = "Commands";
    }

    
    public void resolve(Element element) {
        this.element = element;
        var emit = element.getAnnotation(Source.class);
        var value = emit.value();
        
        if (RELATIVE_PACKAGE.equals(value)) {
            folder = element.accept(Filter.PACKAGE, null).getQualifiedName().toString();
            return;
            
        } else if (value.endsWith(".java") || value.endsWith(".class")) {
            var parts = value.split("\\.");
            environment.error(element, "File ends with " + quote("." + parts[parts.length - 1])+ ", should not end with file extension");
            return;
            
        }  else if (!PACKAGE.reset(value).matches()) {
            environment.error(element, "Invalid package name");
            return;
        } 
        
        int dot = value.lastIndexOf(".");
        if (dot != -1) {
            folder = value.substring(0, dot);
            file = value.substring(dot + 1, value.length());
            
        } else {
            file = value;
        }
    }
    
    public @Nullable Element element() {
        return element;
    }
    
    public String folder() {
        return folder;
    }
    
    public String file() {
        return file;
    }

}
