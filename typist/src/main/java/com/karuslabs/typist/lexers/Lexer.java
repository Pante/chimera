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

import com.karuslabs.typist.*;
import com.karuslabs.utilitary.Logger;

import java.util.*;
import javax.lang.model.element.Element;

/**
 * A {@code Lexer} transforms a string that represents a command into a sequence
 * of tokens. These tokens are subsequently processed by a {@code Parser}. A {@code Lexer}
 * may cache and reuse a token's {@code Identity}.
 */
@FunctionalInterface
public interface Lexer {
    
    /**
     * Transforms the given lexeme into a sequence of tokens; an empty sequence
     * is returned if the lexeme is invalid.
     * 
     * @param logger a logger used to report errors
     * @param element the element on which the lexeme is declared
     * @param lexeme the lexeme
     * @return a sequence of tokens
     */
    List<Token> lex(Logger logger, Element element, String lexeme);
    
    /**
     * A {@code Memoizer} stores the {@code Identity} of a lexeme, returning the
     * cached {@code Identity} when the same lexeme occurs again.
     */
    static class Memoizer {
        
        private final Map<String, Identity> identities = new HashMap<>();
        
        /**
         * Returns a argument {@code Token} with the given name and value.
         * 
         * @param name the argument name
         * @param lexeme the lexeme value
         * @return a {@code Token}
         */
        public Token argument(String name, String lexeme) {
            return memoize(Identity.Type.ARGUMENT, name, lexeme);
        }
        
        /**
         * Return a literal {@code Token} with the given name, lexeme and aliases.
         * 
         * @param name the literal name
         * @param lexeme the lexeme value
         * @param aliases the aliases
         * @return a {@code Token}
         */
        public Token literal(String name, String lexeme, String[] aliases) {
            return memoize(Identity.Type.LITERAL, name, lexeme, aliases);
        }
        
        /**
         * Memoizes the {@code Identity} of the given arguments, creating a {@code Token}
         * using the memoized {@code Identity}.
         * 
         * @param type the resultant token's type
         * @param name the name
         * @param lexeme the lexeme
         * @param aliases the aliases
         * @return a {@code Token}
         */
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
