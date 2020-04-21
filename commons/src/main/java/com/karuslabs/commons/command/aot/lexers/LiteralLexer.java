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
import com.karuslabs.commons.command.aot.tokens.Literal;
import com.karuslabs.commons.command.aot.tokens.Token.Visitor;

import java.util.HashSet;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.Messages.reason;


public @Stateless class LiteralLexer implements Lexer {

    @Override
    public boolean lex(Visitor<String, Boolean> visitor, Element site, String context, String value) {
        var names = split(visitor, context, value);
        if (names.length == 0) {
            return false;
        }
        
        var name = names[0];
        if (!valid(visitor, context, "name", name)) {
            return false;
        }
        
        var aliases = new HashSet<String>();
        var success = true;
        
        for (int i = 1; i < names.length; i++) {
            var alias = names[i];
            success &= valid(visitor, context, "alias", alias);
            
            if (!aliases.add(alias)) {
                visitor.warn(reason("Invalid alias", alias, context, "alias already exists"));
            }
        }
        
        return success && visitor.literal(new Literal(site, context, name, aliases), context);
    }
    
    
    String[] split(Visitor<String, Boolean> visitor, String context, String value) {
        // No need to check for starting '|'s since it will result in blank spaces in array
        if (value.endsWith("|")) {
            visitor.warn(reason("Trailing '|'s found in", value, context));
        }
        
        var names = value.split("\\|");
        if (names.length == 0) {
            visitor.error(reason("Blank literal name", value, context, "literals cannot be blank"));
        }
        
        return names;
    }
    
    
    boolean valid(Visitor<String, Boolean> visitor, String context, String type, String value) {
        if (value.isBlank()) {
            return visitor.error(reason("Blank literal", type, context, "literals cannot be blank"));
            
        } else if (value.contains("<") || value.contains(">")) {
            return visitor.error(reason("Invalid literal" + type,  value , context, "literals cannot contain '<' and '>'"));
            
        } else if (value.contains("|")) {
            return visitor.error(reason("Invalid literal" + type, value, context, "literals cannot contain '|'"));
            
        } else {
            return true;
        }
    }

}
