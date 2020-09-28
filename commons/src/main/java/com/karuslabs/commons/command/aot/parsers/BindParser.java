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
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.Mirrors.Command;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.lexers.Lexer;

import java.util.Map;
import javax.lang.model.element.Element;

public class BindParser extends Parser {
    
    public BindParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }
    
    @Override
    protected void process(Element element, Map<Identifier, Command> namespace) {
        if (namespace.isEmpty()) {
            logger.zone(element.accept(Filter.CLASS, null)).error("Class should be annotated with @Command");
            return;
        }
        
        var bindings = element.getAnnotation(Bind.class).value();
        if (bindings.length == 0) {
            logger.error("@Bind annotation should not be empty");
            return;
        }
        
        for (var binding : bindings) {
            var tokens = lexer.lex(logger, binding);
            if (tokens.isEmpty()) {
                continue;
            }
            
            var identifier = tokens.get(tokens.size() - 1).identifier;
        }
    }
    
}
