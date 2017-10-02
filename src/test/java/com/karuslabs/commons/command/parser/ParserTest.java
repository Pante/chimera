/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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

import org.junit.jupiter.api.*;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Collections.EMPTY_LIST;
import static org.mockito.Mockito.*;


public class ParserTest {
    
    private CommandElement command = when(mock(CommandElement.class).parse(any(), any())).thenReturn(mock(Command.class)).getMock();
    private TranslationElement translation = mock(TranslationElement.class);
    private CompletionElement completion = mock(CompletionElement.class);
    private Parser parser = spy(new Parser(command, translation, completion));
    
    
    @Test
    public void parse() {
        doNothing().when(parser).parseDeclarations(any());
        doReturn(EMPTY_LIST).when(parser).parseCommands(any());
        
        parser.parse(COMMANDS);
        
        verify(parser).parseDeclarations(COMMANDS.getConfigurationSection("declarations"));
        verify(parser).parseCommands(COMMANDS.getConfigurationSection("commands"));
    }
    
}
