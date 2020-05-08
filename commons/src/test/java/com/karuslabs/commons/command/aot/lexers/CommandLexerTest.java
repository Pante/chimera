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
import com.karuslabs.commons.command.aot.Type;

import javax.lang.model.element.Element;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static java.util.Collections.EMPTY_LIST;


@ExtendWith(MockitoExtension.class)
class CommandLexerTest {
    
    CommandLexer lexer = new CommandLexer(new ArgumentLexer(), new LiteralLexer());
    Environment environment = mock(Environment.class);
    Element location = mock(Element.class);
    
    
    @ParameterizedTest
    @CsvSource({"a <b>", "a       <b>"})
    void lex(String value) {
        var tokens = lexer.lex(environment, location, value);
        var a = tokens.get(0);
        var b = tokens.get(1);
        
        assertEquals(2, tokens.size());
        assertEquals(Type.LITERAL, a.type);
        assertEquals(Type.ARGUMENT, b.type);
    }
    
    
    @ParameterizedTest
    @CsvSource({"''", "'      '"})
    void lex_errors(String value) {
        assertEquals(EMPTY_LIST, lexer.lex(environment, location, value));
        verify(environment).error(location, "Command should not be blank");
    }
} 
