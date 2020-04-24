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

import java.util.Set;

import static com.karuslabs.commons.command.aot.Messages.*;


public class Token {
    
    public static enum Type {
        
        ARGUMENT("argument"), LITERAL("literal");
        
        public final String value;
        
        private Type(String value) {
            this.value = value;
        }
        
    }
    
    
    public final String lexeme;
    public final Type type;
    public final Set<String> aliases;
    public final String literal;
    public final String context;
    
    
    public static Token argument(String lexeme, String context) {
        return new Token(lexeme, Type.ARGUMENT, Set.of(), "<" + lexeme + ">", context);
    }
    
    public static Token literal(String lexeme, Set<String> aliases, String context) {
        return new Token(lexeme, Type.LITERAL, aliases, String.join("|", lexeme, String.join("|", aliases)), context);
    }
    
    
    Token(String lexeme, Type type, Set<String> aliases, String literal, String context) {
        this.lexeme = lexeme;
        this.type = type;
        this.literal = literal;
        this.context = context;
        this.aliases = aliases;
    }
    
    
    public boolean merge(Environment environment, Token other) {
        if (!lexeme.equals(other.lexeme)) {
            throw new IllegalArgumentException("Invalid merge between: " + lexeme + " and " + other.lexeme + ", tokens have different lexemes");
            
        } else if (type != other.type) {
            environment.error(reason("Invalid " + other.type.value, other, "command of a different type already exists"));
            return false; 
            
        }
        
        if (type == Type.LITERAL) {
            for (var alias : other.aliases) {
                if (!aliases.add(alias)) {
                    environment.warn(reason("Duplicate alias", alias, other.context, "alias already exists"));
                }
            }
        } 
        
        return true;
    }
    
    
    @Override
    public String toString() {
        return site(literal, context);
    }
    
}
