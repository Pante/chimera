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

import static com.karuslabs.annotations.processor.Messages.*;


/**
 * A token which is produced from a lexical analyzer and subsequently used to build
 * an AST.
 */
public class Token {
    
    /**
     * The location at which this token was declared.
     */
    public final Element location;
    /**
     * A literal value of the declared command.
     */
    public final String literal;
    /**
     * The name of the declared command.
     */
    public final String lexeme;
    /**
     * The type of the command which this token represents.
     */
    public final Type type;
    /**
     * The aliases for for this command.
     */
    public final Set<String> aliases;
    /**
     * The elements which are bound to this token.
     */
    public final Map<Binding, Element> bindings;
    /**
     * The children of this command.
     */
    public final Map<String, Token> children;

    
    /**
     * Creates a {@code Token} with the given parameters that represents an argument.
     * 
     * @param location the location at which this token was declared
     * @param literal the raw value of the declared argument
     * @param lexeme the name of the declared argument
     * @return a token that represents an argument
     */
    public static Token argument(Element location, String literal, String lexeme) {
        return new Token(location, literal, lexeme, Type.ARGUMENT, Set.of());
    }
    
    /**
     * Creates a {@code Token} with the given parameters that represents a literal.
     * 
     * @param location the location at which this token was declared
     * @param literal the raw value of the declared literal
     * @param lexeme the name of the declared literal
     * @param aliases the aliases of the literal
     * @return a token that represents a literal
     */
    public static Token literal(Element location, String literal, String lexeme, Set<String> aliases) {
        return new Token(location, literal, lexeme, Type.LITERAL, aliases);
    }
    
    /**
     * Creates a {@code Token} that is a root.
     * 
     * @return a root token
     */
    public static Token root() {
        return new Token(null, "", "", Type.ROOT, Set.of());
    }
    
    
    /**
     * Creates a {@code Token} with the given parameters.
     * 
     * @param location the location at which this token was declared
     * @param literal the raw value of the declared command
     * @param lexeme the name of the declared command
     * @param type the token type
     * @param aliases the aliases of this declared command
     */
    Token(Element location, String literal, String lexeme, Type type, Set<String> aliases) {
        this.location = location;
        this.lexeme = lexeme;
        this.type = type;
        this.aliases = aliases;
        this.bindings = new EnumMap<>(Binding.class);
        this.children = new HashMap<>();
        this.literal = literal;
    }
    
    
    /**
     * Adds the given child. If a child already exists, the existing child is merged
     * with the given child.
     * 
     * @param environment the environment
     * @param child the child
     * @return the token, or {@code null} if the given child could not be added
     */
    public @Nullable Token add(Environment environment, Token child) {
        var existing = children.get(child.lexeme);
        if (existing == null) {
            children.put(child.lexeme, child);
            return child;
        }
        
        return existing.merge(environment, child);
    } 
    
    /**
     * Merges this token with {@code other}.
     * 
     * @param environment the environment
     * @param other the other token
     * @return this token, or {@code null} if the tokens could not be merged
     */
    @Nullable Token merge(Environment environment, Token other) {
        if (type != other.type) {
            environment.error(location, format(lexeme, "already exists", "an argument and literal in the same scope should not have the same name"));
            return null; 
            
        } else if (type == Type.LITERAL) {
            for (var alias : other.aliases) {
                if (!aliases.add(alias)) {
                    environment.warn(location, "Duplicate alias: " + quote(alias));
                }
            }
        }
        
        return this;
    }
    
    
    /**
     * Binds the given location to this token.
     * 
     * @param environment the environment
     * @param binding the binding type
     * @param location the location to be bound
     */
    public void bind(Environment environment, Binding binding, Element location) {
        var existing = bindings.get(binding);
        if (existing != null) {
            environment.error(existing, binding.article + " " + binding.type + " is already bound to " + quote(toString()));
            
        } else if (type != Type.ARGUMENT && (binding == Binding.TYPE || binding == Binding.SUGGESTIONS)) {
            environment.error(location, binding.article + " " + binding.type + " should not be bound to a literal");
       
        } else {
            bindings.put(binding, location);
        }
    }
    
    
    @Override
    public String toString() {
        return literal;
    }
    
}
