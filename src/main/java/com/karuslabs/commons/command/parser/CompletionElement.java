/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.completion.CachedCompletion;

import java.util.*;
import javax.annotation.Nullable;
import com.karuslabs.commons.command.completion.Completion;


public class CompletionElement extends Element<Completion> {    
    
    public CompletionElement() {
        this(new HashMap<>());
    }
    
    public CompletionElement(Map<String, Completion> completions) {
        super(completions);
        definitions.put("PLAYER_NAMES", Completion.PLAYER_NAMES);
        definitions.put("WORLD_NAMES", Completion.WORLD_NAMES);
    }

    
    @Override
    public Completion parse(Object value) {
        if (value instanceof List) {
            return new CachedCompletion((List<String>) value);
            
        } else {
            return super.parse(value);
        }
    }
    
}
