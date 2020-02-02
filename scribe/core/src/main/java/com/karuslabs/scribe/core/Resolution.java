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

import java.util.*;


public class Resolution {
    
    private Map<String, Object> mapping;
    private List<String> warnings;
    private List<String> errors;
    
    
    
    public Resolution() {
        this(new HashMap<>(), new ArrayList<>(), new ArrayList<>());
    }
    
    public Resolution(Map<String, Object> mapping, List<String> warnings, List<String> errors) {
        this.mapping = mapping;
        this.warnings = warnings;
        this.errors = errors;
    }
    
    
    public Resolution warn(String warning) {
        warnings.add(warning);
        return this;
    }
    
    public Resolution error(String error) {
        errors.add(error);
        return this;
    } 
    
    
    public Map<String, Object> mapping() {
        return mapping;
    }
    
    public List<String> warnings() {
        return warnings;
    }
    
    public List<String> errors() {
        return errors;
    }
    
}
