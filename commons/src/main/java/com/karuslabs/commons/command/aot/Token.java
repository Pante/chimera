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

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.aot.Messages.*;


public class Token {
    
    public final Element location;
    public final String lexeme;
    public final Type type;
    public final Set<String> aliases;
    public final Map<Binding, Token> bindings;
    public final Map<String, Token> children;
    public final String literal;

    
    public static Token argument(Element location, String lexeme) {
        return new Token(location, lexeme, Type.ARGUMENT, Set.of(), "<" + lexeme + ">");
    }
    
    public static Token literal(Element location, String lexeme, Set<String> aliases) {
        var literal = lexeme;
        if (!aliases.isEmpty()) {
            literal = literal + "|" + String.join("|", aliases);
        }
        
        return new Token(location, lexeme, Type.LITERAL, aliases, literal);
    }
    
    public static Token root() {
        return new Token(null, "", Type.ROOT, Set.of(), "");
    }
    
    
    Token(Element location, String lexeme, Type type, Set<String> aliases, String literal) {
        this.location = location;
        this.lexeme = lexeme;
        this.type = type;
        this.aliases = aliases;
        this.bindings = new EnumMap<>(Binding.class);
        this.children = new HashMap<>();
        this.literal = literal;
    }
    
    
    public @Nullable Token add(Environment environment, Token child) {
        var existing = children.get(child.lexeme);
        if (existing == null) {
            children.put(child.lexeme, child);
            return child;
        }
        
        return existing.merge(environment, child) ? existing : null;
    } 
    
    boolean merge(Environment environment, Token other) {
        if (type != other.type) {
            environment.error(location, format(lexeme, "already exists", "an argument and literal in the same scope should not have the same name"));
            return false; 
            
        } else if (type == Type.LITERAL) {
            for (var alias : other.aliases) {
                if (!aliases.add(alias)) {
                    environment.warn(location, "Duplicate alias: " + quote(alias));
                }
            }
        }
        
        return true;
    }
    
    
    public void bind(Environment environment, Binding binding, Token token) {
        var existing = bindings.get(binding);
        if (existing != null) {
            environment.error(existing.location, binding.article + " " + binding.signature + " is already bound to " + this);
            
        } else if (binding == Binding.TYPE && type != Type.ARGUMENT) {
            environment.error(token.location, binding.article + " " + binding.signature + " cannot be bound to literal: " + this);
       
        } else {
            bindings.put(binding, token);
        }
    }
    
    
    @Override
    public String toString() {
        return literal;
    }
    
}
