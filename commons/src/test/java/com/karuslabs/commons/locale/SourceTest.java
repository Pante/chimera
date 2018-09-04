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

package com.karuslabs.commons.locale;

import com.karuslabs.commons.codec.decoders.Stringifier;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class SourceTest {
    
    @Test
    void embedded() {
        var map = Stringifier.stringify().from(Source.embedded("codec/decoder").stream("encoded.properties"), "properties");
        assertEquals(Map.of("a", "value", "b", "true"), map);
    }
    
    
    @Test
    void folder() {
        var map = Stringifier.stringify().from(Source.folder(getClass().getClassLoader().getResource("codec/decoder").getPath()).stream("encoded.properties"), "properties");
        assertEquals(Map.of("a", "value", "b", "true"), map);
    }
    
    
    @Test
    void folder_directory() throws IOException {
        try (var stream = Source.folder("codec/").stream("decoder")) {
            assertNull(stream);
        }
    }
    
    
    @Test
    void folder_nonexistent() throws IOException {
        try (var stream = Source.folder("codec/decoder").stream("nonexistent.json")) {
            assertNull(stream);
        }
    }
    
    
}
