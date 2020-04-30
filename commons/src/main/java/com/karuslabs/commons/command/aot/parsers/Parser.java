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
import com.karuslabs.commons.command.aot.lexers.Lexer;

import java.util.List;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.Messages.reason;


public abstract class Parser {
    
    protected Environment environment;
    protected Lexer lexer;
    
    
    public Parser(Environment environment, Lexer lexer) {
        this.environment = environment;
        this.lexer = lexer;
    }
    
    
    public abstract void parse(Element element);
    
    
    protected boolean valid(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return false;
        }
        
        var token = tokens.get(0);
        if (token.type == Type.ARGUMENT) {
            environment.error(token.location, reason("Commands cannot start with arguments", token));
            return false;
        }
        
        return true;
    }
    
}
