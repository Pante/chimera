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
package com.karuslabs.commons.util.collections;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import org.checkerframework.checker.nullness.qual.Nullable;


class TrieEntry<T> extends SimpleEntry<String, T> {
    
    static final int PRINTABLE = 95;
    static final int OFFSET = 32;
    
    
    @Nullable TrieEntry<T> parent;
    @Nullable TrieEntry<T>[] ascii;
    @Nullable Map<Character, TrieEntry<T>> expanded;
    int descendants;
    
    
    TrieEntry() {
        this(null, null, null);
    }
    
    TrieEntry(@Nullable TrieEntry<T> parent, String key, T value) {
        super(key, value);
        this.parent = parent;
        ascii = null;
        expanded = null;
        descendants = 0;
    }
    
    
    @Nullable TrieEntry<T> get(char character) {
        if (ascii != null && 31 < character && character < 127) {
            return ascii[character - OFFSET];
            
        } else if (expanded != null) {
            return expanded.get(character);
            
        } else {
            return null;
        }
    }
    
    @Nullable TrieEntry<T> add(char character, String key, T value) {
        descendants++;
        if (31 < character && character < 127) {
            if (ascii == null) {
                ascii = (TrieEntry<T>[]) new Object[PRINTABLE];
            }
            
            var old = ascii[character - OFFSET];
            ascii[character - OFFSET] = new TrieEntry<>(this, key, value);
            return old;
        
        } else {
            if (expanded == null) {
                expanded = new HashMap<>();
            }
            
            return expanded.put(character, new TrieEntry<>(this, key, value));
        }
    }
    
    @Nullable TrieEntry<T> remove(char character) {
        TrieEntry<T> removed = null;
        if (ascii != null && 31 < character && character < 127) {
            removed = ascii[character - OFFSET];
            ascii[character - OFFSET] = null;
        
        } else if (expanded != null) {
            removed = expanded.remove(character);   
        }
        
        if (removed != null) {
            descendants--;
        }
        
        return removed;
    }
    
    void clear() {
        ascii = null;
        expanded = null;
    }
    
}
