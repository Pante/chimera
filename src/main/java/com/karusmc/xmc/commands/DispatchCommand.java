/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.commands;

import com.karusmc.xmc.core.*;
import com.karusmc.xmc.util.Else;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import static com.karusmc.xmc.util.Validator.*;
import static com.karusmc.xmc.util.Commands.trimArguments;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class DispatchCommand extends XMCommand implements Dispatcher {
    
    private Map<String, XMCommand> commands;
    private Else handle;
    
    
    public DispatchCommand(Plugin owningPlugin, String name) {
        this(owningPlugin, name, sender -> sender.sendMessage(ChatColor.RED + "No such command"));
    }
    
    public DispatchCommand(Plugin owningPlugin, String name, Else handle) {
        super(owningPlugin, name);
        commands = new HashMap<>();
        this.handle = handle;
    }
    
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (is(args.length >= 1 && commands.containsKey(args[0]), handle, sender)) {
            commands.get(args[0]).execute(sender, trimArguments(args));
        }
    }
    
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        String argument;
        if (args.length == 1) {
            return commands.keySet().stream().filter(command -> command.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(argument = args[0])) {
            return commands.get(argument).tabComplete(sender, argument, trimArguments(args));
            
        } else {
            return null;
        }
    }
    
    
    @Override
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
}
