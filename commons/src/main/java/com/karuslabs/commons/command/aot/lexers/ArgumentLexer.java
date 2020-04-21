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

import com.karuslabs.annotations.Stateless;
import com.karuslabs.commons.command.aot.tokens.Argument;
import com.karuslabs.commons.command.aot.tokens.Token.Visitor;

import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.Messages.reason;


public @Stateless class ArgumentLexer implements Lexer {
    
    @Override
    public boolean lex(Visitor<String, Boolean> visitor, Element site, String context, String value) {
        if (!value.startsWith("<") || !value.endsWith(">")) {
            return visitor.error(reason("Invalid argument syntax", value, context, "argument must be enclosed by '<' and '>'"));
        }
        
        if (value.contains("|")) {
            return visitor.error(reason("Invalid argument syntax", value, context, "argument must not contain '|'s"));
        }
        
        
        var argument = value.substring(1, value.length() - 1);
        if (argument.isBlank()) {
            return visitor.error(reason("Blank argument", value, context, "arguments cannot be blank"));
        }
        
        if (argument.startsWith("<") || argument.startsWith(">")) {
            visitor.warn(reason("Trailing '<'s or '>'s found in", value, context));
        }
        
        return visitor.argument(new Argument(site, context, argument), context);
    }
    
}
