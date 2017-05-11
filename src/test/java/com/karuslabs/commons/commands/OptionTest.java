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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import org.junit.Test;

import static java.util.Collections.singletonList;;
import static org.mockito.Mockito.*;


public class OptionTest {
    
    private CommandSender sender;
    private Command command;
    private Command subcommand;
    
    
    public OptionTest() {
        sender = when(mock(CommandSender.class).hasPermission(anyString())).thenReturn(true).getMock();
        subcommand = new Command("subcommand", null, null);
        command = new CommandBuilder(null).name("command").aliases(singletonList("cmd")).description("desc").usage("usage").subcommand(subcommand).build();
    }
    
    
    @Test
    public void aliases() {
        Option.ALIASSES.execute(sender, command);
        
        verify(sender).sendMessage(ChatColor.GOLD + "Aliases: " + ChatColor.RED + command.getAliases().toString());
    }
    
    
    @Test
    public void description() {
        Option.DESCRIPTION.execute(sender, command);
        
        verify(sender).sendMessage(ChatColor.GOLD  + "Description: " + ChatColor.RED + command.getDescription() + ChatColor.GOLD  +"\nUsage: " + ChatColor.RED + command.getUsage());
    }
    
    
    @Test
    public void help() {
        Option.HELP.execute(sender, command);
        
        verify(sender, times(4)).sendMessage(anyString());
    }
    
}
