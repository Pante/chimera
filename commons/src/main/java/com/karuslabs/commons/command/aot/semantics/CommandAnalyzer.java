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
package com.karuslabs.commons.command.aot.semantics;

import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.annotations.Command;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.tokens.Token;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;


public class CommandAnalyzer extends SyntaxAnalyzer {

    public CommandAnalyzer(Lexer lexer, Environment environment, Messager messager, Elements elements, Types types) {
        super(lexer, environment, messager, elements, types);
    }

    
    @Override
    public boolean analyze(Element element) {
        environment.initialize(this);
        environment.scope(element);
        this.element = element;
        
        var commands = element.getAnnotation(Command.class).value();
        if (commands.length == 0) {
            return error("Invalid command declaration, @Command cannot be empty");
        }
        
        var success = true;
        for (var command : commands) {
            success &= lexer.lex(this, element, command, command);
        }
        
        return success;
    }
    
    
    @Override
    public Boolean token(Token token, String context) {
        return environment.add(token) != null;
    }

}
