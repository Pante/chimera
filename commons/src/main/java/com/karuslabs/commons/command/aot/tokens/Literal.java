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

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Literal extends Token {

    public Set<String> aliases;
    
    
    public Literal(Element site, String context, String value, Set<String> aliases) {
        super(site, context, value, String.join("|", value, String.join("|", aliases)));
        this.aliases = aliases;
    }

    
    @Override
    public <T, R> @Nullable R visit(Visitor<T, R> visitor, T context) {
        return visitor.literal(this, context);
    }

    
    @Override
    public @Nullable Token merge(Agent agent, Token other) {
        if (!(other instanceof Literal)) {
            agent.error("Invalid command: " + other + ", command already exists");
            return null;
        }
        
        for (var alias : ((Literal) other).aliases) {
            if (!aliases.add(alias)) {
                agent.warn("'" + alias + "' already exists");
            }
        }

        return this;
    }

}
