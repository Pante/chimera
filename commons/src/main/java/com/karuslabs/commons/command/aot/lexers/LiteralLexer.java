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

import static com.karuslabs.smoke.Messages.*;

import static java.util.Collections.EMPTY_LIST;

public abstract class LiteralLexer implements Lexer {

    public static LiteralLexer aliases() {
        return new LiteralAliasLexer();
    }
    
    public static LiteralLexer single() {
        return new SingleLiteralLexer();
    }
    
    @Override
    public List<Token> lex(Logger logger, String lexeme) {
        if (lexeme.contains("<") || lexeme.contains(">")) {
            logger.error(lexeme, "contains \"<\"s and \">\"s", "a literal should not contain \"<\"s and \">\"s");
            return EMPTY_LIST;
        }
        
        return process(logger, lexeme, lexeme.split("\\|", -1));
    }
    
    abstract List<Token> process(Logger logger, String lexeme, String... identifiers);

}

class LiteralAliasLexer extends LiteralLexer {

    @Override
    List<Token> process(Logger logger, String lexeme, String... identifiers) {        
        for (var identifier : identifiers) {
            if (identifier.isEmpty()) {
                logger.error(lexeme, "contains an empty literal alias or name", "should not be empty");
                return EMPTY_LIST;
            }
        }
        
        var aliases = new HashSet<String>();
        for (int i = 1; i < identifiers.length; i++) {
            var alias = identifiers[i];
            if (!aliases.add(alias)) {
                logger.warn("Duplicate alias: " + quote(alias));
            }
        }
        
        // We cannot memomize literal identifiers since .equals(...) ignores aliases
        // but .toString() returns the (raw) lexeme;
        return List.of(new Token(new Identifier(Type.LITERAL, lexeme, identifiers[0]), aliases));
    }
    
}

class SingleLiteralLexer extends LiteralLexer {
    
    private final Memoizer memoizer = new Memoizer();
    
    @Override
    public List<Token> process(Logger logger, String lexeme, String... identifiers) {
        if (identifiers.length > 1) {
            logger.error(lexeme, "contains aliases", "should not contain aliases in this context");
            return EMPTY_LIST;
            
        } else if (identifiers.length == 0) {
            logger.error(lexeme, "is empty");
            return EMPTY_LIST;
        }
        
        return List.of(memoizer.token(Type.LITERAL, lexeme, identifiers[0], Set.of()));
    }
    
}

