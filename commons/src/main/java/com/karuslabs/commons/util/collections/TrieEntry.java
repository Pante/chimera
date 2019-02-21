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


class TrieEntry<T> extends AbstractMap.SimpleEntry<String, T> {

    private static final int VISIBLE = 95;

    
    @Nullable TrieEntry<T> parent;
    @Nullable TrieEntry<T>[] ascii;
    @Nullable Map<Character, TrieEntry<T>> expanded;

    
    TrieEntry(@Nullable TrieEntry<T> parent) {
        super(null, null);
        this.parent = parent;
        ascii = (TrieEntry<T>[]) new Object[VISIBLE];
        expanded = null;
    }

    TrieEntry(@Nullable TrieEntry<T> parent, String key, T value) {
        super(key, value);
        this.parent = parent;
        ascii = null;
        expanded = null;
    }

    
    @Nullable TrieEntry<T> get(char index) {
        if (ascii != null && 31 < index && index < 127) {
            return ascii[index - 32];

        } else if (expanded != null) {
            return expanded.get(index);

        } else {
            return null;
        }
    }

    @Nullable TrieEntry<T> set(char index, @Nullable TrieEntry<T> node) {
        if (31 < index && index < 127) {
            if (ascii == null) {
                ascii = (TrieEntry<T>[]) new Object[VISIBLE];
            }

            var old = ascii[index];
            ascii[index] = node;

            return old;

        } else {
            if (expanded == null) {
                expanded = new HashMap<>();
            }

            return expanded.put(index, node);
        }
    }

}

