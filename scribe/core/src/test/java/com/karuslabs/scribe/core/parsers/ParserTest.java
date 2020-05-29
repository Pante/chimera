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

import com.karuslabs.scribe.annotations.Command;
import com.karuslabs.scribe.core.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ParserTest {
    
    Parser<String> parser = spy(new StubResolver(new StubEnvironment<>()));
    
       
    @Test
    void parse_set() {
        var set = Set.of("a", "b");
        
        parser.parse(set);
        
        verify(parser).check(set);
        verify(parser, times(2)).parse(any(String.class));
        verify(parser).clear();
    }
    
    
    @Test
    void check() {
        var set = mock(Set.class);
        
        parser.check(set);
        verifyNoInteractions(set);
    }
    
    
    @Test
    void annotations() {
        assertEquals(Set.of(Command.class), parser.annotations());
    }

} 

class StubResolver extends Parser<String> {
    
    public StubResolver(Environment environment) {
        super(environment, Set.of(Command.class));
    }
    
    @Override
    protected void parse(String type) {
        
    }
    
}
