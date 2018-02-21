/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.Command;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.configuration.Yaml.*;
import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;


class ParserTest {
    
    Token<Command> token = mock(Token.class);
    
    
    @Test
    void parse() {
        new Parser(token).parse(COMMANDS);
        
        verify(token).from(COMMANDS.getConfigurationSection("commands"), "brush");
        verify(token).from(COMMANDS.getConfigurationSection("commands"), "test");
    }
    
    
    @Test
    void parse_EMPTY_LIST() {
        assertSame(EMPTY_LIST, new Parser(token).parse(INVALID));
        verify(token, never()).from(any(), any());
    }
    
}
