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

import com.karuslabs.commons.command.aot.*;

import java.util.*;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.Messages.reason;
import static java.util.Collections.EMPTY_LIST;


public class LiteralLexer implements Lexer {

    @Override
    public List<Token> lex(Environment environment, Element location, String value, String context) {
        var names = split(environment, location, value, context);
        if (names.length == 0) {
            return EMPTY_LIST;
        }
        
        var name = names[0];
        if (!valid(environment, location, name, context)) {
            return EMPTY_LIST;
        }
        
        var aliases = new HashSet<String>();
        var success = true;
        
        for (int i = 1; i < names.length; i++) {
            var alias = names[i];
            success &= valid(environment, location, alias, context);
            
            if (!aliases.add(alias)) {
                environment.warn(location, reason("Alias already exists", alias, context));
            }
        }
        
        return success ? List.of(Token.literal(location, name, aliases, context)) : EMPTY_LIST;
    }
    
    
    String[] split(Environment environment, Element location, String value, String context) {
        // No need to check for starting '|'s since it will result in blank spaces in array
        if (value.endsWith("|")) {
            environment.warn(location, reason("Trailing '|'s found in", value, context));
        }
        
        var names = value.split("\\|");
        if (names.length == 0) {
            environment.error(location, reason("Literals cannot be blank", value, context));
        }
        
        return names;
    }
    
    
    boolean valid(Environment environment, Element location, String value, String context) {
        if (value.isBlank()) {
            environment.error(location,reason("Literals cannot be blank", value, context));
            return false;
            
        } else if (value.contains("<") || value.contains(">")) {
            environment.error(location,reason("Literals cannot contain '<' and '>'",  value , context));
            return false;
            
        } else if (value.contains("|")) {
            environment.error(location,reason("Literals cannot contain '|'", value, context));
            return false;
            
        } else {
            return true;
        }
    }

}
