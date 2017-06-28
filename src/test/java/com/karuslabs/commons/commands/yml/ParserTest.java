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

import com.google.common.collect.Sets;

import com.karuslabs.commons.commands.*;
import com.karuslabs.commons.configuration.Configurations;

import java.util.*;

import junitparams.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ParserTest {
    
    private Parser parser;
    private YamlConfiguration config;
    
    
    public ParserTest() {
        parser = spy(new Parser(mock(Plugin.class)));
        config = Configurations.from(getClass().getClassLoader().getResourceAsStream("commands/commands.yml"));
    }
    
    
    @Test
    public void parse() {
        doReturn(mock(Command.class)).when(parser).parseCommand(any(ConfigurationSection.class));
        
        parser.parse(config.getConfigurationSection("command-name.subcommands"));
        
        verify(parser).parseCommand(any(ConfigurationSection.class));
    }
    
    
    @Test
    public void parseCommand() {
        Command command = parser.parseCommand(config.getConfigurationSection("command-name"));
        
        assertEquals("command-name", command.getName());
        assertEquals("description", command.getDescription());
        assertEquals(Arrays.asList("cmd", "comm"), command.getAliases());
        assertEquals("command.permission", command.getPermission());
        assertEquals("message", command.getPermissionMessage());
        assertThat(command.getSubcommands().keySet(), equalTo(Sets.newHashSet("subcommand-name", "subcmd")));
        assertTrue(command.getOptions().containsKey("name"));
        assertEquals("usage", command.getUsage());
    }
    
    
    @Test
    public void parseCommand_NoNested() {
        Command command = parser.parseCommand(config.getConfigurationSection("command-name.subcommands.subcommand-name"));
        
        assertEquals("subcommand-name", command.getName());
        assertEquals("subdescription", command.getDescription());
        assertEquals(Arrays.asList("subcmd"), command.getAliases());
        assertEquals("subcommand.permission", command.getPermission());
        assertEquals("submessage", command.getPermissionMessage());
        assertTrue(command.getSubcommands().isEmpty());
        assertTrue(command.getOptions().isEmpty());
        assertEquals("subusage", command.getUsage());
    }
    
}
