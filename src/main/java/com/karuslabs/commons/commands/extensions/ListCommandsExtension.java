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
package com.karuslabs.commons.commands.extensions;

import com.karuslabs.commons.commands.Command;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class ListCommandsExtension implements Extension {
    
    public static final ListCommandsExtension INSTANCE = new ListCommandsExtension(5);
    
    
    private int size;
    
    
    public ListCommandsExtension(int size) {
        this.size = size;
    }
            
            
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        int page = Integer.parseInt(args.length != 0 && args[0]!= "0" && args[0].matches("\\d+") ? args[0] : "", 1);
        int first = (page - 1) * size;
        int last = page * size;
        
        sender.sendMessage(ChatColor.GOLD + "Showing subcommands for: " + command.getName());
        
        List<Command> allCommands = new ArrayList<>(new HashSet<>(command.getNestedCommands().values()));
        allCommands.subList(first, Math.min(allCommands.size(), last)).forEach(cmd -> {
            if (sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.RED + cmd.getName());
            }
        });
    }
    
    
    public int getSize() {
        return size;
    }
    
}
