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
package com.karuslabs.commons.commands;

import com.google.common.collect.ImmutableMap;

import org.bukkit.plugin.Plugin;

import org.junit.Test;

import static java.util.Collections.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CommandBuilderTest {
    
    private CommandBuilder builder;
    private Command subcommand;
    
    
    public CommandBuilderTest() {
        builder = new CommandBuilder(mock(Plugin.class));
        subcommand = when(mock(Command.class).getName()).thenReturn("subcommand").getMock();
        when(subcommand.getAliases()).thenReturn(singletonList("subcmd"));
    }
    
    
    @Test
    public void build() {
        CommandExecutor executor = mock(CommandExecutor.class);
        Option option = mock(Option.class);
        
        Command command = builder.name("command")
                            .description("description")
                            .aliases(singletonList("cmd"))
                            .permission("permission")
                            .message("message")
                            .usage("usage")
                            .label("label")
                            .executor(executor)
                            .subcommand(subcommand)
                            .option("opt", option)
                            .build();
        
        assertEquals("command", command.getName());
        assertEquals("description", command.getDescription());
        assertThat(command.getAliases(), equalTo(singletonList("cmd")));
        assertEquals("permission", command.getPermission());
        assertEquals("message", command.getPermissionMessage());
        assertEquals("usage", command.getUsage());
        assertEquals("label", command.getLabel());
        
        assertEquals(executor, command.getExecutor());
        
        assertThat(command.getOptions(), equalTo(singletonMap("opt", option)));
        assertThat(command.getSubcommands(), equalTo(ImmutableMap.of("subcommand", subcommand, "subcmd", subcommand)));
    }
    
}
