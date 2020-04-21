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
package com.karuslabs.commons.command.aot.tokens;

import com.karuslabs.commons.command.aot.Agent;

import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Argument extends Token {

    @Nullable Element type;
    
    
    public Argument(Element site, String context, String value) {
        super(site, context, value, "<" + value + ">");
    }

    @Override
    public <T, R> @Nullable R visit(Visitor<T, R> visitor, T context) {
        return visitor.argument(this, context);
    }

    
    @Override
    public @Nullable Argument merge(Agent agent, Token other) {
        if (other instanceof Argument && value.equals(other.value)) {
            return this;
            
        } else {
            agent.error("Invalid command: " + other + ", command already exists");
            return null;
        }
    }
    
    
    public @Nullable Element type() {
        return type;
    }
    
    public boolean type(Element element) {
        if (type == null) {
            type = element;
            return true;
            
        } else {
            return false;
        }
    }

}
