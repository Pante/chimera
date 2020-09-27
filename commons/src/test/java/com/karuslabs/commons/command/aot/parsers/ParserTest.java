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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.commons.command.aot.lexers.LiteralLexer;
import com.karuslabs.commons.command.aot.lexers.CommandLexer;
import com.karuslabs.commons.command.aot.lexers.ArgumentLexer;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.lexers.*;

import java.util.*;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ParserTest {
    
    Environment environment = mock(Environment.class);
    Parser parser = new StubParser(environment, new CommandLexer(new ArgumentLexer(), new LiteralLexer()));
    
    
    @Test
    void valid() {
        assertTrue(parser.valid(List.of(Identifier.literal(null, "", "", Set.of()))));
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void valid_empty() {
        assertFalse(parser.valid(List.of()));
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void valid_invalid() {
        var argument = Identifier.argument(mock(Element.class), "<argument>", "argument");
        
        assertFalse(parser.valid(List.of(argument)));
        verify(environment).error(argument.location, "\"<argument>\" is at an invalid position, command should not start with an argument");
    }

} 

class StubParser extends Parser {

    public StubParser(Environment environment, Lexer lexer) {
        super(environment, lexer);
    }

    @Override
    public void parse(Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
