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
package com.karuslabs.typist.lexers;

import com.karuslabs.typist.Token;
import com.karuslabs.utilitary.Logger;

import java.util.*;
import javax.lang.model.element.Element;

import static java.util.Collections.EMPTY_LIST;

/**
 * A {@code Lexer} that transforms a string representation of arguments and literals
 * into a sequence of argument and literal tokens.
 */
public class CommandLexer implements Lexer {

    private final Lexer argument;
    private final Lexer literal;
    
    /**
     * Creates a {@code CommandLexer} with the given argument and literal lexers.
     * 
     * @param argument the argument lexer
     * @param literal the literal lexer
     */
    public CommandLexer(Lexer argument, Lexer literal) {
        this.argument = argument;
        this.literal = literal;
    }
    
    /**
     * Transforms the given lexeme into a sequence of argument and literal {@code Token}s;
     * invalid lexemes are discarded from the returned sequence.
     * 
     * @param logger the logger used to report errors
     * @param element the element on which the lexeme declared
     * @param line the lexeme
     * @return a single argument {@code Token}
     */
    @Override
    public List<Token> lex(Logger logger, Element element, String line) {
        if (line.isBlank()) {
            logger.error(element, "Command should not be blank");
            return EMPTY_LIST;
        }
        
        var tokens = new ArrayList<Token>();
        for (var command : line.split("\\s+")) {
            if (command.startsWith("<")) {
                tokens.addAll(argument.lex(logger, element, command));
                
            } else {
                tokens.addAll(literal.lex(logger, element, command));
            }
        }
        
        return tokens;
    }

}
