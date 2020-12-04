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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.smoke.Filter;
import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.old.Mirrors.Command;
import com.karuslabs.commons.command.aot.old.Lexer;

import java.util.*;
import javax.lang.model.element.*;

public abstract class TokenParser implements Parser<Map<TypeElement, Map<Identity, Command>>> {

    protected final Logger logger;
    protected final Lexer lexer;
    
    public TokenParser(Logger logger, Lexer lexer) {
        this.logger = logger;
        this.lexer = lexer;
    }
    
    @Override
    public void parse(Element element, Map<TypeElement, Map<Identity, Command>> namespaces) {
        logger.zone(element);
        
        var type = element.accept(Filter.CLASS, null);
        var namespace = namespaces.get(type);
        if (namespace == null) {
            namespaces.put(type, namespace = new HashMap<>());
        } 
        
        process(element, namespace);
    }
    
    protected abstract void process(Element element, Map<Identity, Command> namespace);
    
}
