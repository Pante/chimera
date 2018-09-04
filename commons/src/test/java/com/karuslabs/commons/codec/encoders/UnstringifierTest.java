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

package com.karuslabs.commons.codec.encoders;

import com.karuslabs.commons.codec.decoders.*;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UnstringifierTest {
    
    static final String FOLDER = UnstringifierTest.class.getClassLoader().getResource("codec/encoder").getPath();
    static final File LENIENT = new File(FOLDER, "lenient_unstringified.json");
    static final File JSON = new File(FOLDER, "unstringified.json");
    static final File PROPERTIES = new File(FOLDER, "unstringified.properties");
    static final File YAML = new File(FOLDER, "unstringified.yml");
    
    
    String[] array;
    
    
    @ParameterizedTest
    @MethodSource({"lenient_to_json_provider"})
    void lenient_to_json(Map<String, Object> map) throws InterruptedException {
        Unstringifier.lenient().to(LENIENT, map);
        var results = LenientStringifier.stringify().from(LENIENT);
        
        array = (String[]) results.get("a.b");
        assertArrayEquals(new String[] {"first", "2", "true"}, array);
        
        array = (String[]) results.get("a.b[1].c");
        assertArrayEquals(new String[] {"second", "1", "false"}, array);
        
        array = (String[]) results.get("a.b[2]");
        assertArrayEquals(new String[] {"value"}, array);
        
        assertEquals("third", results.get("e.f"));
        
        assertEquals("fourth", results.get("g"));
    }
    
    static Stream<Map<String, Object>> lenient_to_json_provider() {   
        var array = new LinkedHashMap<String, Object>();
        array.put("a.b", new String[] {"first", "2", "true"});
        array.put("a.b[1].c", new String[] {"second", "1", "false"});
        array.put("a.b[2]", new String[] {"value"});
        array.put("e.f", "third");
        array.put("g", "fourth");
        
        var nested = new LinkedHashMap<String, Object>();
        nested.put("a.b[1].c", new String[] {"second", "1", "false"});
        nested.put("a.b[2]", new String[] {"value"});
        nested.put("a.b", new String[] {"first", "2", "true"});
        nested.put("e.f", "third");
        nested.put("g", "fourth");
        
        return Stream.of(array, nested);
    }
    
    
    @Test
    void to_json() {
        var map = Map.of("a.b[0]", "first", "a.b[1].c[0]", "second", "a.b[1].c[1]", "1", "a.b[1].c[2]", "false", "e.f", "third", "g", "fourth");
        Unstringifier.unstringify().to(JSON, map);
                
        assertEquals(map, Stringifier.stringify().from(JSON));
    }
    
    
    @Test
    void to_properties() {
        var map = Map.of("a", "value", "b", "true");
        Unstringifier.unstringify().to(PROPERTIES, map);
        
        assertEquals(map, Stringifier.stringify().from(PROPERTIES));
    }
    
    
    @Test
    void to_yaml() {
        var map = Map.of("a.b[0]", "first", "a.b[1]", "1", "a.b[2]", "true", "c.d", "second", "e", "third", "g", "");
        Unstringifier.unstringify().to(YAML, map);
        
        assertEquals(map, Stringifier.stringify().from(YAML));
    }
    
    
    @AfterEach
    void delete_lenient() {
        LENIENT.delete();
    }
    
    
    @AfterAll
    static void delete_files() {
        JSON.delete();
        PROPERTIES.delete();
        YAML.delete();
    }
    
}