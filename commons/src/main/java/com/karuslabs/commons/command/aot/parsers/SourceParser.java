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

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Pack;
import com.karuslabs.puff.Logger;
import com.karuslabs.puff.type.Find;

import java.util.regex.*;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.annotations.Pack.RELATIVE_PACKAGE;
import static com.karuslabs.puff.Texts.quote;

public class SourceParser implements Parser {

    static final Matcher PACKAGE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])(\\.(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$]))*").matcher("");
    
    private final Logger logger;
    
    public SourceParser(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void parse(Environment environment, Element element) {
        var source = element.getAnnotation(Pack.class).age();
        
        if (RELATIVE_PACKAGE.equals(source)) {
            environment.out = new Out(element, element.accept(Find.PACKAGE, null).getQualifiedName().toString(), "Commands");
            return;
            
        } else if (source.endsWith(".java") || source.endsWith(".class")) {
            var parts = source.split("\\.");
            logger.error(element, "File ends with " + quote("." + parts[parts.length - 1])+ ", should not end with file extension");
            return;
            
        }  else if (!PACKAGE.reset(source).matches()) {
            logger.error(element, "Invalid package name");
            return;
        } 
        
        int dot = source.lastIndexOf(".");
        environment.out = dot == -1 ? new Out(element, "", source) : new Out(element, source.substring(0, dot), source.substring(dot + 1, source.length()));
    }

}
