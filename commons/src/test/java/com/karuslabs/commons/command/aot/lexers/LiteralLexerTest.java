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
package com.karuslabs.commons.command.aot.lexers;

import com.karuslabs.commons.command.aot.Environment;

import java.util.Set;
import java.util.stream.Stream;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

import static java.util.Collections.EMPTY_LIST;


@ExtendWith(MockitoExtension.class)
class LiteralLexerTest {
    
    LiteralLexer lexer = new LiteralLexer();
    Environment environment = mock(Environment.class);
    Element location = mock(Element.class);
    
    
    @Test
    void lex() {
        var tokens = lexer.lex(environment, location, "a|b|c");
        var token = tokens.get(0);
        
        verifyNoInteractions(environment);
        
        assertEquals(1, tokens.size());
        assertEquals(location, token.location);
        assertEquals("a|b|c", token.literal);
        assertEquals("a", token.lexeme);
        assertEquals(Set.of("b", "c"), token.aliases);
    }
    
    
    @Test
    void lex_warnings() {
        var tokens = lexer.lex(environment, location, "a|b|b");
        var token = tokens.get(0);
        
        verify(environment).warn(location, "Duplicate alias: \"b\"");
        
        assertEquals(1, tokens.size());
        assertEquals(location, token.location);
        assertEquals("a|b|b", token.literal);
        assertEquals("a", token.lexeme);
        assertEquals(Set.of("b"), token.aliases);
    }
    
    
    @ParameterizedTest
    @MethodSource("lex_errors_parameters")
    void lex_errors(String value, String error) {
        assertEquals(EMPTY_LIST, lexer.lex(environment, location, value));
        verify(environment).error(location, error);
    }
    
    static Stream<Arguments> lex_errors_parameters() {
        return Stream.of(
            of("|", "\"|\" contains an empty literal alias or name, should not be empty"),
            of("||", "\"||\" contains an empty literal alias or name, should not be empty"),
            of("a|b|", "\"a|b|\" contains an empty literal alias or name, should not be empty"),
            of("|a|b", "\"|a|b\" contains an empty literal alias or name, should not be empty"),
            of("<a|b", "\"<a|b\" contains \"<\"s and \">\"s, a literal should not contain \"<\"s and \">\"s"),
            of("a>|b", "\"a>|b\" contains \"<\"s and \">\"s, a literal should not contain \"<\"s and \">\"s"),
            of("a|<b", "\"a|<b\" contains \"<\"s and \">\"s, a literal should not contain \"<\"s and \">\"s"),
            of("a|b>", "\"a|b>\" contains \"<\"s and \">\"s, a literal should not contain \"<\"s and \">\"s")
        );
    }

} 
