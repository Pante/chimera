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
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.Translation;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ParserTest {
    
    private static final ConfigurationSection DEFINITONS = COMMANDS.getConfigurationSection("define");
    
    private Parser parser;
    private Element<Command> command;
    private Element<Completion> completion;
    private Element<Translation> translation;
    
    
    public ParserTest() {
        parser = spy(new Parser(command = mock(Element.class), completion = mock(Element.class), translation = mock(Element.class)));
    }
    
    
    @Test
    public void parse() {
        doNothing().when(parser).parseDefinitions(any(ConfigurationSection.class));
        
        Command aCommand = mock(Command.class);
        doReturn(aCommand).when(command).parse(any(ConfigurationSection.class));
        
        assertEquals(singletonList(aCommand), parser.parse(COMMANDS));
        verify(parser).parseDefinitions(COMMANDS.getConfigurationSection("define"));
    }
    
    
    @Test
    public void parseDefinitons() {
        doNothing().when(parser).parseDefinition(any(ConfigurationSection.class), any(Element.class));
        
        parser.parseDefinitions(DEFINITONS);
        
        verify(parser).parseDefinition(DEFINITONS.getConfigurationSection("completions"), completion);
        verify(parser).parseDefinition(DEFINITONS.getConfigurationSection("commands"), command);
        verify(parser).parseDefinition(DEFINITONS.getConfigurationSection("translations"), translation);
    }
    
    
    @Test
    public void parseDefinition() {
        parser.parseDefinition(DEFINITONS.getConfigurationSection("commands"), command);
        verify(command).define(eq("help"), any(ConfigurationSection.class));
    }
    
}
