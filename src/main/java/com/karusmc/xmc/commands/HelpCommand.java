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
import com.karusmc.xmc.util.*;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karusmc.xmc.util.Page.*;
import static com.karusmc.xmc.util.Validator.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 * TODO
 */
public class HelpCommand extends XMCommand implements Observer {
    
    private Map<String, XMCommand> commands;
    private Else handle;
   
    private int pageSize;
    
    
    public HelpCommand(Plugin owningPlugin, String name) {
        this(owningPlugin, name,
                sender -> sender.sendMessage(ChatColor.RED + "You either do not have permission to use this command or invalid number of arguments were specified."));
    }
   
    public HelpCommand(Plugin owningPlugin, String name, Else handle) {
        super(owningPlugin, name);
        
        commands = new HashMap<>();
        this.handle = handle;
        
        this.pageSize = 5;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (is(canUse(this, sender) && hasLength(0, args.length, 2), handle, sender)) {
            int page = getPage(getOrDefault(args, 0, ""));
            String search = getOrDefault(args, 1, "");
            
            List<String> usages = getUsages(sender, page, search);
            displayUsages(sender, page, search, usages);
        }
       
        return true;
    }
    
    
    protected List<String> getUsages(CommandSender sender, int page, String search) {
        return commands.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(search) && entry.getValue().testPermissionSilent(sender))
                .limit(page * pageSize)
                .map(entry -> entry.getValue().getUsage())
                .collect(Collectors.toList());
    }
    
    
    protected void displayUsages(CommandSender sender, int page, String search, List<String> usages) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6==== Help: &c" + search
                + " &6=== Page: &c" + page + "/" + " &6===="));
        sender.sendMessage(usages.subList(page, usages.size()).toArray(new String[0]));
    }
    

    @Override
    public void update(Observable o, Object object) {
        XMCommand command;
        if (object instanceof XMCommand && (command = (XMCommand) object).getPlugin().equals(owningPlugin)) {
            Commands.flatMapTo(command, commands);
        }
    }
    
    
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
    public void setCommands(Map<String, XMCommand> commands) {
        this.commands.clear();
        commands.values().forEach(command -> Commands.flatMapTo(command, this.commands));
    }
    
}
