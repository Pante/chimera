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

import com.karuslabs.scribe.core.parsers.APIParser;
import com.karuslabs.scribe.maven.plugin.Message;
import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@API(Version.V1_15)
class APIResolverTest {
    
    APIParser<Class<?>> resolver = new APIParser<>();
    Environment<Class<?>> resolution = new Environment();
    
    
    @Test
    void resolve() {
        resolver.initialize(Project.EMPTY, Resolver.CLASS, resolution);
        
        resolver.parse(APIResolverTest.class);
        
        assertEquals("1.15", resolution.mappings.get("api-version"));
        assertTrue(resolution.messages.isEmpty());
    }
    
    
    @Test
    void resolve_inferred() {
        resolver.initialize(new Project("", "", "1.15.1", List.of(), "", ""), Resolver.CLASS, resolution);
        
        resolver.parse(Inferred.class);
        
        assertEquals("1.15", resolution.mappings.get("api-version"));
    }
    
    
    @Test
    void resolve_inferred_default() {
        resolver.initialize(Project.EMPTY, Resolver.CLASS, resolution);
        
        resolver.parse(Inferred.class);
        var message = resolution.messages.get(0);
        
        assertEquals("1.13", resolution.mappings.get("api-version"));
        assertEquals(Message.warning(Inferred.class, "Unable to infer 'api-version', defaulting to '1.13'"), message);
    }
    
    
    @API(Version.INFERRED)
    static class Inferred {
        
    }

}
