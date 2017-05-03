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

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public interface Extension {
    
    public static final Extension ALIASSES = (sender, command) -> sender.sendMessage(ChatColor.GOLD + "Aliases: " + ChatColor.RED + command.getAliases().toString());
       
    public static final Extension DESCRIPTION = (sender, command) -> sender.sendMessage(
        ChatColor.GOLD  + "Description: " + ChatColor.RED + command.getDescription() + ChatColor.GOLD  +"\nUsage: " + ChatColor.RED + command.getUsage()
    );
            
    public static final Extension NONE = (sender, command) -> {};
    
    public static final Extension HELP = (sender, command) -> {
        List<String> names = command.getSubcommands().values().stream()
                                    .filter(cmd -> sender.hasPermission(cmd.getPermission()))
                                    .map(Command::getName)
                                    .collect(Collectors.toList());
        
        sender.sendMessage(ChatColor.GOLD + "==== Help for: " + command.getName() + " ====");
        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RED + command.getDescription());
        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + command.getUsage());
        sender.sendMessage(ChatColor.GOLD + "\n==== Subcommands: ====" + "\n" + ChatColor.RED + names);
    };
    
    
    public void execute(CommandSender sender, Command command);
    
}
