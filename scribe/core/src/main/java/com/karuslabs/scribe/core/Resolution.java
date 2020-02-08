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
package com.karuslabs.scribe.core;

import com.karuslabs.scribe.core.Message.Type;

import java.util.*;


public class Resolution<T> {
    
    Map<String, Object> mapping;
    List<Message<T>> messages;
    
    
    public Resolution() {
        mapping = new HashMap<>();
        messages = new ArrayList<>();
    }
    
    
    public Resolution error(T location, String message) {
        messages.add(Message.error(location, message));
        return this;
    }
    
    public Resolution error(String message) {
        messages.add(new Message(message, Type.ERROR));
        return this;
    }
    
    
    public Resolution warning(T location, String message) {
        messages.add(Message.warning(location, message));
        return this;
    }
    
    public Resolution warning(String message) {
        messages.add(new Message(message, Type.WARNING));
        return this;
    }
    
    
    public Resolution info(T location, String message) {
        messages.add(Message.info(location, message));
        return this;
    }
    
    public Resolution info(String message) {
        messages.add(new Message(message, Type.INFO));
        return this;
    }
    
    
    public Map<String, Object> mapping() {
        return mapping;
    }
    
}
