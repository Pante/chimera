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

import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Identifier.Type;

import java.util.*;

@FunctionalInterface
public interface Lexer {

    List<Token> lex(Logger logger, String lexeme);
    
}

class Memoizer {
    
    private final Map<String, Identifier> identifiers = new HashMap<>();
    
    Token token(Type type, String lexeme, String name, Set<String> aliases) {
        var identifier = identifiers.get(lexeme);
        if (identifier == null) {
            identifier = new Identifier(type, lexeme, name);
            identifiers.put(lexeme, identifier);
        }
        
        return new Token(identifier, aliases);
    }
    
}
