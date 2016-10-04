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

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karusmc.xmc.util.Validator.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class DispatchCommand extends XMCommand implements Dispatcher {
    
    private Map<String, XMCommand> commands;
    private Else handle;
    
    
    public DispatchCommand(Plugin owningPlugin, String name, Else handle) {
        super(owningPlugin, name);
        commands = new HashMap<>();
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (isValid(hasLength(1, args.length, 100) && commands.containsKey(args[0]), handle, sender)) {
            return commands.get(args[0]).execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length - 1));
        }
    
        return true;
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
    
    
    @Override
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
    
    public Else getHandle() {
        return handle;
    }
    
    public void setHandle(Else handle) {
        this.handle = handle;
    }
    
}
