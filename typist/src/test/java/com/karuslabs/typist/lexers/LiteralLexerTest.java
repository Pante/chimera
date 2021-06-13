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

import java.util.stream.Stream;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.typist.Identity.Type.LITERAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class LiteralLexerTest {

    LiteralLexer lexer = LiteralLexer.aliasable(new Memoizer());
    Logger logger = mock(Logger.class);
    Element element = mock(Element.class);
    
    @ParameterizedTest
    @MethodSource("lex_error_parameters")
    void lex_error(String lexeme, String reason, String resolution) {
        assertEquals(0, lexer.lex(logger, element, lexeme).size());
        
        verify(logger).error(element, lexeme, reason, resolution);
    }
    
    static Stream<Arguments> lex_error_parameters() {
        return Stream.of(
            of("<something", "contains '<' and/oe '>'", "a literal should not contain '<' and '>'"),
            of("something>", "contains '<' and/oe '>'", "a literal should not contain '<' and '>'"),
            of("|a", "contains a blank name", "should not be blank"),
            of("  |a", "contains a blank name", "should not be blank")
        );
    }
    
}

class AliasableLexerTest {

    LiteralLexer lexer = LiteralLexer.aliasable(new Memoizer());
    Logger logger = mock(Logger.class);
    Element element = mock(Element.class);
    
    @Test
    void lex() {
        var tokens = lexer.lex(logger, element, "a|b|c");
        var literal = tokens.get(0);
        
        verifyNoInteractions(logger);
        
        assertEquals(1, tokens.size());
        assertEquals("a", literal.identity().name());
        assertEquals(LITERAL, literal.identity().type());
        assertArrayEquals(new String[] {"b", "c"}, literal.aliases());
    }
    
    @Test
    void lex_name_alias_clash() {
        assertEquals(0, lexer.lex(logger, element, "a|a").size());
        
        verify(logger).error(element, "a|a", "contains an alias that is the same as its name");
    }
    
    @ParameterizedTest
    @MethodSource("lex_error_parameters")
    void lex_error(String lexeme, String reason, String resolution) {
        assertEquals(0, lexer.lex(logger, element, lexeme).size());
        
        verify(logger).error(element, lexeme, reason, resolution);
    }
    
    static Stream<Arguments> lex_error_parameters() {
        return Stream.of(
            of("a|", "contains a blank alias", "should not be blank"),
            of("a|  ", "contains a blank alias", "should not be blank")
        );
    }
    
}

class SingleLiteralLexerTest {
    
    LiteralLexer lexer = LiteralLexer.single(new Memoizer());
    Logger logger = mock(Logger.class);
    Element element = mock(Element.class);
    
    @Test
    void lex() {
        var tokens = lexer.lex(logger, element, "a");
        var literal = tokens.get(0);
        
        verifyNoInteractions(logger);
        
        assertEquals(1, tokens.size());
        assertEquals("a", literal.identity().name());
        assertEquals(LITERAL, literal.identity().type());
        assertEquals(0, literal.aliases().length);
    }
    
    @ParameterizedTest
    @MethodSource("lex_error_parameters")
    void lex_error(String lexeme, String reason, String resolution) {
        assertEquals(0, lexer.lex(logger, element, lexeme).size());
        
        verify(logger).error(element, lexeme, reason, resolution);
    }
    
    static Stream<Arguments> lex_error_parameters() {
        return Stream.of(
            of("a|", "contains aliases", "should not contain aliases in this context"),
            of("a|b", "contains aliases", "should not contain aliases in this context")
        );
    }
    
}
