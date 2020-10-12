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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.aot.annotations.Source;
import com.karuslabs.commons.command.aot.generation.Generation;

import java.util.regex.*;
import javax.lang.model.element.Element;

import static com.karuslabs.annotations.processor.Messages.quote;
import static com.karuslabs.commons.command.aot.annotations.Source.RELATIVE_PACKAGE;

public class SourceParser implements Parser<Generation> {

    static final Matcher PACKAGE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])(\\.(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$]))*").matcher("");
    
    private final Logger logger;
    
    public SourceParser(Logger logger) {
        this.logger = logger;
    }
    
    
    @Override
    public void parse(Element element, Generation generation) {
        logger.zone(element);
        
        var source = element.getAnnotation(Source.class).value();
        if (RELATIVE_PACKAGE.equals(source)) {
            generation.path(element, element.accept(Filter.PACKAGE, null).getQualifiedName().toString(), "Commands");
            return;
            
        } else if (source.endsWith(".java") || source.endsWith(".class")) {
            var parts = source.split("\\.");
            logger.error("File ends with " + quote("." + parts[parts.length - 1])+ ", should not end with file extension");
            return;
            
        }  else if (!PACKAGE.reset(source).matches()) {
            logger.error("Invalid package name");
            return;
        } 
        
        int dot = source.lastIndexOf(".");
        if (dot != -1) {
            generation.path(element, source.substring(0, dot), source.substring(dot + 1, source.length()));
            
        } else {
            generation.path(element, "", source);
        }
        
    }

}
