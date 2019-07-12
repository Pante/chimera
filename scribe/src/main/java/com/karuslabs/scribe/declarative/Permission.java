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
package com.karuslabs.scribe.declarative;

import com.karuslabs.scribe.Default;

import java.util.*;


public class Permission {
    
    private String name;
    private String description;
    private Default implicit;
    private Map<String, Permission> children;
    
    
    public Permission(String name) {
        this.name = name;
        description = "";
        implicit = Default.OP;
        children = new HashMap<>();
    }
    
    
    public Permission description(String description) {
        this.description = description;
        return this;
    }
    
    public Permission implicit(Default value) {
        this.implicit = value;
        return this;
    }
    
    public Permission children(Permission... children) {
        for (var child : children) {
            this.children.put(child.name(), child);
        } 
        return this;
    }

    
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Default implicit() {
        return implicit;
    }

    public Map<String, Permission> children() {
        return children;
    }
    
}
