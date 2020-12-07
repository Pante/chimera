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
import com.karuslabs.puff.Log;

import java.util.*;
import javax.lang.model.element.Element;

@FunctionalInterface
public interface Lexer {

    List<Token> lex(Log log, Element element, String lexeme);
    
    static class Memoizer {
        
        private final Map<String, Identity> identities = new HashMap<>();
        
        Token memoize(Identity.Type type, String name, String lexeme, String... aliases) {
            var identity = identities.get(name);
            if (identity == null) {
                identity = new Identity(type, name);
                identities.put(lexeme, identity);
            }
            
            return new Token(identity, lexeme, aliases);
        }
        
    }
    
}
