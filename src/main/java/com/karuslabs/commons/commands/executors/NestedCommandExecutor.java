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
package com.karuslabs.commons.commands.executors;

import com.karuslabs.commons.commands.Command;

import java.util.*;

import org.bukkit.command.CommandSender;

import static com.karuslabs.commons.commands.Utility.trim;


@FunctionalInterface
public interface NestedCommandExecutor extends CommandExecutor {
    
    public static NestedCommandExecutor NONE = (sender, command, label, args) -> {};
    
    
    @Override
    public default void execute(CommandSender sender, Command command, String label, String[] args) {
        Map<String, Command> commands = command.getNestedCommands();
        if (args.length >= 1 && commands.containsKey(args[0])) {
            commands.get(args[0]).execute(sender, label, trim(args));
            
        } else {
           onExecute(sender, command, label, args);
        }
    }
    
    public void onExecute(CommandSender sender, Command command, String label, String[] args);
    
}
