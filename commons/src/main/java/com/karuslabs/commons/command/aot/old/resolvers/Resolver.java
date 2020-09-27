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

import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.command.CommandSender;


public abstract class Resolver<T extends Element> {

    protected Environment environment;
    protected Elements elements;
    protected Types types;
    protected TypeMirror sender;
    
    
    public Resolver(Environment environment) {
        this.environment = environment;
        this.elements = environment.elements;
        this.types = environment.types;
        this.sender = elements.getTypeElement(CommandSender.class.getName()).asType();
    }
    
    
    public abstract void resolve(T element, Identifier token);
    
    
    protected final TypeMirror specialize(Class<?> type, TypeMirror... parameters) {
        return types.getDeclaredType(elements.getTypeElement(type.getName()), parameters);
    }
    
}
