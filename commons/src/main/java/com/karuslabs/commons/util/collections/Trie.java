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

import org.checkerframework.checker.nullness.qual.Nullable;


public class Trie<T> implements Map<String, T> {
    
    private TrieEntry<T> root;
    private int size;
        
    
    @Override
    public boolean containsValue(Object value) {
        return contains(root, value);
    }
    
    boolean contains(TrieEntry<T> entry, Object value) {
        if (entry != root && ((entry.getValue() == null && value == null) || entry.getValue().equals(value))) {
            return true;
        }
        
        if (entry.ascii != null) {
            for (var child : entry.ascii) {
                if (child != null && contains(child, value)) {
                    return true;
                }
            }
            
        } else if (entry.expanded != null) {
            for (var child : entry.expanded.values()) {
                if (contains(child, value)) {
                    return true;
                }
            }
            
        }
        
        return false;
    }
    
    
    @Override
    public boolean containsKey(Object key) {
        return entry(key) != null;
    }
    
    @Override
    public @Nullable T get(Object key) {
        var entry = entry(key);
        return entry == null ? null: entry.getValue();
    } 
    
    @Nullable TrieEntry<T> entry(Object key) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted in a Trie");
        }
        
        var entry = root;
        for (char character : ((String) key).toCharArray()) {
            entry = entry.get(character);
            if (entry == null) {
                return null;
            }
        }
        
        return entry;
    }
    
    @Override
    public T put(String key, T value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted in a trie");
        }
        
        var entry = root;
        var characters = key.toCharArray();
        
        int i = 0;
        for (; i < characters.length; i++) {
            var next = entry.get(characters[i]);
            if (next != null) {
                entry = next;
                
            } else {
                break;
            }
        }
        
        for (; i < characters.length - 1; i++) {
            entry.add(characters[i], null, null);
            entry = entry.get(characters[i]);
        }
        
        var replaced = entry.add(characters[characters.length - 1], key, value);
        return replaced != null ? replaced.getValue() : null;
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        for (var entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    
    @Override
    public T remove(Object key) {
        var entry = entry(key);
        if (entry == null) {
            return null;
        }
        
        var leaf = entry;
        var characters = ((String) key).toCharArray();
        for (int i = characters.length; i >= 0; i--) {
            entry = entry.parent;
            entry.remove(characters[i]);
            if (entry.descendants > 0) {
                break;
            }
        }
        
        return leaf.getValue();
    }

    
    @Override
    public void clear() {
        root.clear();
    }

    
    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<T> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    
    class EntrySet<T> extends AbstractSet<Entry<String, T>> {

        @Override
        public Iterator<Entry<String, T>> iterator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int size() {
            return size;
        }
        
    }
    
}
