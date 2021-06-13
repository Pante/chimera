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
import com.karuslabs.utilitary.Logger;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.*;

import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandLexerTest {

    Memoizer memoizer = new Memoizer();
    CommandLexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(memoizer));
    Logger logger = mock(Logger.class);
    Element element = mock(Element.class);
    
    @Test
    void lex() {
        var tokens = lexer.lex(logger, element, "a|b <c>");
        
        verifyNoInteractions(logger);
        
        var literal = tokens.get(0);
        var argument = tokens.get(1);
        
        assertEquals(2, tokens.size());
        assertEquals("a", literal.identity().name());
        assertEquals(LITERAL, literal.identity().type());
        assertArrayEquals(new String[] {"b"}, literal.aliases());
        assertEquals("a|b", literal.lexeme());
        
        assertEquals("c", argument.identity().name());
        assertEquals(ARGUMENT, argument.identity().type());
        assertEquals(0, argument.aliases().length);
        assertEquals("<c>", argument.lexeme());
    }
    
    @Test
    void lex_error() {
        assertEquals(0, lexer.lex(logger, element, "   ").size());
        verify(logger).error(element, "Command should not be blank");
    }
    
}
