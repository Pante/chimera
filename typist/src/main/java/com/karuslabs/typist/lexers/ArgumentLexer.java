/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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

import java.util.List;
import javax.lang.model.element.Element;

import static java.util.Collections.EMPTY_LIST;

/**
 * A {@code Lexer} that transforms a string representation of an argument into a
 * single argument {@code Token}. An argument should match {@code <name>}.
 */
public class ArgumentLexer implements Lexer {

    private final Memoizer memoizer = new Memoizer();
    
    /**
     * Transforms the given lexeme into a single argument {@code Token}; an empty
     * sequence is returned if the lexeme is invalid.
     * 
     * @param logger the logger used to report errors
     * @param element the element on which the lexeme declared
     * @param lexeme the lexeme
     * @return a single argument {@code Token}
     */
    @Override
    public List<Token> lex(Logger logger, Element element, String lexeme) {
        if (!lexeme.startsWith("<") || !lexeme.endsWith(">")) {
            logger.error(element, lexeme, "is not an argument", "should be surrounded by '<' and '>'");
            return EMPTY_LIST;
        }
        
        if (lexeme.contains("|")) {
            logger.error(element, lexeme, "contains '|'", "argument should not contain aliases");
            return EMPTY_LIST;
        }
        
        var name = lexeme.substring(1, lexeme.length() - 1);
        if (name.isBlank()) {
            logger.error(element, lexeme, "should not be blank");
            return EMPTY_LIST;
        }
        
        if (name.startsWith("<") || name.endsWith(">")) {
            logger.error(element, lexeme, "contains unnecessary '<' and/or '>'");
            return EMPTY_LIST;
        }
        
        return List.of(memoizer.argument(name, lexeme));
    }

}