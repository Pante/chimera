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

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


class TrieEntryTest {
    
    static final TrieEntry<String> ENTRY = new TrieEntry<>('a', null, "key", "value");
    
    TrieEntry<String> entry = new TrieEntry((char) 0, null);
    
    
    
    @Test
    void get_null() {
        assertNull(entry.get(' '));
    }
    
    
    @ParameterizedTest
    @MethodSource("characters")
    void add(char character) {
        entry.add(character);
        assertEquals(character, entry.get(character).character);
        
        assertEquals(1, entry.children);
    }
    
    
    @ParameterizedTest
    @MethodSource("characters")
    void add_key_value(char character) {
        entry.add(character, "key", "value");
        
        var child = entry.get(character);
        
        assertEquals(character, child.character);
        assertEquals("key", child.getKey());
        assertEquals("value", child.getValue());
        assertEquals(1, entry.children);
    }

    
    
    @ParameterizedTest
    @MethodSource("characters")
    void set_size_increase(char character) {
        assertNull(entry.set(character, "key1", "value1"));
        assertEquals(1, entry.children);
        
        assertNull(entry.set((char) (character + 1), "key2", "value2"));
        assertEquals(2, entry.children);
        
        var added = entry.get((char) (character + 1));

        assertEquals(character + 1, added.character);
        assertEquals("key2", added.getKey());
        assertEquals("value2", added.getValue());
    }
    
    
    @ParameterizedTest
    @MethodSource("characters")
    void set_size_same(char character) {
        assertNull(entry.set(character, "key1", "value1"));
        assertEquals(1, entry.children);
        
        var old = entry.set(character, "key2", "value2");
        assertEquals(1, entry.children);
        
        assertEquals(character, old.character);
        assertEquals("key1", old.key);
        assertEquals("value1", old.value);
        
        var added = entry.get(character);
        
        assertEquals(character, added.character);
        assertEquals("key2", added.getKey());
        assertEquals("value2", added.getValue());
    }
    
    
    @ParameterizedTest
    @MethodSource("characters")
    void remove_size_decrease(char character) {
        assertNull(entry.set(character, "key1", "value1"));
        assertEquals(1, entry.children);
        
        var removed = entry.remove(character);
        assertEquals(0, entry.children);
        
        assertEquals(character, removed.character);
        assertEquals("key1", removed.getKey());
        assertEquals("value1", removed.getValue());
    }
    
    
    @ParameterizedTest
    @MethodSource("characters")
    void remove_size_same(char character) {
        assertNull(entry.set(character, "key1", "value1"));
        assertEquals(1, entry.children);
        
        assertNull(entry.remove((char) (character + 1)));
        assertEquals(1, entry.children);
    }
    
    // @CsvSource sucks and does not handle ü properly
    static Stream<Character> characters() {
        return Stream.of('a', 'ü');
    }
    
    
    @Test
    void clear() {
        entry.add('a');
        entry.add('ü');
        assertEquals(2, entry.children);
        
        entry.clear();
        
        assertEquals(0, entry.children);
        assertNull(entry.get('a'));
        assertNull(entry.get('ü'));
    }
    
    
    @Test
    void setValue() {
        entry.value = "old";
        
        assertEquals("old", entry.setValue("new"));
        assertEquals("new", entry.value);
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void equals(Object other, boolean expected) {
        assertEquals(expected, ENTRY.equals(other));
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void hashCode(Object other, boolean expected) {
        assertEquals(expected, ENTRY.hashCode() == Objects.hashCode(other));
    }
    
    
    static Stream<Arguments> equality_parameters() {
        return Stream.of(
            of(ENTRY, true),
            of(new TrieEntry<>(' ', null, "key", "value"), true),
            of(new Object(), false),
            of(null, false),
            of(new TrieEntry<>(' ', null, "key", "invalid"), false),
            of(new TrieEntry<>(' ', null, "invalid", "value"), false),
            of(new TrieEntry<>(' ', null, "invalidkey", "invalidvalue"), false)
        );
    }
    
    
    @Test
    void to_string() {
        assertEquals("e=mc2", new TrieEntry<>(' ', null, "e", "mc2").toString());
    }

} 
