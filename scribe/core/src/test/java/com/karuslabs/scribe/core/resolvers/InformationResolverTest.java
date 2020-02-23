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

import com.karuslabs.scribe.annotations.Information;
import com.karuslabs.scribe.core.*;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@Information(authors = {"Pante"}, description = "description", url = "http://wwww.repo.karuslabs.com", prefix = "prefix")
class InformationResolverTest {
    
    InformationResolver<Class<?>> resolver = new InformationResolver<>();
    Resolution<Class<?>> resolution = new Resolution<>();
    
    
    @BeforeEach
    void before() {
        resolver.initialize(Project.EMPTY, Extractor.CLASS, resolution);
    }
    
    
    @Test
    void resolve_element() {
        resolver.resolve(InformationResolverTest.class);
        var mapping = resolution.mappings;
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) mapping.get("authors"));
        assertEquals("description", mapping.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mapping.get("website"));
        assertEquals("prefix", mapping.get("prefix"));
    }
    
    
    @Test
    void check() {
        resolver.check(InvalidURL.class.getAnnotation(Information.class), Object.class);
        
        assertEquals(
            Message.error(Object.class, "Invalid URL: htt://wwww.repo.karuslabs.com, htt://wwww.repo.karuslabs.com is not a valid URL"), 
            resolution.messages.get(0)
        );
    }
    
    
    @Information(url = "htt://wwww.repo.karuslabs.com")
    static class InvalidURL {
        
    }
    
    
    @Test
    void resolve_information() {
        resolver.resolve(InformationResolverTest.class.getAnnotation(Information.class));
        var mappings = resolution.mappings;
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) mappings.get("authors"));
        assertEquals("description", mappings.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mappings.get("website"));
        assertEquals("prefix", mappings.get("prefix"));
    }
    
    
    @Test
    void resolve_information_project() {
        resolver.initialize(new Project("", "", "", List.of("Pante"), "project description", "http://wwww.repo.karuslabs.com"), Extractor.CLASS, resolution);
        
        resolver.resolve(Empty.class.getAnnotation(Information.class));
        var mappings = resolution.mappings;
        
        assertEquals(List.of("Pante"), (List<String>) mappings.get("authors"));
        assertEquals("project description", mappings.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", mappings.get("website"));
    }
    
    
    @Test
    void resolve_information_empty() {
        resolver.resolve(Empty.class.getAnnotation(Information.class));
        
        assertTrue(resolution.mappings.isEmpty());
        assertTrue(resolution.messages.isEmpty());
    }
    
    
    @Information
    static class Empty {
        
    }

} 
