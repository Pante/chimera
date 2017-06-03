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

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import static com.karuslabs.commons.commands.Utility.trim;


public interface CommandExecutor {
    
    public static final CommandExecutor NONE = (sender, command, label, args) -> {};
    
    public static final CommandExecutor DEFAULT = (sender, command, label, args) -> sender.sendMessage(ChatColor.RED + "Unknown command! Type /" + command.getName() + " help for more information");
    
    
    public default void execute(CommandSender sender, Command command, String label, String[] args) {
        Map<String, Command> commands = command.getSubcommands();
        Map<String, Option> options = command.getOptions();
        
        boolean hasArguments = args.length >= 1;
        if (hasArguments && commands.containsKey(args[0])) {
            commands.get(args[0]).execute(sender, label, trim(args));

        } else if (hasArguments && options.containsKey(args[0])) {
            options.get(args[0]).execute(sender, command);

        } else if (sender.hasPermission(command.getPermission())) {
            onExecute(sender, command, label, args);

        } else {
            onNoPermission(sender, command, label, args);
        }
    }
    
    public void onExecute(CommandSender sender, Command command, String label, String[] args);
    
    public default void onNoPermission(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + command.getPermissionMessage());
    }
    
    
    public default List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String argument;
        Map<String, Command> commands = command.getSubcommands();
        
        if (args.length == 1 && !command.getSubcommands().isEmpty()) {
            argument = args[0];
            return command.getSubcommands().values().stream()
                    .filter(cmd -> sender.hasPermission(cmd.getPermission()) && cmd.getName().startsWith(argument))
                    .map(Command::getName)
                    .collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(argument = args[0])) {
            return commands.get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
            return onTabComplete(sender, command, alias, args);
        }
    }
    
    public default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Collections.EMPTY_LIST;
            
        } else {
            return Argument.PLAYER_NAMES.complete(sender, command, args[args.length - 1]);
        }
    }
    
}
