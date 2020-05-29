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
package com.karuslabs.commons.command.aot.lexers;

import com.karuslabs.commons.command.aot.*;

import java.util.*;
import javax.lang.model.element.Element;

import static java.util.Collections.EMPTY_LIST;


/**
 * A {@code Lexer} subclass that produces literal and argument tokens from a string.
 * Delegates lexical analysis of literals and arguments to the respective {@code Lexer}
 * subclasses. Commands should match {@code (literal)( ((argument)|(literal)))*}.
 */
public class CommandLexer implements Lexer {

    private Lexer argument;
    private Lexer literal;
    
    
    /**
     * Creates a {@code CommandLexer}.
     * 
     * @param argument the lexical analyzer for argument tokens
     * @param literal the lexical analyzer for literal tokens
     */
    public CommandLexer(Lexer argument, Lexer literal) {
        this.literal = literal;
        this.argument = argument;
    }
    
    
    /**
     * Produces a list of argument and literal tokens from {@code raw}.
     * 
     * @param environment the environment
     * @param location the location that {@code raw} was declared
     * @param raw the value
     * @return a list of argument and literal tokens if {@code raw} is valid; else an empty list
     */
    @Override
    public List<Token> lex(Environment environment, Element location, String raw) {
        if (raw.isBlank()) {
            environment.error(location, "Command should not be blank");
            return EMPTY_LIST;
        }
        
        var tokens = new ArrayList<Token>();
        for (var command : raw.split("\\s+")) {
            if (command.startsWith("<")) {
                tokens.addAll(argument.lex(environment, location, command));
                
            } else {
                tokens.addAll(literal.lex(environment, location, command));
            }
        }
        
        return tokens;
    }
    
}
