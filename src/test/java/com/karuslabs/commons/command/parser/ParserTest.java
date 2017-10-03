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

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class ParserTest {
    
    private static Command stub = mock(Command.class);
    
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
    
    
    @ParameterizedTest
    @CsvSource({"declare, 1", "declare.commands.help.aliases, 0"})
    public void parseDeclarations(String path, int times) {
        doNothing().when(parser).parseDeclaration(any(), any());
        ConfigurationSection declarations = COMMANDS.getConfigurationSection("declare");
        
        parser.parseDeclarations(COMMANDS.getConfigurationSection(path));
        
        verify(parser, times(times)).parseDeclaration(command, declarations.getConfigurationSection("commands"));
        verify(parser, times(times)).parseDeclaration(translation, declarations.getConfigurationSection("translations"));
        verify(parser, times(times)).parseDeclaration(completion, declarations.getConfigurationSection("completions"));
    }
    
    
    @ParameterizedTest
    @CsvSource({"declare.commands, 1", "declare.commands.help.aliases, 0"})
    public void parseDeclaration(String path, int times) {
        parser.parseDeclaration(command, COMMANDS.getConfigurationSection(path));
        
        verify(command, times(times)).declare(COMMANDS.getConfigurationSection("declare.commands"), "help");
    }
    
    
    @ParameterizedTest
    @MethodSource("parseCommands_parameters")
    public void parseCommands(String path, int times, List<Command> expected) {
        ConfigurationSection config = COMMANDS.getConfigurationSection(path);
        when(command.parse(any(), any())).thenReturn(stub);
        
        assertEquals(expected, parser.parseCommands(config));
        verify(command, times(times)).parse(config, "brush");
    }
    
    static Stream<Arguments> parseCommands_parameters() {
        return Stream.of(of("commands", 1, singletonList(stub)), of("declare.commands.help.aliases", 0 , EMPTY_LIST));
    }
    
}
