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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.annotations.Load;
import com.karuslabs.scribe.core.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.scribe.annotations.Phase.STARTUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


@Load(during = STARTUP, before = {"a"}, optionallyAfter = {"b"}, after = {"c"})
class LoadParserTest {
    
    static final String[] EMPTY = new String[0];
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    LoadParser<Class<?>> parser = new LoadParser<>(environment);
    
    
    @Test
    void parse() {
        parser.parse(LoadParserTest.class);
        var mappings = environment.mappings;
        
        assertEquals(4, mappings.size());
        assertEquals("STARTUP", mappings.get("load"));
        assertArrayEquals(new String[] {"a"}, (String[]) mappings.get("loadbefore"));
        assertArrayEquals(new String[] {"b"}, (String[]) mappings.get("softdepend"));
        assertArrayEquals(new String[] {"c"}, (String[]) mappings.get("depend"));
    }
    
    
    @Test
    void parse_empty() {
        parser.parse(Empty.class);
        var mappings = environment.mappings;
        
        assertEquals(4, mappings.size());
        assertEquals("POSTWORLD", mappings.get("load"));
        assertArrayEquals(EMPTY, (String[]) mappings.get("loadbefore"));
        assertArrayEquals(EMPTY, (String[]) mappings.get("softdepend"));
        assertArrayEquals(EMPTY, (String[]) mappings.get("depend"));
    }
    
    
    @Load
    static class Empty {
        
    }
    
    
    @Test
    void check_errors() {
        parser.check(LoadParserTest.class, new String[] {"a1", "hey!", "sp ace"});
        
        verify(environment).error(LoadParserTest.class, "\"hey!\" is not a valid plugin name, should contain only alphanumeric characters and \"_\"");
        verify(environment).error(LoadParserTest.class, "\"sp ace\" is not a valid plugin name, should contain only alphanumeric characters and \"_\"");
    }

} 
