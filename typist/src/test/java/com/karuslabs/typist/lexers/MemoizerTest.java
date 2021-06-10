/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.lexers;

import com.karuslabs.typist.lexers.Lexer.Memoizer;

import org.junit.jupiter.api.*;

import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.*;

class MemoizerTest { 

    Memoizer memoizer = new Memoizer();
    
    @Test
    void argument() {
        var token = memoizer.argument("something", "<something>");
        assertEquals(0, token.aliases().length);
        assertEquals("something", token.identity().name());
        assertEquals(ARGUMENT, token.identity().type());
        assertEquals("<something>", token.lexeme());
    }
    
    @Test
    void literal() {
        var token = memoizer.literal("something", "something|a", new String[] {"a"});
        assertArrayEquals(new String[] {"a"}, token.aliases());
        assertEquals("something", token.identity().name());
        assertEquals(LITERAL, token.identity().type());
        assertEquals("something|a", token.lexeme());
    }
    
    @Test
    void memoize() {
        var a = memoizer.argument("1", "2");
        var b = memoizer.argument("1", "2");
        
        assertSame(a.identity(), b.identity());
    }
    
}
