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


public class LiteralLexer implements Lexer {

    @Override
    public void lex(Visitor visitor, String context, String literal) {
        var names = literal.split("\\|");
        var name = names[0];
        
        if (valid(visitor, context, name)) {
            visitor.literal(context, name);
        }
        
        for (int i = 1; i < names.length; i++) {
            var alias = names[i];
            if (valid(visitor, context, alias)) {
                visitor.alias(context, literal, alias);
            }
        }
    }
    
    boolean valid(Visitor visitor, String context, String name) {
        if (name.startsWith("<") || name.endsWith(">")) {
            visitor.error("Invalid literal syntax: '" + name +"' in " + context + ", literals cannot be enclosed by '<' and '>'");
            return false;

        } else if (name.contains("|")) {
            visitor.error("Invalid literal syntax: '" + name +"' in " + context + ", excessive '|'s found");
            return false;
            
        } else if (name.isBlank()) {
            visitor.error("Invalid literal name in '" + context + "', literals cannot be blank");
            return false;
            
        } else {
            return true;
        }
    }

}
