/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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

package com.karuslabs.commons.codec.decoders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class LenientStringifierTest {
   
    static final String ENCODED = "codec/decoder/encoded.";
    
    String[] array;
    
    
    @Test
    void visit_json() {
        var results = LenientStringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "json"), "json");
        assertEquals(5, results.size());
        
        array = (String[]) results.get("a.b");
        assertArrayEquals(new String[] {"first", "2", "true"}, array);
        
        array = (String[]) results.get("a.b[1].c");
        assertArrayEquals(new String[] {"second", "1", "false"}, array);
        
        array = (String[]) results.get("a.b[2]");
        assertArrayEquals(new String[] {"value"}, array);
        
        assertEquals("third", results.get("e.f"));
        
        assertEquals("fourth", results.get("g"));
    }
        
    
    @Test
    void visit_properties() {
        var results = LenientStringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "properties"), "properties");
        assertEquals(2, results.size());
        
        assertEquals("value", results.get("a"));
        assertEquals("true", results.get("b"));
    }
    
    
    @Test
    void visit_yaml() {
        var results = LenientStringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "yml"), "yml");
        
        assertEquals(4, results.size());
        
        array = (String[]) results.get("a.b");
        assertArrayEquals(new String[] {"first", "1", "true"}, array);
        
        assertEquals("second", results.get("c.d"));
        
        assertEquals("third", results.get("e"));
        
        array = (String[]) results.get("f");
        assertArrayEquals(new String[] {}, array);
    }
    
}
