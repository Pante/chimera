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
import java.util.stream.*;

import org.apache.commons.lang.StringUtils;
    
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.karuslabs.commons.commands.Utility.trim;


public interface TabCompleter {
    
    public static final TabCompleter PLAYER_NAMES = (sender, command, alias, args) -> {
        if (args.length == 0) {
            return Collections.EMPTY_LIST;
        }
        
        String argument = args[args.length - 1];
        Stream<? extends Player> stream = sender.getServer().getOnlinePlayers().stream();
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            stream = stream.filter(p -> player.canSee(p));
        }
        
        return stream.filter(p -> StringUtils.startsWithIgnoreCase(p.getName(), argument)).map(Player::getName).collect(Collectors.toList());
    };
    
       
    public default List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String argument;
        Map<String, Command> commands = command.getNestedCommands();
        
        if (args.length == 1) {
            return command.getNestedNames().stream().filter(subcommand -> subcommand.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(argument = args[0])) {
            return commands.get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
           return onTabComplete(sender, command, alias, args);
        }
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
    
}
