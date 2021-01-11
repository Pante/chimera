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

import com.karuslabs.satisfactory.Logger;
import com.karuslabs.typist.*;

import java.util.*;
import javax.lang.model.element.Element;

@FunctionalInterface
public interface Lexer {

    List<Token> lex(Logger logger, Element element, String lexeme);
    
    static class Memoizer {
        
        private final Map<String, Identity> identities = new HashMap<>();
        
        public Token argument(String name, String lexeme) {
            return memoize(Identity.Type.ARGUMENT, name, lexeme);
        }
        
        public Token literal(String name, String lexeme, String[] aliases) {
            return memoize(Identity.Type.LITERAL, name, lexeme, aliases);
        }
        
        Token memoize(Identity.Type type, String name, String lexeme, String... aliases) {
            var identity = identities.get(name);
            if (identity == null) {
                identity = new Identity(type, name);
                identities.put(name, identity);
            }
            
            return new Token(identity, lexeme, aliases);
        }
        
    }
    
}
