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
package com.karuslabs.scribe.core;

import com.karuslabs.scribe.annotations.Command;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ResolverTest {
    
    Resolver resolver = spy(new StubResolver());
    Project project = mock(Project.class);
    Extractor<String> extractor = mock(Extractor.class);
    Resolution<String> resolution = mock(Resolution.class);
    
    
    @Test
    void initialize() {
        resolver.initialize(project, extractor, resolution);
        
        assertEquals(project, resolver.project);
        assertEquals(extractor, resolver.extractor);
        assertEquals(resolution, resolver.resolution);
    }
    
    
    @Test
    void resolve_set() {
        var set = Set.of("a", "b");
        
        resolver.resolve(set);
        
        verify(resolver).check(set);
        verify(resolver, times(2)).resolve(any(String.class));
        verify(resolver).clear();
    }
    
    
    @Test
    void check() {
        var set = mock(Set.class);
        
        resolver.check(set);
        verifyNoInteractions(set);
    }
    
    
    @Test
    void annotations() {
        assertEquals(Set.of(Command.class), resolver.annotations());
    }

} 

class StubResolver extends Resolver<String> {
    
    public StubResolver() {
        super(Set.of(Command.class));
    }
    
    @Override
    protected void resolve(String type) {
        
    }
    
}
