/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe;

import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.processing.Messager;


public abstract class Resolver<T> {
    
    public static final Pattern COMMAND = Pattern.compile("^ ");
    public static final Pattern PERMISSION = Pattern.compile("\\w+(\\.\\w+)*(.\\*)?");
    public static final Pattern VERSIONING = Pattern.compile("(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[a-zA-Z\\d][-a-zA-Z.\\d]*)?(\\+[a-zA-Z\\d][-a-zA-Z.\\d]*)?$");
    public static final Pattern WORD = Pattern.compile("\\w+");
    
    protected Messager messager;
    
    
    public Resolver(Messager messager) {
        this.messager = messager;
    }
    
    
    public abstract void resolve(T element, Map<String, Object> map);
    
    public void close() {
        
    }
    
}
