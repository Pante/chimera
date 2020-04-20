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
import com.karuslabs.commons.command.aot.tokens.LiteralToken;
import com.karuslabs.commons.command.aot.tokens.Token.Visitor;

import java.util.HashSet;
import javax.lang.model.element.Element;


public @Stateless class LiteralLexer implements Lexer {
    
    @Override
    public boolean lex(Visitor<String> visitor, Element site, String context, String value) {
        var names = split(visitor, context, value);
        if (names.length == 0) {
            return false;
        }
        
        var name = names[0];
        if (!valid(visitor, context, "name", name)) {
            return false;
        }
        
        var aliases = new HashSet<String>();
        var error = false;
        
        for (int i = 1; i < names.length; i++) {
            var alias = names[i];
            if (!valid(visitor, context, "alias", alias)) {
                error = true;
            }
            
            if (!aliases.add(alias)) {
                visitor.warn("Duplicate alias: '" + alias + "' in '" + context + "'");
            }
        }
        
        return !error && visitor.literal(new LiteralToken(site, name, aliases), context);
    }
    
    
    String[] split(Visitor visitor, String context, String value) {
        // No need to check for starting '|'s since it will result in blank spaces in resultant array
        if (value.endsWith("|")) {
            visitor.warn("Trailing '|'s found in '" + value + "' at '" + context + "'");
        }
        
        var names = value.split("\\|");
        if (names.length == 0) {
            visitor.error("Blank literal name: '" + value + "' in '" + context + "', literals cannot be blank");
        }
        
        return names;
    }
    
    
    boolean valid(Visitor visitor, String context, String type, String value) {
        if (value.isBlank()) {
            visitor.error("Blank literal " + type + " in '" + context + "', literals cannot be blank");
            return false;
            
        } else if (value.contains("<") || value.contains(">")) {
            visitor.error("Invalid literal " + type + ": '" + value + "' in '" + context + "', literals cannot contain '<' and '>'");
            return false;
            
        } else if (value.contains("|")) {
            visitor.error("Invalid literal " + type + ": '" + value + "' in '" + context + "', literals cannot contain '|'");
            return false;
            
        } else {
            return true;
        }
    }

}
