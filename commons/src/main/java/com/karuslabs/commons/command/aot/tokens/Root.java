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

import org.checkerframework.checker.nullness.qual.Nullable;


public class Root extends Token {

    public Root() {
        super(null, "", "[root]", "[root]");
    }

    
    @Override
    public <T, R> @Nullable R visit(Visitor<T, R> visitor, T context) {
        return visitor.root(this, context);
    }
    
    
    @Override
    public @Nullable Token add(Agent agent, Token child) {
        if (child instanceof Literal) {
            return super.add(agent, child);
            
        } else {
            agent.error("Invalid command: " + child + ", command must be a literal");
            return null;
        }
    }
    
    @Override
    public @Nullable Root merge(Agent agent, Token other) {
        if (this == other) {
            return this;
            
        } else {
            throw new UnsupportedOperationException("Root cannot be merged");
        }
    }

}
