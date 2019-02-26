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
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Trie<V> extends AbstractMap<String, V> {
    
    TrieEntry<V> root;
    int modifications;
    int size;
    
    @Nullable EntrySet entries;
    @Nullable KeySet keys;
    @Nullable ValueCollection values;
    
    
    public Trie() {
        root = new TrieEntry<>((char) 0, null);
        modifications = 0;
        size = 0;
    }
    
    
    public Set<Entry<String, V>> prefixEntries(String prefix) {
        return startsWith(prefix, entry -> entry, new HashSet<>(), Collections.emptySet());
    }
    
    public Set<String> prefixedKeys(String prefix) {
        return startsWith(prefix, entry -> entry.getKey(), new HashSet<>(), Collections.emptySet());
    }
    
    public Collection<V> prefixedValues(String prefix) {
        return startsWith(prefix, entry -> entry.getValue(), new ArrayList<>(), Collections.emptyList());
    }
    
    
    <C extends Collection<T>, T> C startsWith(String prefix, Function<Entry<String, V>, T> visitor, C collection, C empty) {
        var entry = root;
        for (var character : prefix.toCharArray()) {
            entry = entry.get(character);
            if (entry == null) {
                return empty;
            }
        }
        
        accumulate(root, visitor, collection);
        return collection;
    }
    
    <C extends Collection<T>, T> void accumulate(TrieEntry<V> entry, Function<Entry<String, V>, T> visitor, C leaves) {
        if (entry.key != null) {
            leaves.add(visitor.apply(entry));
        }
        
        if (entry.ascii != null) {
            for (var child : entry.ascii) {
                if (child != null) {
                    accumulate(child, visitor, leaves);
                }
            }
        }
        
        if (entry.expanded != null) {
            for (var child : entry.expanded.values()) {
                accumulate(child, visitor, leaves);
            }
        }
    }


    @Override
    public boolean containsValue(Object value) {
        return contains(root, value);
    }
    
    boolean contains(TrieEntry<V> entry, Object value) {
        if (entry.key != null && Objects.equals(entry.value, value)) {
            return true;
        }
        
        if (entry.ascii != null) {
            for (var child : entry.ascii) {
                if (child != null && contains(child, value)) {
                    return true;
                }
            }
            
        }
        
        if (entry.expanded != null) {
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
        return getEntry(key) != null;
    }
    
    @Override
    public @Nullable V get(Object key) {
        var entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }
    
    @Nullable TrieEntry<V> getEntry(Object key) {
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
    public @Nullable V put(String key, V value) {
        var entry = root;
        var array = key.toCharArray();
        
        int i = 0;
        for (; i < array.length - 1; i++) {
            var next = entry.get(array[i]);
            if (next != null) {
                entry = next;
                
            } else {
                break;
            }
        }
        
        for (; i < array.length - 1; i++) {
            entry = entry.add(array[i]);
        }
        
        var replaced = entry.set(array[array.length - 1], key, value);
        modifications++;
        
        if (replaced == null) {
            size++;
            return null;
            
        } else {
            return replaced.value;
        }
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        for (var entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    
    @Override
    public @Nullable V remove(Object key) {
        var entry = getEntry(key);
        return entry != null ? removeEntry(entry) : null;
    }
    
    @Nullable V removeEntry(TrieEntry<V> entry) {
        var removed = entry;
        if (entry.children == 0) {
            do {
                entry.parent.remove(entry.character);
                entry = entry.parent;
            } while (entry.key == null && entry != root && entry.parent.children == 1);
            size--;
            
        } else {
            entry.key = null;
            entry.value = null;
        }
        
        modifications++;
        return removed.value;
    }
    

    @Override
    public void clear() {
        size = 0;
        modifications++;
        root.clear();
    }
    
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    
    @Override
    public Set<Entry<String, V>> entrySet() {
        if (entries == null) {
            entries = new EntrySet();
        }
        
        return entries;
    }
    
    @Override
    public Set<String> keySet() {
        if (keys == null) {
            keys = new KeySet();
        }
        
        return keys;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValueCollection();
        }
        return values;
    }
    
    
    class EntrySet extends AbstractSet<Entry<String, V>> {
        
        @Override
        public boolean contains(Object object) {
            return getEntry(object) != null;
        }
        
        @Override
        public boolean remove(Object object) {
            return removeEntry((TrieEntry<V>) object) != null;
        }
        
        @Override
        public Iterator<Entry<String, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public int size() {
            return size;
        }
        
    }
    
    class KeySet extends AbstractSet<String> {
        
        @Override
        public boolean contains(Object object) {
            return containsKey(object);
        }
        
        @Override
        public boolean remove(Object object) {
            return Trie.this.remove(object) != null;
        }
        
        @Override
        public Iterator<String> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return size;
        }
        
    }
    
    class ValueCollection extends AbstractCollection<V> {

        @Override
        public boolean contains(Object object) {
            return containsValue(object);
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }
        
    }
    
    
    abstract class TrieIterator<T> implements Iterator<T> {
        
        Deque<TrieEntry<V>> stack;
        @Nullable TrieEntry<V> returned;
        int expectedModifications;
        
        
        TrieIterator() {
            stack = new ArrayDeque<>();
            children(root);
            returned = null;
            expectedModifications = modifications;
        }
        
        
        @Override
        public T next() {
             if (expectedModifications != modifications) {
                throw new ConcurrentModificationException();
                
            } else if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }
             
            return get(returned = nextEntry());
        }
        
        TrieEntry<V> nextEntry() {
            TrieEntry<V> entry;
            do {
                entry = stack.pollLast();
                if (entry.children > 0) {
                    children(entry);
                }
                
            } while (entry.key != null);
            
            return entry;
        }

        void children(TrieEntry<V> entry) {
            if (entry.ascii != null) {
                for (var child : entry.ascii) {
                    if (child != null) {
                        stack.add(child);
                    }
                }
            }
            
            if (entry.expanded != null) {
                for (var child : entry.expanded.values()) {
                    stack.add(child);
                }
            }
        }
                
        abstract T get(TrieEntry<V> entry);
        
        
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public void remove() {
            if (expectedModifications != modifications) {
                throw new ConcurrentModificationException();
                
            } else if (returned == null) {
                throw new IllegalStateException();
            }
            
            removeEntry(returned);
            expectedModifications = modifications;
            returned = null;
        }
        
    }
    
    class EntryIterator extends TrieIterator<Entry<String, V>> {

        @Override
        Entry<String, V> get(TrieEntry<V> entry) {
            return entry;
        }
        
    }
    
    class KeyIterator extends TrieIterator<String> {

        @Override
        String get(TrieEntry<V> entry) {
            return entry.key;
        }
        
    }
    
    class ValueIterator extends TrieIterator<V> {

        @Override
        V get(TrieEntry<V> entry) {
            return entry.value;
        }
        
    }
    
}
