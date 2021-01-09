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

import com.karuslabs.commons.command.aot.Token;
import com.karuslabs.Satisfactory.Logger;

import java.util.*;
import javax.lang.model.element.Element;

import static java.util.Collections.EMPTY_LIST;

public abstract class LiteralLexer implements Lexer {
    
    public static LiteralLexer aliasable(Memoizer memoizer) {
        return new AliasableLiteralLexer(memoizer);
    }
    
    public static LiteralLexer single(Memoizer memoizer) {
        return new SingleLiteralLexer(memoizer);
    }
    
    protected final Memoizer memoizer;
    
    public LiteralLexer(Memoizer memoizer) {
        this.memoizer = memoizer;
    }
    
    @Override
    public List<Token> lex(Logger logger, Element element, String lexeme) {
        if (lexeme.contains("<") || lexeme.contains(">")) {
            logger.error(element, lexeme, "contains \"<\"s and \">\"s", "a literal should not contain \"<\"s and \">\"s");
            return EMPTY_LIST;
        }
        
        var identities = lexeme.split("\\|", -1);
        var name = identities[0];
        if (name.isBlank()) {
            logger.error(element, lexeme, "contains a blank name", "should not be blank");
            return EMPTY_LIST;
        }
        
        return lex(logger, element, name, lexeme, Arrays.copyOfRange(identities, 1, identities.length));
    }
    
    abstract List<Token> lex(Logger logger, Element element, String name, String lexeme, String[] aliases);
    
}

class AliasableLiteralLexer extends LiteralLexer {

    public AliasableLiteralLexer(Memoizer memoizer) {
        super(memoizer);
    }
    
    @Override
    List<Token> lex(Logger logger, Element element, String name, String lexeme, String[] aliases) {
        for (var alias : aliases) {
            if (alias.isBlank()) {
                logger.error(element, lexeme, "contains a blank alias", "should not be blank");
                return EMPTY_LIST;
            }
        }
        
        return List.of(memoizer.literal(name, lexeme, aliases));
    }
    
}

class SingleLiteralLexer extends LiteralLexer {

    public SingleLiteralLexer(Memoizer memoizer) {
        super(memoizer);
    }
    
    @Override
    List<Token> lex(Logger logger, Element element, String name, String lexeme, String[] aliases) {
        if (aliases.length > 0) {
            logger.error(element, lexeme, "contains aliases", "should not contain aliases in this context");
            return EMPTY_LIST;
        }
        
        return List.of(memoizer.literal(name, lexeme, aliases));
    }
    
}
