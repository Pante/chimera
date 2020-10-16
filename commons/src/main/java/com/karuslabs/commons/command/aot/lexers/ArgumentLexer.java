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

import com.karuslabs.annotations.processor.Logger;
import com.karuslabs.commons.command.aot.Token;

import java.util.*;

import static com.karuslabs.commons.command.aot.Identifier.Type.ARGUMENT;
import static java.util.Collections.EMPTY_LIST;

public class ArgumentLexer implements Lexer {
    
    private final Memoizer memoizer = new Memoizer();
    
    @Override
    public List<Token> lex(Logger logger, String lexeme) {
        if (!lexeme.startsWith("<") || !lexeme.endsWith(">")) {
            logger.error(lexeme, "is an invalid argument", "should be enclosed by \"<\" and \">\"");
            return EMPTY_LIST;
        }
        
        if (lexeme.contains("|")) {
            logger.error(lexeme, "contains \"|\"", "an argument should not have aliases");
            return EMPTY_LIST;
        }
        
        var name = lexeme.substring(1, lexeme.length() - 1);
        if (name.isEmpty()) {
            logger.error(lexeme, "is empty", "an argument should not be empty");
            return EMPTY_LIST;
        }
        
        if (name.startsWith("<") || name.endsWith(">")) {
            logger.error(lexeme, "contains trailing \"<\"s or \">\"s");
        }
        
        return List.of(memoizer.token(ARGUMENT, lexeme, name, Set.of()));
    }
    
}