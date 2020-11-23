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
package com.karuslabs.puff.generation;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SourceTest {
    
    Source source = new Source();
    
    
    @Test
    void arguments() {
        assertEquals("(1, 2, 3, 4)", Source.arguments(1, "2", 3, "4"));
    }
    
    
    @Test
    void assign() {
        assertEquals("var a = b;" + System.lineSeparator(), source.assign("a", "b").toString());
    }
    
    @Test
    void cast() {
        assertEquals("var a = (B) c;" + System.lineSeparator(), source.cast("a", "B", "c").toString());
    }
    
    @Test
    void pack() {
        assertEquals("package a.b.c;" + System.lineSeparator(), source.pack("a.b.c").toString());
    }
    
    @Test
    void include() {
        assertEquals("import a.b.c;" + System.lineSeparator(), source.include("a.b.c").toString());
    }
    
    
    @Test
    void line_newline() {
        assertEquals(System.lineSeparator(), source.line().toString());
    }
    
    @Test
    void line_element() {
        assertEquals("    test" + System.lineSeparator(), source.indentation(4).line("test").toString());
    }
    
    @Test
    void line_elements() {
        assertEquals("    abc" + System.lineSeparator(), source.indentation(4).line("a", "b", "c").toString());
    }
    
    
    @Test
    void append() {
        assertEquals("a", source.append("a").toString());
    }
    
    
    @Test
    void indent() {
        assertEquals(" ".repeat(8) + System.lineSeparator(), source.indent().indent().line("").toString());
    }
    
    @Test
    void unindent() {
        assertEquals(" ".repeat(4) + System.lineSeparator(), source.indent().indent().unindent().line("").toString());
    }
    
    @Test
    void indentation() {
        assertEquals(" ".repeat(5) + System.lineSeparator(), source.indentation(5).line("").toString());
    }
    
    
    @Test
    void charAt() {
        assertEquals('B', source.append("AB").charAt(1));
    }
    
    
    @Test
    void subSequence() {
        assertEquals("BC", source.append("ABCD").subSequence(1, 3));
    }
    
    
    @Test
    void length() {
        assertEquals(4, source.append("ABCD").length());
    }
    
}
