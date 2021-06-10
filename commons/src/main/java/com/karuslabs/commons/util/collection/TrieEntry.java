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
package com.karuslabs.commons.util.collection;

import com.karuslabs.annotations.Lazy;

import java.util.*;
import java.util.Map.Entry;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A trie entry. Each entry represents a character in a string. Each entry points
 * to the next character in a string. A entry that represents the last character in 
 * a string also contains the string and, optionally, a value.
 * 
 * Children entries that contain an ASCII character are stored in an array to
 * improve look-up. Non-ASCII characters are stored in a {@code HashMap} instead.
 * 
 * Strings that share the same starting characters can similarly share the same 
 * starting entries. 
 * 
 * @param <T> the type of the value
 */
final class TrieEntry<T> implements Entry<String, T> {
    
    static final int PRINTABLE = 95;
    static final int OFFSET = 32;
    
    final char character;
    final @Nullable TrieEntry<T> parent;
    @Nullable String key;
    @Nullable T value;
    @Lazy TrieEntry<T>[] ascii;
    @Lazy Map<Character, TrieEntry<T>> expanded;
    int children;
    
    /**
     * Creates an empty {@code TrieEntry} that contains the given character and parent.
     * 
     * @param character the character that this {@code TrieEntry} represents
     * @param parent the parent {@code TrieEntry}
     */
    TrieEntry(char character, @Nullable TrieEntry<T> parent) {
        this(character, parent, null, null);
    }
    
    /**
     * Creates a {@code TrieEntry} with the given values.
     * 
     * @param character the character that this {@code TrieEntry} represents
     * @param parent the parent {@code TrieEntry}
     * @param key the string
     * @param value the value
     */
    TrieEntry(char character, @Nullable TrieEntry<T> parent, String key, T value) {
        this.character = character;
        this.parent = parent;
        this.children = 0;
        this.key = key;
        this.value = value;
    }
    
    
    @Nullable TrieEntry<T> child(char character) {
        if (ascii != null && 31 < character && character < 127) {
            return ascii[character - OFFSET];
            
        } else if (expanded != null) {
            return expanded.get(character);
            
        } else {
            return null;
        }
    }
    
    
    @Nullable TrieEntry<T> add(char character) {
        return add(character, null, null);
    }
    
    @Nullable TrieEntry<T> add(char character, String key, T value) {
        children++;
        if (31 < character && character < 127) {
            if (ascii == null) {
                ascii = (TrieEntry<T>[]) new TrieEntry<?>[PRINTABLE];
            }
            
            return ascii[character - OFFSET] = new TrieEntry<>(character, this, key, value);
            
        } else {
            if (expanded == null) {
                expanded = new HashMap<>();
            }
            
            var entry = new TrieEntry(character, this, key, value);
            expanded.put(character, entry);
            
            return entry;
        }
    }
    
    
    @Nullable TrieEntry<T> set(char character, String key, T value) {
        TrieEntry<T> old;
        if (31 < character && character < 127) {
            if (ascii == null) {
                ascii = (TrieEntry<T>[]) new TrieEntry<?>[PRINTABLE];
            }
            
            old = ascii[character - OFFSET];
            ascii[character - OFFSET] = new TrieEntry<>(character, this, key, value);

        } else {
            if (expanded == null) {
                expanded = new HashMap<>();
            }
            
            old = expanded.put(character, new TrieEntry(character, this, key, value));
        }
        
        if (old == null) {
            children++;
        }
        
        return old;
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
            children--;
        }

        return removed;
    }

    void clear() {
        children = 0;
        ascii = null;
        expanded = null;
    }
    
    
    @Override
    public @Nullable String getKey() {
        return key;
    }

    @Override
    public @Nullable T getValue() {
        return value;
    }

    @Override
    public @Nullable T setValue(T value) {
        var replaced = this.value;
        this.value = value;

        return replaced;
    }
    

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof TrieEntry entry && Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return key + "=" + value;
    }
    
}
