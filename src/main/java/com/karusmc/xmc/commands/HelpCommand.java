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
import static com.karusmc.xmc.util.Commands.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 *
 */
public class HelpCommand extends XMCommand implements CommandMapObserver {
    
    private Map<String, XMCommand> commands;
    private Else handle;
    private int size;
    
    
    public HelpCommand(Plugin owningPlugin, String name, int size) {
        this(owningPlugin, name, size,
                sender -> sender.sendMessage(ChatColor.RED + "You either do not have permission or an invalid number of arguments were specified."));
    }
   
    public HelpCommand(Plugin owningPlugin, String name, int size, Else handle) {
        super(owningPlugin, name);
        
        commands = new HashMap<>();
        this.handle = handle;
        this.size = size;
    }
    
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (is(canUse(this, sender) && hasLength(0, args.length, 2), handle, sender)) {
            int page = getPage(getArgumentOrDefault(args, 0, ""));
            String search = getArgumentOrDefault(args, 1, "");
            
            displayUsages(sender, page, search, getUsages(sender, page, search));
        }
    }
    
    
    protected String[] getUsages(CommandSender sender, int page, String search) {
        List<String> usages = commands.values().stream()
                .filter(command -> command.getName().startsWith(search) && command.testPermissionSilent(sender))
                .limit(page * size)
                .map(command -> command.getUsage())
                .collect(Collectors.toList());
        
        return usages
                .subList(Math.max(0, Math.min(usages.size(), page * size - size)), usages.size())
                .toArray(new String[0]);
    }
    
    
    protected void displayUsages(CommandSender sender, int page, String search, String[] usages) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                "&6==== Help: &c" + search
                + " &6=== Page: &c" + page + "/" + " &6===="));
        sender.sendMessage(usages);
    }
    
    
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
    public void setCommands(Map<String, XMCommand> commands) {
        this.commands.clear();
        commands.values().forEach(command -> flatMap(command, this.commands));
    }
    

    @Override
    public void register(Command command) {
        XMCommand xmCommand;
        if (command instanceof XMCommand && (xmCommand = (XMCommand) command).getPlugin().getName().equals(owningPlugin.getName())) {
            flatMap(xmCommand, commands);
        }
    }
    

    @Override
    public void registerAll(Collection<? extends Command> commands) {
        commands.forEach(this::register);
    }

    
    @Override
    public void unregister(String name) {
        commands.entrySet().removeIf(entry -> entry.getKey().startsWith(name));
    }

    @Override
    public void unregisterAll() {
        commands.clear();
    }

}
