/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.core.resolvers;

import com.karuslabs.scribe.annotations.Load;
import com.karuslabs.scribe.core.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.karuslabs.scribe.annotations.Phase.STARTUP;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@Load(during = STARTUP, before = {"a"}, optionallyAfter = {"b"}, after = {"c"})
class LoadResolverTest {
    
    LoadResolver<Class<?>> resolver = new LoadResolver<>();
    Resolution<Class<?>> resolution = new Resolution<>();
    
    
    @BeforeEach
    void before() {
        resolver.initialize(Project.EMPTY, Extractor.CLASS, resolution);
    }
    
    
    @Test
    void resolve() {
        resolver.resolve(LoadResolverTest.class);
        var mappings = resolution.mappings;
        
        assertEquals(4, mappings.size());
        assertEquals("STARTUP", mappings.get("load"));
        assertArrayEquals(new String[] {"a"}, (String[]) mappings.get("loadbefore"));
        assertArrayEquals(new String[] {"b"}, (String[]) mappings.get("softdepend"));
        assertArrayEquals(new String[] {"c"}, (String[]) mappings.get("depend"));
    }
    
    
    @Test
    void resolve_empty() {
        resolver.resolve(Empty.class);
        var mappings = resolution.mappings;
        
        assertEquals(4, mappings.size());
        assertEquals("POSTWORLD", mappings.get("load"));
        assertArrayEquals(new String[] {}, (String[]) mappings.get("loadbefore"));
        assertArrayEquals(new String[] {}, (String[]) mappings.get("softdepend"));
        assertArrayEquals(new String[] {}, (String[]) mappings.get("depend"));
    }
    
    
    @Load
    static class Empty {
        
    }
    
    
    @Test
    void check_errors() {
        resolver.check(LoadResolverTest.class, new String[] {"a1", "hey!", "sp ace"});
        
        assertEquals(
            Message.error(LoadResolverTest.class, "Invalid name: 'hey!', name must contain only alphanumeric characters and '_'"),
            resolution.messages.get(0)
        );
        assertEquals(
            Message.error(LoadResolverTest.class, "Invalid name: 'sp ace', name must contain only alphanumeric characters and '_'"),
            resolution.messages.get(1)
        );
    }

} 
