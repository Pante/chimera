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
import static org.mockito.Mockito.*;


class SingleParserTest {
    
    Environment<String> environment = spy(new StubEnvironment<>());
    StubParser resolver = new StubParser(environment);
    
    
    @Test
    void check_one() {
        resolver.check(Set.of("a"));
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void check_many() {
        resolver.check(Set.of("a", "b"));
        
        verify(environment).error("a", "Project contains 2 @hello annotations, should contain one @hello annotation");
        verify(environment).error("a", "Project contains 2 @hello annotations, should contain one @hello annotation");
    }
    
    static class StubParser extends SingleParser<String> {
    
        StubParser(Environment<String> environment) {
            super(environment, Set.of(Command.class), "hello");
        }

        @Override
        protected void parse(String type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
