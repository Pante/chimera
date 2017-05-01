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

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import static com.karuslabs.commons.commands.Utility.trim;


public interface CommandExecutor {
    
    public static final CommandExecutor NONE = (sender, command, label, args) -> {};
    
    
    public default void execute(CommandSender sender, Command command, String label, String[] args) {
        Map<String, Command> commands = command.getNestedCommands();
        Map<String, Extension> extensions = command.getExtensions();
        
        boolean hasArguments = args.length >= 1;
        
        if (hasArguments && commands.containsKey(args[0])) {
            commands.get(args[0]).execute(sender, label, trim(args));
            
        } else if (hasArguments && extensions.containsKey(args[0])) {
           extensions.get(args[0]).execute(sender, command);
           
        } else if (sender.hasPermission(command.getPermission())) {
            onExecute(sender, command, label, args);
            
        } else {
            onInvalid(sender, command, label, args);
        }
    }
    
    public void onExecute(CommandSender sender, Command command, String label, String[] args);
    
    
    public default void onInvalid(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + command.getPermissionMessage());
    }
    
}
