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

import static com.karuslabs.annotations.processor.Messages.format;


/**
 * A parser that builds an AST from {@code Element}s.
 */
public abstract class Parser {
    
    /**
     * The environment.
     */
    protected Environment environment;
    /**
     * The lexical analyzer that produces tokens from an {@code element}.
     */
    protected Lexer lexer;
    
    
    /**
     * Creates a {@code Parser} with the given parameters.
     * 
     * @param environment the environment
     * @param lexer the lexical analyzer
     */
    public Parser(Environment environment, Lexer lexer) {
        this.environment = environment;
        this.lexer = lexer;
    }
    
    
    /**
     * Parses the given element.
     * 
     * @param element the element
     */
    public abstract void parse(Element element);
    
    
    /**
     * Determines if the given sequence of tokens is valid.
     * <br><br>
     * <b>Implementation details:</b><br>
     * The sequence of tokens is considered valid if it is not empty and does not
     * start with an argument token.
     * 
     * @param tokens the tokens
     * @return {@code true} if valid; else {@code false}
     */
    protected boolean valid(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return false;
        }
        
        var token = tokens.get(0);
        var error = token.type == Type.ARGUMENT;
        if (error) {
            environment.error(token.location, format(token, "is at an invalid position", "command should not start with an argument"));
        }
        
        return !error;
    }
    
}
