/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.commands.reference;

import com.karusmc.commons.commands.*;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import static com.karusmc.commons.commands.Utility.trim;


public class MarshallCommand extends PluginCommand implements Marshall {
    
    private PluginCommand delegate;
    private Map<String, Command> commands;
    
    
    public MarshallCommand(PluginCommand delegate) {
        this(delegate, new HashMap<>(0));
    }
    
    public MarshallCommand(PluginCommand delegate, Map<String, Command> commands) {
        super(null, null, null);
        
        this.delegate = delegate;
        this.commands = commands;
    }
    
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1 && getCommands().containsKey(args[0])) {
            getCommands().get(args[0]).execute(sender, trim(args));
            
        } else {
           delegate.execute(sender, args);
        }
    }
    
   
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String argument;
        if (args.length == 1) {
            return getCommands().keySet().stream().filter(command -> command.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && getCommands().containsKey(argument = args[0])) {
            return getCommands().get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
            return Collections.emptyList();
        }
    }
    
    
    @Override
    public Map<String, Command> getCommands() {
        return commands;
    }


    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean setName(String name) {
        return delegate.setName(name);
    }


    @Override
    public String getPermission() {
        return delegate.getPermission();
    }

    @Override
    public void setPermission(String permission) {
        delegate.setPermission(permission);
    }


    @Override
    public boolean testPermission(CommandSender target) {
        return delegate.testPermission(target);
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return delegate.testPermissionSilent(target);
    }


    @Override
    public String getLabel() {
        return delegate.getLabel();
    }

    @Override
    public boolean setLabel(String name) {
        return delegate.setLabel(name);
    }


    @Override
    public boolean register(CommandMap commandMap) {
        return delegate.register(commandMap);
    }

    @Override
    public boolean unregister(CommandMap commandMap) {
        return delegate.unregister(commandMap);
    }
 
    @Override
    public boolean isRegistered() {
        return delegate.isRegistered();
    }

    
    @Override
    public List<String> getAliases() {
        return delegate.getAliases();
    }
    
    @Override
    public org.bukkit.command.Command setAliases(List<String> aliases) {
        return delegate.setAliases(aliases);
    }
    
    
    @Override
    public String getPermissionMessage() {
        return delegate.getPermissionMessage();
    }
    
    @Override
    public org.bukkit.command.Command setPermissionMessage(String permissionMessage) {
        return delegate.setPermissionMessage(permissionMessage);
    }

    
    @Override
    public String getDescription() {
        return delegate.getDescription();
    }
    
    @Override
    public org.bukkit.command.Command setDescription(String description) {
        return delegate.setDescription(description);
    }

    
    @Override
    public String getUsage() {
        return delegate.getUsage();
    }
    
    @Override
    public org.bukkit.command.Command setUsage(String usage) {
        return delegate.setUsage(usage);
    }


    @Override
    public Criteria getCriteria() {
        return delegate.getCriteria();
    }

    @Override
    public Plugin getPlugin() {
        return delegate.getPlugin();
    }
    
}
