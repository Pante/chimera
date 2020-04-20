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
package com.karuslabs.commons.command.aot;

import com.karuslabs.commons.command.aot.tokens.*;

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Environment {

    public final Map<String, LiteralToken> global;
    Map<Element, Map<String, LiteralToken>> scopes;
    @Nullable Reporter reporter;
    @Nullable Map<String, LiteralToken> scope;
    @Nullable Token current;
    
    
    public Environment() {
        global = new HashMap<>();
        scopes = new HashMap<>();
    }
    
    
    public @Nullable Token add(Token token, String context) {
        if (current == null) {
            return addGlobal(token, context);
            
        } else {
            return addScope(token, context);
        }
    }
    
    @Nullable Token addGlobal(Token token, String context) {
        var existing = global.get(token.value);
        if (existing != null) {
            if (token instanceof LiteralToken) {
                if (token.value.equalsIgnoreCase(existing.value)) {
                    reporter.error("Invalid literal: '" + token.value + "' in '" + context + "', literal already exists");
                    return null;
                    
                } else {
                   for (var alias : ((LiteralToken) token).aliases) {
                       if (!existing.aliases.add(alias)) {
                           reporter.warn("Duplicate alias: '" + alias + "' in '" + context + "'");
                       }
                   }
                   
                   current = existing;
                   return existing;
                }
                
            } else {
                reporter.error("Invalid argument position: '<" + token.value + ">' in '" + context + "', commands must start with literals");
                return null;
            }
            
        } else {
            if (token instanceof LiteralToken) {
                var literal = (LiteralToken) token;
                global.put(token.value, literal);
                scope.put(token.value, literal);
                current = token;
                return token;
                
            } else {
                reporter.error("Invalid argument position: '<" + token.value + ">' in '" + context + "', commands must start with literals");
                return null;
            }
        }
    }
    
    @Nullable Token addScope(Token token, String context) {
        return null;
    }
    
}
