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


public class CommandLexer implements Lexer {

    private Lexer literal;
    private Lexer argument;
    
    
    CommandLexer(Lexer literal, Lexer argument) {
        this.literal = literal;
        this.argument = argument;
    }
    
    
    @Override
    public List<Token> lex(Agent agent, String value, String context) {
        if (value.isBlank()) {
            agent.error("Invalid command, command cannot be blank");
            return EMPTY;
        }
        
        var tokens = new ArrayList<Token>();
        for (var command : value.split("\\s+")) {
            if (command.startsWith("<")) {
                tokens.addAll(argument.lex(agent, context, command));
                
            } else {
                tokens.addAll(literal.lex(agent, context, command));
            }
        }
        
        return tokens;
    }
    
}
