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

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TrieTest {
    
    Trie<String> trie = new Trie<>();
    Trie<String> populated = new Trie<>();
    
    
    TrieTest() {
        populated.put("app", "app_value");
        populated.put("apple", "apple_value");
        populated.put("application", "application_value");
        populated.put("banana", null);
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
        trie = new Trie<String>();
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

} 
