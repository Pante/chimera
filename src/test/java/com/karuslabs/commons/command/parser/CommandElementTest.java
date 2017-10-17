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
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class CommandElementTest {
    
    Plugin plugin = mock(Plugin.class);
    CommandsElement commands = when(mock(CommandsElement.class).parse(any(), any())).thenReturn(new HashMap<>()).getMock();
    TranslationElement translation = when(mock(TranslationElement.class).parse(any(), any())).thenReturn(MessageTranslation.NONE).getMock();
    CompletionsElement completions = when(mock(CompletionsElement.class).parse(any(), any())).thenReturn(new HashMap<>()).getMock();
    CommandElement element = new CommandElement(plugin, commands, translation, completions);
    
    
    @ParameterizedTest
    @CsvSource({"true, declare", "false, declare.commands.help.aliases"})
    void check(boolean expected, String key) {
        assertEquals(expected, element.check(COMMANDS, key));
    }
    
    
    @Test
    void handle() {
        ConfigurationSection config = COMMANDS.getConfigurationSection("commands.brush");
        
        Command command = element.handle(COMMANDS, "commands.brush");
        assertEquals("brush", command.getName());
        assertEquals("selects the brush tool", command.getDescription());
        assertEquals("/brush [option]", command.getUsage());
        assertEquals(singletonList("b"), command.getAliases());
        assertSame(plugin, command.getPlugin());
        assertSame(CommandExecutor.NONE, command.getExecutor());
        assertEquals(MessageTranslation.NONE, command.getTranslation());
        assertEquals(EMPTY_MAP, command.getSubcommands());
        assertEquals(EMPTY_MAP, command.getCompletions());
        
        verify(commands).parse(config, "subcommands");
        verify(translation).parse(config, "translation");
        verify(completions).parse(config, "completions");
    }
    
}
