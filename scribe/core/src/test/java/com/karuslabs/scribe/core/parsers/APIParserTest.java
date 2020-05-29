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

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@API(Version.V1_15)
class APIParserTest {
    
    Environment<Class<?>> environment = StubEnvironment.of(Project.EMPTY);
    APIParser<Class<?>> parser = new APIParser<>(environment);
    
    
    @Test
    void parse() {
        parser.parse(APIParserTest.class);
        
        assertEquals("1.15", environment.mappings.get("api-version"));
        verify(environment, times(0)).warn(any(), any());
    }
    
    
    @Test
    void resolve_inferred() {
        parser.environment = StubEnvironment.of(new Project("", "", "1.15.1", List.of(), "", ""));

        parser.parse(Inferred.class);
        
        assertEquals("1.15", parser.environment.mappings.get("api-version"));
    }
    
    
    @Test
    void resolve_inferred_default() {    
        parser.parse(Inferred.class);
        
        assertEquals("1.13", environment.mappings.get("api-version"));
        verify(environment).warn(Inferred.class, "Could not infer \"api-version\", \"1.13\" will be used instead");
    }
    
    
    @API(Version.INFERRED)
    static class Inferred {
        
    }

}
