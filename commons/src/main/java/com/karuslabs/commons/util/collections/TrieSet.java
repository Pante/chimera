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


/**
 * A {@code Set} backed by a {@code Trie}.
 */
public class TrieSet extends AbstractSet<String> {
    
    static final Object PRESENT = new Object();
    
    
    Trie<Object> trie;
    
    
    /**
     * Creates a {@code TrieSet}.
     */
    public TrieSet() {
        trie = new Trie<>();
    }
    
    
    /**
     * Returns the elements that start with the given prefix
     * 
     * @param prefix the prefix
     * @return the elements which start with the given prefix
     */
    public Set<String> startsWith(String prefix) {
        return trie.prefixedKeys(prefix);
    }
    
        
    @Override
    public boolean add(String string) {
        return trie.put(string, PRESENT) == null;
    }
    
    @Override
    public boolean contains(Object object) {
        return trie.containsKey(object);
    }
    
    
    @Override
    public boolean remove(Object object) {
        return trie.remove(object, PRESENT);
    }
    
    
    @Override
    public Iterator<String> iterator() {
        return trie.keySet().iterator();
    }

    @Override
    public int size() {
        return trie.size();
    }
    
}
