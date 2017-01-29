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


/**
 * Represents a decorator which decorates a command and allows it to contain subcommands.
 * Delegates all getter methods to the decorated command.
 */
public class MarshallCommand extends PluginCommand implements Marshall {
    
    private PluginCommand command;
    private Map<String, Command> commands;
    
    
    /**
     * Constructs this with the specified command.
     * 
     * @param command The command to be decorated
     */
    public MarshallCommand(PluginCommand command) {
        this(command, new HashMap<>(0));
    }
    
    /**
     * Constructs this with the specified command and subcommands.
     * 
     * @param command The command to be decorated
     * @param commands The subcommands
     */
    public MarshallCommand(PluginCommand command, Map<String, Command> commands) {
        super(null, null, null);
        
        this.command = command;
        this.commands = commands;
    }
    
    
    /**
     * Delegates execution to the decorated command or subcommands.
     * Checks if a subcommand with the same name as the first argument exists
     * and trims the arguments and delegates execution to it. Otherwise delegates
     * execution to the decorated command.
     * 
     * @param sender Source object which is executing this command
     * @param args All arguments passed to the command, split via ' '
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1 && commands.containsKey(args[0])) {
            commands.get(args[0]).execute(sender, trim(args));
            
        } else {
           command.execute(sender, args);
        }
    }
    
   
    /**
     * Delegates tab completion to the decorated command or subcommands.
     * Checks if a subcommand with a name starting with the first argument exists and adds it to a list.
     * If the number of arguments is greater than 1, and a subcommand with the same name as the first argument exists, delegate tab completion to it.
     * Otherwise return the decorated commands aliases.
     * 
     * @param sender Source object which is executing this command
     * @param alias The alias being used
     * @param args All arguments passed to the command, split via ' '
     * @return A list of tab-completions for the specified arguments. This will never be null. List may be immutable.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        String argument;
        if (args.length == 1) {
            return commands.keySet().stream().filter(command -> command.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && commands.containsKey(argument = args[0])) {
            return commands.get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
            return getAliases();
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Command> getCommands() {
        return commands;
    }


    @Override
    public String getName() {
        return command.getName();
    }

    @Override
    public boolean setName(String name) {
        return command.setName(name);
    }


    @Override
    public String getPermission() {
        return command.getPermission();
    }

    @Override
    public void setPermission(String permission) {
        command.setPermission(permission);
    }


    @Override
    public boolean testPermission(CommandSender target) {
        return command.testPermission(target);
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return command.testPermissionSilent(target);
    }


    @Override
    public String getLabel() {
        return command.getLabel();
    }

    @Override
    public boolean setLabel(String name) {
        return command.setLabel(name);
    }


    @Override
    public boolean register(CommandMap commandMap) {
        return command.register(commandMap);
    }

    @Override
    public boolean unregister(CommandMap commandMap) {
        return command.unregister(commandMap);
    }
 
    @Override
    public boolean isRegistered() {
        return command.isRegistered();
    }

    
    @Override
    public List<String> getAliases() {
        return command.getAliases();
    }
    
    @Override
    public org.bukkit.command.Command setAliases(List<String> aliases) {
        return command.setAliases(aliases);
    }
    
    
    @Override
    public String getPermissionMessage() {
        return command.getPermissionMessage();
    }
    
    @Override
    public org.bukkit.command.Command setPermissionMessage(String permissionMessage) {
        return command.setPermissionMessage(permissionMessage);
    }

    
    @Override
    public String getDescription() {
        return command.getDescription();
    }
    
    @Override
    public org.bukkit.command.Command setDescription(String description) {
        return command.setDescription(description);
    }

    
    @Override
    public String getUsage() {
        return command.getUsage();
    }
    
    @Override
    public org.bukkit.command.Command setUsage(String usage) {
        return command.setUsage(usage);
    }

    
    @Override
    public Criteria getCriteria() {
        return command.getCriteria();
    }
    
    @Override
    public Plugin getPlugin() {
        return command.getPlugin();
    }
    
}
