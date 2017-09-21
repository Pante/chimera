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

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.*;
import com.karuslabs.commons.locale.Translation;

import java.util.*;
import java.io.File;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CommandElementTest {
    
    private static final ConfigurationSection COMMAND = COMMANDS.getConfigurationSection("commands.command");
    
    
    private CommandElement element;
    private Completion completion;
    private Translation translation;
    
    
    public CommandElementTest() {
        Map<String, Completion> completions = new HashMap<>();
        completions.put("completion", completion = new CachedCompletion());
        
        Map<String, Translation> translations = new HashMap<>();
        translations.put("translation", translation = when(mock(Translation.class).copy()).thenReturn(Translation.NONE).getMock());
        
        element = spy(new CommandElement(null, new CompletionElement(completions), new TranslationElement(new File(""), translations)));
    }
    
    
    @Test
    public void parseConfigurationSection() {
        Map<String, Command> subcommands = new HashMap<>();
        Command subcommand = mock(Command.class);
        subcommands.put("subcommand", subcommand);
        doReturn(subcommands).when(element).parseCommands(COMMAND.getConfigurationSection("subcommands"));
        
        Map<Integer, Completion> completions = new HashMap<>();
        completions.put(1, completion);
        doReturn(completions).when(element).parseCompletions(COMMAND.getConfigurationSection("completions"));
        
        doReturn(translation).when(element).parseTranslation(COMMAND.getConfigurationSection("translation"));
        
        Command command = element.parseConfigurationSection(COMMAND);
        
        assertEquals("command", command.getName());
        assertEquals("description", command.getDescription());
        assertEquals("usage", command.getUsage());
        assertEquals(asList("a", "b", "c"), command.getAliases());
        assertNull(command.getPlugin());
        assertTrue(CommandExecutor.NONE == command.getExecutor());
        assertTrue(Translation.NONE == command.getTranslation());
        assertTrue(subcommands == command.getSubcommands());
        assertTrue(completions == command.getCompletions());
        assertEquals("command.permission", command.getPermission());
        assertEquals("message", command.getPermissionMessage());
    }
    
    
    @Test
    public void parseCommands() {
        Command a = mock(Command.class);
        doReturn(a).when(element).parse("help");
        
        Command b = when(mock(Command.class).getAliases()).thenReturn(singletonList("b-alias")).getMock();
        doReturn(b).when(element).parse(any(ConfigurationSection.class));
        
        Map<String, Command> commands = element.parseCommands(COMMAND.getConfigurationSection("subcommands"));
        
        assertEquals(3, commands.size());
        assertTrue(a == commands.get("help"));
        assertTrue(b == commands.get("b"));
    }
    
    
    @Test
    public void parseCommands_Null() {
        assertTrue(element.parseCommands(null).isEmpty());
    }
    
    
    @Test
    public void parseCompletions() {
        Map<Integer, Completion> completions = element.parseCompletions(COMMAND.getConfigurationSection("completions"));
        
        assertEquals(2, completions.size());
        assertTrue(completion == completions.get(0));
        assertEquals(asList("a", "b", "c"), ((CachedCompletion) completions.get(2)).getCompletions());
    }
    
    
    @Test
    public void parseCompletions_Null() {
        assertTrue(element.parseCompletions(null).isEmpty());
    }
    
    
    @Test
    public void parseTranslation() {
        assertTrue(translation == element.parseTranslation(COMMAND.get("translation")));
    }
    
    
    @Test
    public void parseTranslation_Null() {
        assertTrue(Translation.NONE == element.parseTranslation(null));
    }
    
}
