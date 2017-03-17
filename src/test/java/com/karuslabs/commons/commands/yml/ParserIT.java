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

import com.karuslabs.commons.commands.CommandBuilder;
import com.karuslabs.commons.commands.Command;
import com.google.common.collect.Sets;

import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ParserIT {
    
    private Parser parser;
    
    
    public ParserIT() {
        parser = new Parser(new CommandBuilder(mock(Plugin.class)));
    }
    
    
    @Test
    public void parse() {
        Map<String, Command> commands = parser.parse("commands/commands.yml");
        
        assertEquals(1, commands.size());
        
        Command command = commands.get("command-name");
        
        assertEquals("command-name", command.getName());
        assertEquals("description", command.getDescription());
        assertEquals(Arrays.asList("cmd", "comm"), command.getAliases());
        assertEquals("command.permission", command.getPermission());
        assertEquals("message", command.getPermissionMessage());
        assertEquals(Sets.newHashSet("subcommand-name", "subcmd"), command.getNestedCommands().keySet());
        assertEquals("usage", command.getUsage());
        
        Command subcommand = command.getNestedCommands().get("subcmd");
        
        assertEquals("subcommand-name", subcommand.getName());
        assertEquals("subdescription", subcommand.getDescription());
        assertEquals(Arrays.asList("subcmd"), subcommand.getAliases());
        assertEquals("subcommand.permission", subcommand.getPermission());
        assertEquals("submessage", subcommand.getPermissionMessage());
        assertTrue(subcommand.getNestedCommands().isEmpty());
        assertEquals("subusage", subcommand.getUsage());
    }
    
}
