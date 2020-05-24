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

import java.util.stream.Stream;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ArgumentLexerTest {
    
    ArgumentLexer lexer = new ArgumentLexer();
    Environment environment = mock(Environment.class);
    Element location = mock(Element.class);
    
    
    @Test
    void lex() {
        var tokens = lexer.lex(environment, location, "<argument>");
        var token = tokens.get(0);
        
        assertEquals(1, tokens.size());
        assertEquals(location, token.location);
        assertEquals("<argument>", token.literal);
        assertEquals("argument", token.lexeme);
    }
    
    
    @ParameterizedTest
    @CsvSource({"<<arg>", "<arg>>"})
    void lex_warning(String raw) {
        var tokens = lexer.lex(environment, location, raw);
        var token = tokens.get(0);
        
        assertEquals(1, tokens.size());
        assertEquals(location, token.location);
        assertEquals(raw, token.literal);
        assertEquals(raw.substring(1, raw.length() - 1), token.lexeme);
        verify(environment).warn(location, "\"" + raw + "\" contains trailing \"<\"s or \">\"s");
    }
    
    
    @ParameterizedTest
    @MethodSource("lex_error_parameters")
    void lex_errors(String raw, String error) {
        assertEquals(EMPTY_LIST, lexer.lex(environment, location, raw));
        verify(environment).error(location, error);
    }
    
    static Stream<Arguments> lex_error_parameters() {
        return Stream.of(
            of("arg>", "\"arg>\" is an invalid argument, should be enclosed by \"<\" and \">\""),
            of("<arg", "\"<arg\" is an invalid argument, should be enclosed by \"<\" and \">\""),
            of("<arg|a>", "\"<arg|a>\" contains \"|\", an argument should not have aliases"),
            of("<>", "\"<>\" is empty, an argument should not be empty")
        );
    }

} 
