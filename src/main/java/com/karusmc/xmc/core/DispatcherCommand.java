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
package com.karusmc.xmc.core;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karusmc.xmc.utils.Validator.hasLength;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class DispatcherCommand extends XMCommand {
    
    private Map<String, Command> commands;
    private Command defaultHandler;
    
    
    public DispatcherCommand(Plugin owningPlugin, String name, Command defaultHandler) {
        super(owningPlugin, name);
        commands = new HashMap<>();
        this.defaultHandler = defaultHandler;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (hasLength(1, args.length, 999)) {
            return commands.getOrDefault(args[0], defaultHandler).execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length - 1));
            
        } else {
            return defaultHandler.execute(sender, commandLabel, args);
        }
    }
    
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            return commands.keySet().stream().filter(command -> command.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(args[0])) {
            return commands.get(args[0]).tabComplete(sender, args[0], Arrays.copyOfRange(args, 1, args.length - 1));
        }
        
        return null;
    }
    
}
