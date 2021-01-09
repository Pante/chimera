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

import com.karuslabs.commons.command.aot.generation.Out;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Pack;
import com.karuslabs.Satisfactory.Logger;
import com.karuslabs.Satisfactory.type.Find;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.*;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.annotations.Pack.RELATIVE_PACKAGE;
import static com.karuslabs.Satisfactory.Texts.quote;

public class PackageParser implements Parser {

    static final Matcher PACKAGE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])(\\.(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$]))*").matcher("");
    
    private final Logger logger;
    
    public PackageParser(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void parse(Environment environment, Set<? extends Element> elements) {
        if (elements.size() == 1) {
            parse(environment, elements.toArray(new Element[0])[0]);
            
        } else if (elements.isEmpty()) {
            logger.error(null, "Project does not contain a @Pack annotation, should contain one @Source annotation");
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                logger.error(element, "Project contains " + elements.size() + " @Pack annotations, should contain one @Source annotation");
            }
        }
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

    @Override
    public Class<? extends Annotation> annotation() {
        return Pack.class;
    }

}
