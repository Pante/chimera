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

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import static com.karuslabs.commons.commands.Utility.trim;


@FunctionalInterface
public interface TabCompleter {
    
    public static TabCompleter INSTANCE = (sender, command, alias, args) -> {
        String argument;
        Map<String, Command> commands = command.getNestedCommands();
        
        if (args.length == 1) {
            return commands.keySet().stream().filter(subcommand -> subcommand.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(argument = args[0])) {
            return commands.get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
            return command.getAliases();
        }
    };
    
    
    public List<String> complete(CommandSender sender, Command command, String alias, String[] args);
    
}
