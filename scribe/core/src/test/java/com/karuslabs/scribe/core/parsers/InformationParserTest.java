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

import com.karuslabs.scribe.annotations.Information;
import com.karuslabs.scribe.core.*;

import java.util.List;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Information(authors = {"Pante"}, description = "description", url = "http://wwww.repo.karuslabs.com", prefix = "prefix")
class InformationParserTest {
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    InformationParser<Class<?>> parser = new InformationParser<>(environment);
    
    
    @Test
    void parse_class() {
        parser.parse(InformationParserTest.class);
        var mapping = environment.mappings;
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) mapping.get("authors"));
        assertEquals("description", mapping.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mapping.get("website"));
        assertEquals("prefix", mapping.get("prefix"));
    }
    
    
    @Test
    void check() {
        parser.check(InvalidURL.class.getAnnotation(Information.class), Object.class);
        
        verify(environment).error(Object.class, "\"htt://wwww.repo.karuslabs.com\" is not a valid URL");
    }
    
    
    @Information(url = "htt://wwww.repo.karuslabs.com")
    static class InvalidURL {
        
    }
    
    
    @Test
    void parse_information() {
        parser.parse(InformationParserTest.class.getAnnotation(Information.class));
        var mappings = environment.mappings;
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) mappings.get("authors"));
        assertEquals("description", mappings.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mappings.get("website"));
        assertEquals("prefix", mappings.get("prefix"));
    }
    
    
    @Test
    void parse_information_project() {
        parser.environment = StubEnvironment.of((new Project("", "", "", List.of("Pante"), "project description", "http://wwww.repo.karuslabs.com")));
        
        parser.parse(Empty.class.getAnnotation(Information.class));
        var mappings = parser.environment.mappings;
        
        assertEquals(List.of("Pante"), (List<String>) mappings.get("authors"));
        assertEquals("project description", mappings.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mappings.get("website"));
    }
    
    
    @Test
    void parse_information_empty() {
        parser.parse(Empty.class.getAnnotation(Information.class));
        
        verify(environment, times(0)).error(any(), any());
        verify(environment, times(0)).warn(any(), any());
        
        assertTrue(environment.mappings.isEmpty());
    }
    
    
    @Information
    static class Empty {
        
    }

} 
