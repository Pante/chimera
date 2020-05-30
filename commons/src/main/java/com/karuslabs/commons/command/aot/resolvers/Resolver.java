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
package com.karuslabs.commons.command.aot.resolvers;

import com.karuslabs.commons.command.aot.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.command.CommandSender;


/**
 * A {@code Resolver} checks the signature of an {@code Element} and if valid, binds 
 * it to a token.
 * @param <T> the type of the element
 */
public abstract class Resolver<T extends Element> {
    
    /**
     * The current environment.
     */
    protected Environment environment;
    /**
     * A utility for manipulating {@code Elements}.
     */
    protected Elements elements;
    /**
     * A utility for manipulating {@code TypeMirror}s.
     */
    protected Types types;
    /**
     * A {@code TypeMirro} that represents a {@code CommandSender}.
     */
    protected TypeMirror sender;
    
    
    /**
     * Creates a {@code Resolver} with the given parameters.
     * 
     * @param environment the environment
     */
    public Resolver(Environment environment) {
        this.environment = environment;
        this.elements = environment.elements;
        this.types = environment.types;
        this.sender = elements.getTypeElement(CommandSender.class.getName()).asType();
    }
    
    
    /**
     * Checks the signature of the given element and if valid, binds it to {@code token}.
     * 
     * @param element the element
     * @param token the token
     */
    public abstract void resolve(T element, Token token);
    
    
    /**
     * Creates a specialized generic type.
     * @param type the generic type
     * @param parameters the type parameters
     * @return a specialized generic type
     */
    protected final TypeMirror specialize(Class<?> type, TypeMirror... parameters) {
        return types.getDeclaredType(elements.getTypeElement(type.getName()), parameters);
    }
    
}
