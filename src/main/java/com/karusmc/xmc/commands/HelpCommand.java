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

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karusmc.xmc.util.Page.*;
import static com.karusmc.xmc.util.Validator.*;
import java.util.stream.Collectors;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class HelpCommand extends XMCommand implements Observer {
    
    private Map<String, XMCommand> commands;
    private Else handle;
   
    private int pageSize;
    
    
    public HelpCommand(Plugin owningPlugin, String name, Map<String, XMCommand> commands) {
        this(owningPlugin, name, commands,
                sender -> sender.sendMessage(ChatColor.RED + "You either do not have permission to use this command or invalid number of arguments were specified."));
    }
   
    public HelpCommand(Plugin owningPlugin, String name, Map<String, XMCommand> commands, Else handle) {
        super(owningPlugin, name);
        
        commands.forEach(this::putCommand);
        
        this.handle = handle;
        this.pageSize = 5;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (is(canUse(this, sender) && hasLength(0, args.length, 2), handle, sender)) {

            int page = getPage(getOrDefault(args, 0, ""));
            String search = getOrDefault(args, 1, "");
            
            List<String> usages = commands.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(search) && entry.getValue().testPermissionSilent(sender))
                    .limit(page * pageSize)
                    .map(entry -> entry.getValue().getUsage())
                    .collect(Collectors.toList());
            
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6==== Help: &c" + search
                    + " &6=== Page: &c" + page + "/" + " &6===="));
            
            sender.sendMessage(usages.subList(page, usages.size()).toArray(new String[0]));
        }
       
        return true;
    }
    

    @Override
    public void update(Observable o, Object object) {
        XMCommand command;
        if (object instanceof XMCommand && (command = (XMCommand) object).getPlugin().equals(owningPlugin)) {
            putCommand(command.getName(), command);
        }
    }
    
    
    private void putCommand(String name, XMCommand command) {
        if (command instanceof Dispatcher) {
            ((Dispatcher) command).getCommands().forEach((subcommandName, subcommand) -> putCommand(name + " " + subcommandName, subcommand));
            
        } else {
            commands.put(name, command);
        }
    }

}
