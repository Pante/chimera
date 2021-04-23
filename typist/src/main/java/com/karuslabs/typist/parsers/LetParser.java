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
package com.karuslabs.typist.parsers;

import com.karuslabs.commons.command.annotations.Let;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.typist.lexers.Lexer;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.Find;

import java.lang.annotation.Annotation;
import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.utilitary.Texts.quote;

public class LetParser extends LexParser {
    
    public LetParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }
    
    @Override
    public void parse(Environment environment, Element element) {
        var line = element.getAnnotation(Let.class).value();
        if (line.equals(Let.INFERRED_ARGUMENT)) {
            line = "<" + element.getSimpleName().toString() + ">";
        }
        
        var tokens = lexer.lex(logger, element, line);
        if (tokens.size() != 1) {
            return;
        }
        
        var executable = element.accept(Find.EXECUTABLE, null);
        
        var argument = tokens.get(0);
        var parameter = (VariableElement) element;
        var index = executable.getParameters().indexOf(parameter);
        
        for (var command : environment.method(executable)) {
            var method = (Method) command.bindings.get(executable);
            var referred = find(command, argument.identity);
            if (referred != null) {
                method.parameter(command, new Reference(index, parameter, referred));
                
            } else {
                logger.error(element, argument.identity, "does not exist in " + quote(command.path()));
            }
        }
    }
    
    @Nullable Command find(Command command, Identity identity) {
        while (command != null) {
            if (!command.identity.equals(identity)) {
                command = command.parent;
                
            } else {
                return command;
            }
        }
        
        return null;
    }
    
    @Override
    public Class<? extends Annotation> annotation() {
        return Let.class;
    }
    
}
