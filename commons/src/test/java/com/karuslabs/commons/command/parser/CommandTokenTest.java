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
class CommandTokenTest {
    
    References references = mock(References.class);
    NullHandle handle = mock(NullHandle.class);
    Plugin plugin = mock(Plugin.class);
    CommandsToken commands = when(mock(CommandsToken.class).from(any(), any())).thenReturn(new HashMap<>()).getMock();
    TranslationToken translation = when(mock(TranslationToken.class).from(any(), any())).thenReturn(MessageTranslation.NONE).getMock();
    CompletionsToken completions = when(mock(CompletionsToken.class).from(any(), any())).thenReturn(new HashMap<>()).getMock();
    CommandToken token = new CommandToken(references, handle, plugin, commands, translation, completions);
    
    
    @ParameterizedTest
    @CsvSource({"true, help", "false, help.aliases"})
    void isAssignable(boolean expected, String key) {
        assertEquals(expected, token.isAssignable(COMMANDS, key));
    }
    
    
    @Test
    void get() {
        ConfigurationSection config = COMMANDS.getConfigurationSection("commands.brush");
        
        Command command = token.get(COMMANDS, "commands.brush");
        assertEquals("brush", command.getName());
        assertEquals("selects the brush tool", command.getDescription());
        assertEquals("/brush [option]", command.getUsage());
        assertEquals(singletonList("b"), command.getAliases());
        assertSame(plugin, command.getPlugin());
        assertSame(CommandExecutor.NONE, command.getExecutor());
        assertEquals(MessageTranslation.NONE, command.getTranslation());
        assertEquals(EMPTY_MAP, command.getSubcommands());
        assertEquals(EMPTY_MAP, command.getCompletions());
        
        verify(commands).from(config, "subcommands");
        verify(translation).from(config, "translation");
        verify(completions).from(config, "completions");
    }
    
    
    @Test
    void getters() {
        assertEquals(commands, token.getCommandsToken());
        
        token.getReference("a key");
        verify(references, times(1)).getCommand("a key");
        
        Command command = mock(Command.class);
        token.register("a key", command);
        verify(references, times(1)).command("a key", command);

        assertSame(Command.NONE, token.getDefaultReference());
    }
    
}
