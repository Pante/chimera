/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.commands.yml;

import com.karuslabs.commons.commands.*;

import com.google.common.collect.Sets;

import java.util.*;

import junitparams.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ParserTest {
    
    private Parser parser;
    private YamlConfiguration config;
    
    
    public ParserTest() {
        parser = spy(new Parser(new CommandBuilder(mock(Plugin.class))));
        config = YamlConfiguration.loadConfiguration(getClass().getClassLoader().getResourceAsStream("commands/commands.yml"));
    }
    
    
    @Test
    @Parameters
    public void parseCommands(boolean includeAliases, Set<String> keys) {
        Map<String, Command> commands = parser.parseCommands(config.getConfigurationSection("command-name.nested-commands"), includeAliases);
        
        verify(parser, times(1)).parseCommand(any());
        
        assertEquals(keys, commands.keySet());
    }
    
    protected Object[] parametersForParseCommands() {
        return new Object[] {
            new Object[] {true, Sets.newHashSet("subcommand-name", "subcmd")},
            new Object[] {false, Sets.newHashSet("subcommand-name")}
        };
    }
    
    
    @Test
    public void parseCommand() {
        Command command = parser.parseCommand(config.getConfigurationSection("command-name.nested-commands.subcommand-name"));
        
        assertEquals("subcommand-name", command.getName());
        assertEquals("subdescription", command.getDescription());
        assertEquals(Arrays.asList("subcmd"), command.getAliases());
        assertEquals("subcommand.permission", command.getPermission());
        assertEquals("submessage", command.getPermissionMessage());
        assertTrue(command.getNestedCommands().isEmpty());
        assertEquals("subusage", command.getUsage());
    }
    
    
    @Test
    public void parseCommands_Default() {
        Command command = parser.parseCommand(config.getConfigurationSection("blankcommand-name"));
        
        assertEquals("blankcommand-name", command.getName());
        assertEquals("", command.getDescription());
        assertEquals(Collections.EMPTY_LIST, command.getAliases());
        assertEquals("", command.getPermission());
        assertEquals("", command.getPermissionMessage());
        assertTrue(command.getNestedCommands().isEmpty());
        assertEquals("", command.getUsage());
    }
    
}
