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

import com.karuslabs.utilitary.Logger;

import java.util.stream.Stream;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.typist.Identity.Type.ARGUMENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class ArgumentLexerTest {
    
    ArgumentLexer lexer = new ArgumentLexer();
    Logger logger = mock(Logger.class);
    Element element = mock(Element.class);
    
    @Test
    void lex() {
        var tokens = lexer.lex(logger, element, "<something>");
        var argument = tokens.get(0);
        
        verifyNoInteractions(logger);
        
        assertEquals(1, tokens.size());
        assertEquals(0, argument.aliases().length);
        assertEquals("something", argument.identity().name());
        assertEquals(ARGUMENT, argument.identity().type());
        assertEquals("<something>", argument.lexeme());
    }
    
    @ParameterizedTest
    @MethodSource("lex_long_error_parameters")
    void lex_long_error(String lexeme, String reason, String resolution) {
        assertTrue(lexer.lex(logger, element, lexeme).isEmpty());
        verify(logger).error(element, lexeme, reason, resolution);
    }
    
    static Stream<Arguments> lex_long_error_parameters() {
        return Stream.of(
            of("<something", "is not an argument", "should be surrounded by '<' and '>'"),
            of("something>", "is not an argument", "should be surrounded by '<' and '>'"),
            of("<a|b>", "contains '|'", "argument should not contain aliases")
            
        );
    }
    
    @ParameterizedTest
    @MethodSource("lex_short_error_parameters")
    void lex_short_error(String lexeme, String reason) {
        assertTrue(lexer.lex(logger, element, lexeme).isEmpty());
        verify(logger).error(element, lexeme, reason);
    }
    
    static Stream<Arguments> lex_short_error_parameters() {
        return Stream.of(
            of("<>", "should not be blank"),
            of("<    >", "should not be blank"),
            of("<<something>", "contains unnecessary '<' and/or '>'"),
            of("<something>>", "contains unnecessary '<' and/or '>'")
        );
    }

}