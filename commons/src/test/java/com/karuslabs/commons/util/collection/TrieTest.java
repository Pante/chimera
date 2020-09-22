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

import com.karuslabs.commons.util.collection.Trie.TrieIterator;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    
    static final Trie<String> TRIE = new Trie<>();
    static {
        TRIE.put("key", "value");
        TRIE.put("other", null);
    }
    
    Trie<String> trie = new Trie<>();
    Trie<String> populated = new Trie<>();
    
    
    TrieTest() {
        populated.put("app", "app_value");
        populated.put("apple", "apple_value");
        populated.put("application", "application_value");
        populated.put("banana", null);
    }
    
    
    @Test
    void prefixedEntries() {
        var entries = populated.prefixEntries("app");
        assertEquals(3, entries.size());
        assertTrue(entries.contains(new TrieEntry<>('p', null, "app", "app_value")));
        assertTrue(entries.contains(new TrieEntry<>('e', null, "apple", "apple_value")));
        assertTrue(entries.contains(new TrieEntry<>('n', null, "application", "application_value")));
    }
    
    @Test
    void prefixedKeys() {
        var keys = populated.prefixedKeys("app");
        assertEquals(3, keys.size());
        assertTrue(keys.contains("app"));
        assertTrue(keys.contains("apple"));
        assertTrue(keys.contains("application"));
    }
    
    @Test
    void prefixedValues() {
        var values = populated.prefixedValues("app");
        assertEquals(3, values.size());
        assertTrue(values.contains("app_value"));
        assertTrue(values.contains("apple_value"));
        assertTrue(values.contains("application_value"));
    }
    
    @Test
    void prefixed() {
        populated.put("applyingÜee", "value");
        assertEquals(1, populated.prefixed("applyin", entry -> entry, new ArrayList<>()).size());
    }
    
    @Test
    void prefixed_null() {
        assertTrue(populated.prefixed("applying", entry -> entry, new ArrayList<>()).isEmpty());
    }
    
    @ParameterizedTest
    @CsvSource({", true", "apple_value, true", "value, true", "apply_value, false"})
    void containsValue(String value, boolean expected) {
        populated.put("applÜe", "value");
        assertEquals(expected, populated.containsValue(value));
    }
    
    @ParameterizedTest
    @CsvSource({"app, true", "banana, true", "applicant, false"})
    void containsKey(String key, boolean expected) {
        assertEquals(expected, populated.containsKey(key));
    }
    
    @ParameterizedTest
    @CsvSource({"application, application_value", "applicant, ", "appl, "})
    void get(String key, String expected) {
        assertEquals(expected, populated.get(key));
    }
    
    @Test
    void get_throws_exception() {
        assertEquals("Null keys are not permitted in a trie", assertThrows(NullPointerException.class, () -> trie.get(null)).getMessage());
    }  
    
    @Test
    void putAll() {
        trie.putAll(Map.of("key1", "value1", "key2", "value2"));
        assertEquals(2, trie.size());
        assertEquals(2, trie.modifications);
        
        assertEquals("value1", trie.get("key1"));
        assertEquals("value2", trie.get("key2"));
    }
    
    @Test
    void put_new() {
        trie = new Trie<>();
        assertTrue(trie.isEmpty());
        
        assertNull(trie.put("a", "old"));
        assertEquals("old", trie.get("a"));
        
        assertEquals(1, trie.size());
        assertEquals(1, trie.modifications);
        
        assertNull(trie.put("abc", "new"));
        assertEquals("new", trie.get("abc"));
        
        assertEquals(2, trie.size());
        assertEquals(2, trie.modifications);
    }
    
    @Test
    void put_replacement() {
        trie = new Trie<String>();
        assertTrue(trie.isEmpty());
        
        assertNull(trie.put("a", "old"));
        assertEquals("old", trie.get("a"));
        assertEquals(1, trie.size());
        assertEquals(1, trie.modifications);
        
        assertEquals("old", trie.put("a", "new"));
        assertEquals("new", trie.get("a"));
        assertEquals(1, trie.size());
        assertEquals(2, trie.modifications);
    }
    
    @Test
    void remove_preserve_parent_chain() {
        assertEquals("application_value", populated.remove("application"));
        assertEquals(3, populated.size());
        assertEquals(5, populated.modifications);
        
        assertNull(populated.get("application"));        
        assertEquals("app_value", populated.get("app"));
        
        assertEquals(1, populated.getEntry("app").children);
    }
    
    @Test
    void remove_preserve_child_chain() {
        assertEquals("app_value", populated.remove("app"));
        assertEquals(3, populated.size());
        assertEquals(5, populated.modifications);
        
        assertEquals("application_value", populated.get("application"));
        assertNull(populated.get("app"));
    }
    
    @Test
    void remove_preserve_root() {
        populated.remove("banana");
        
        assertEquals("app_value", populated.get("app"));
    }
    
    @Test
    void clear() {
        populated.clear();
        
        assertTrue(populated.isEmpty());
        assertEquals(5, populated.modifications);
        assertNull(populated.get("app"));
    }
    
    @Test
    void entryset_contains() {
        var entries = populated.entrySet();
        assertEquals(4, entries.size());
        
        assertTrue(entries.contains(new TrieEntry<>('p', null, "app", "app_value")));
        assertFalse(entries.contains(new TrieEntry<>('e', null, "invalid", "apple_value")));
        assertFalse(entries.contains(new TrieEntry<>('e', null, "apple", "invalid")));
        assertFalse(entries.contains(new TrieEntry<>('X', null, "application", "appliction_value")));
    }
    
    @ParameterizedTest
    @CsvSource({"app, app_value, true, 3", "apple, invalid_value, false, 4", "appli, application_value, false, 4"})
    void entryset_remove(String key, String value, boolean expected, int size) {
        var entries = populated.entrySet();
        assertEquals(4, entries.size());

        assertEquals(expected, entries.remove(new TrieEntry<>(' ', null, key, value)));
        assertEquals(size, entries.size());
    }
    
    @Test
    void keyset_contains() {
        var keys = populated.keySet();
        assertEquals(4, keys.size());
        
        assertTrue(keys.contains("app"));
        assertFalse(keys.contains("invalid"));
    }
    
    @ParameterizedTest
    @CsvSource({"app, true, 3", "appl, false, 4"})
    void keyset_remove(String key, boolean expected, int size) {
        var keys = populated.keySet();
        assertEquals(4, keys.size());
        
        assertEquals(expected, keys.remove(key));
        assertEquals(size, keys.size());
    }
    
    @Test
    void values_contains() {
        var values = populated.values();
        assertEquals(4, values.size());
        
        assertTrue(values.contains("app_value"));
        assertTrue(values.contains(null));
        assertFalse(values.contains("invalid"));
    }
    
    @ParameterizedTest
    @CsvSource({"app_value, true, 3", ", true, 3", "application, false, 4"})
    void values_remove(String value, boolean expected, int size) {
        var values = populated.values();
        assertEquals(4, values.size());
        
        assertEquals(expected, values.remove(value));
        assertEquals(size, values.size());
    }
    
    @Test
    void trie_iterator_next_throws_concurrent_exception() {
        var iterator = populated.keySet().iterator();
        populated.put("new", "value");
        
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }
    
    @Test
    void trie_iterator_next_throws_empty_exception() {
        var iterator = trie.keySet().iterator();
        
        assertThrows(NoSuchElementException.class, iterator::next);
    }
    
    @Test
    void trie_iterator_next() {
        var keys = Set.of("app", "apple", "application", "anÜb", "banana");
        populated.put("anÜb", "value");
        
        int counter = 0;
        for (var key : populated.keySet()) {
            counter++;
            assertTrue(keys.contains(key));
        }
        
        assertEquals(5, counter);
    }
    
    @Test
    void trie_iterator_remove_throws_concurrent_exception() {
        var iterator = populated.keySet().iterator();
        populated.remove("banana");
        
        assertThrows(ConcurrentModificationException.class, iterator::remove);
    }
    
    @Test
    void trie_iterator_remove_throws_state_exception() {
        var iterator = populated.keySet().iterator();
        iterator.next();
        iterator.remove();
        
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void trie_iterator_remove() {
        var iterator = (TrieIterator) populated.keySet().iterator();
        var key = iterator.next();
        iterator.remove();
        
        assertFalse(populated.containsKey(key));
        assertEquals(populated.modifications, iterator.expectedModifications);
    }
    
    @Test
    void entryset_iterator() {
        var iterator = populated.entrySet().iterator();
        assertTrue(iterator.next() instanceof TrieEntry<?>);
    }
    
    @Test
    void keyset_iterator() {
        var iterator = populated.keySet().iterator();
        assertTrue(Set.of("app", "apple", "application", "banana").contains(iterator.next()));
    }
    
    @Test
    void values_iterator() {
        var iterator = populated.values().iterator();
        
        var set = new HashSet<String>();
        Collections.addAll(set, null, "app_value", "apple_value", "application_value");

        assertTrue(set.contains(iterator.next()));
    }

} 
