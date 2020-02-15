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

import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.core.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UniqueResolverTest {
    
    StubResolver resolver = new StubResolver();
    Resolution<String> resolution = mock(Resolution.class);
    
    
    @Test
    void check_one() {
        resolver.initialize(Project.EMPTY, null, resolution);
        
        assertTrue(resolver.check(Set.of("a")));
        verifyNoInteractions(resolution);
    }
    
    
    @Test
    void check_many() {
        resolver.initialize(Project.EMPTY, null, resolution);
        
        assertFalse(resolver.check(Set.of("a", "b")));
        verify(resolution).error("a", "Invalid number of @hello annotations, plugin must contain only one @hello annotation");
        verify(resolution).error("b", "Invalid number of @hello annotations, plugin must contain only one @hello annotation");
    }

}

class StubResolver extends UniqueResolver<String> {
    
    StubResolver() {
        super(Set.of(Command.class), "hello");
    }

    @Override
    protected void resolve(String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
